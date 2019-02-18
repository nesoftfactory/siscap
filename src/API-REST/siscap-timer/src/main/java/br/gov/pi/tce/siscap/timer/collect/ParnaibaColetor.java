package br.gov.pi.tce.siscap.timer.collect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import br.gov.pi.tce.siscap.timer.model.Fonte;
import br.gov.pi.tce.siscap.timer.service.ColetorService;
import br.gov.pi.tce.siscap.timer.util.DateUtil;

@Service
public class ParnaibaColetor implements Coletor {
	private static final Logger logger = LoggerFactory.getLogger(ColetorService.class);

	private final String URL_COLETA = "http://dom.parnaiba.pi.gov.br/home?d="; 
	private final String URL_DOWNLOAD = "http://dom.parnaiba.pi.gov.br/assets/diarios/";
	private final String REGEX_FOR_DATE = "\\d{2}[\\.\\-]{1,2}\\d{2}-\\d{4}";
	private final String REGEX_FOR_PDF = "[0-9A-Za-z]+.(pdf)";

	private ColetorService coletorService;

	@Override
	public void coletar(ColetorService coletorService, Fonte fonte, 
			Date dataInicial, Date dataFinal) {
		this.coletorService = coletorService;
		
		Boolean isFinalPaginacao = Boolean.FALSE;

		List<String> arquivoList = new ArrayList<String>();
		List<LocalDate> diasUteisList = DateUtil.getDiasUteis(dataInicial, dataFinal);
		
		for (int pageDiario = 1; isFinalPaginacao.equals(Boolean.FALSE); pageDiario++) {
			isFinalPaginacao = getPaginasDiarios(fonte, String.valueOf(pageDiario), arquivoList,
					dataInicial, dataFinal, diasUteisList);
			if (isFinalPaginacao == null) {
				logger.error("Erro na fonte:" + fonte.getNome() + " - pagina - " + pageDiario);
				isFinalPaginacao = Boolean.FALSE;
			}
		}
		coletorService.checarPublicacaoInexistente(diasUteisList, fonte);
	}
	
	private Boolean getPaginasDiarios(Fonte fonte, String pageDiario, 
			List<String> arquivoList, Date dataInicial, Date dataFinal, List<LocalDate> diasUteisList) {

		Boolean isFinalPaginacao = null;
		try {
			isFinalPaginacao = lerPaginaDiario(fonte, String.valueOf(pageDiario), arquivoList,
					dataInicial, dataFinal, diasUteisList);
		} catch (MalformedURLException excecao) {
			logger.error(excecao.getMessage());
		} catch (IOException excecao) {
			logger.error(excecao.getMessage());
		}
		return isFinalPaginacao;
	}

	private Boolean lerPaginaDiario(Fonte fonte, String pageDom, 
			List<String> arquivoList, Date dataInicial, Date dataFinal,
			List<LocalDate> diasUteisList) throws IOException {

		Boolean isFinalPaginacao = null;
		String htmlCompleto = "";
		String linhaHTML;
		Date date = null;
		
		URL url = new URL(URL_COLETA + pageDom);
		BufferedReader fonteHTML = new BufferedReader(new InputStreamReader(url.openStream()));

		Pattern datePattern = Pattern.compile(REGEX_FOR_DATE);
		Pattern pdfPattern = Pattern.compile(REGEX_FOR_PDF);
		
		while ((linhaHTML = fonteHTML.readLine()) != null) {

			htmlCompleto = htmlCompleto + linhaHTML;
			if (REGEX_FOR_DATE != null && REGEX_FOR_PDF != null ) {

				Matcher matcher = datePattern.matcher(linhaHTML);
				while (matcher.find()) {
					date = DateUtil.convertToDate(matcher.group());
				}

				matcher = pdfPattern.matcher(linhaHTML);
				while (matcher.find()) {
					if (date != null) {
						
						String arquivoStr = matcher.group();
						if (dataInicial.compareTo(date) <= 0 && dataFinal.compareTo(date) >= 0) {

							String codigo = matchDiario(linhaHTML);
							String publicacaoName = recuperaNomePublicacao(linhaHTML);

							coletorService.salvarPublicacao(fonte, URL_DOWNLOAD + arquivoStr,
									date, arquivoStr, Boolean.TRUE, Boolean.FALSE,
									"Sucesso", null, null, codigo, publicacaoName);

							LocalDate localDate = DateUtil.asLocalDate(date);
							if (diasUteisList.contains(localDate)) {
								diasUteisList.remove(localDate);
							}
						} else if (date.compareTo(dataInicial) < 0) {
							isFinalPaginacao = Boolean.TRUE;
							break;
						}
						// Necessário nullar o date para evitar trazer PDFs que não contenham na
						// lista, e estes estão sem data.
						date = null;
					}
				}
			}
		}
		fonteHTML.close();
		isFinalPaginacao = htmlCompleto.contains("assets/diarios/");
		return isFinalPaginacao;
	}

	private String recuperaNomePublicacao(String linhaHTML) {
		String publicacaoName = "";

		Matcher matcherTitleTag = Pattern.compile(
				"[title=\"]+[0-9A-Za-z|\\s|_|Ã|š|-|/]+(- EDIÇÃO EXTRA)?+(-EDIÃ‡ÃƒO EXTRA)?+(_PESQUISÃVEL)?+(_EDIÃ‡ÃƒO EXTRA)?+(_EDIÃ‡ÃƒO_EXTRA_Caderno_Ãšnico)?+[\"]")
				.matcher(linhaHTML);
		if (matcherTitleTag.find()) {
			publicacaoName = matcherTitleTag.group().replace("title=", "").replace("\"", "")
					.replace("Ãš", "U").replace("EDIÃ‡ÃƒO", "EDIÇÃO")
					.replace("_PESQUISÃVEL", "_PESQUISÁVEL");
		}
		return publicacaoName;
	}

	private String matchDiario(String linhaHTML) {
		String codigo = "";
		Matcher matcherDOM = Pattern.compile("DOM_+[\\d]+").matcher(linhaHTML);
		if (matcherDOM.find()) {
			Matcher matcherCodigo = Pattern.compile("[\\d]+").matcher(matcherDOM.group());
			if (matcherCodigo.find()) {
				codigo = matcherCodigo.group();
			}
		}
		return codigo;
	}

}