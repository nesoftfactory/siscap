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

import br.gov.pi.tce.siscap.timer.model.Arquivo;
import br.gov.pi.tce.siscap.timer.model.Fonte;
import br.gov.pi.tce.siscap.timer.model.PublicacaoAnexo;
import br.gov.pi.tce.siscap.timer.service.ColetorService;
import br.gov.pi.tce.siscap.timer.util.DateUtil;

@Service
public class TeresinaColetor implements Coletor {
	private static final Logger logger = LoggerFactory.getLogger(ColetorService.class);

	private final String URL_COLETA = "http://www.dom.teresina.pi.gov.br/lista_diario.php?pagina="; 
	private final String URL_DOWNLOAD = "http://www.dom.teresina.pi.gov.br/admin/upload/";
	private final String REGEX_FOR_DATE = "\\d{2}/\\d{2}/\\d{4}";
	private final String REGEX_FOR_PDF = "[0-9A-Za-z|\\s|-]+[\\(\\d\\)]*+.(pdf)";

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
			logger.error("Erro de URL mal formada: " + excecao.getMessage());
		} catch (IOException excecao) {
			logger.error("Erro ao ler a página: " + excecao.getMessage());
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
					
					String arquivoStr = matcher.group();
					if (!arquivoList.contains(arquivoStr)) {
						arquivoList.add(arquivoStr);
						if (dataInicial.compareTo(date) <= 0 && dataFinal.compareTo(date) >= 0) {

							PublicacaoAnexo publicacaoAnexo = null;
							Arquivo arquivoAnexo = null;
							
							String publicacaoName = recuperaNomePublicacao(linhaHTML);
							String codigo = matchDiario(arquivoStr);

							do {
								linhaHTML = fonteHTML.readLine();
								Matcher matcherPositive = Pattern.compile("[0-9A-Za-z\\s|-]+.(pdf)")
										.matcher(linhaHTML);
								Matcher matcherNegative = Pattern.compile("--").matcher(linhaHTML);

								if (matcherNegative.find()) {
									break;
								}

								if (matcherPositive.find()) {
									String arquivoAnexoStr = matcherPositive.group();
									arquivoAnexo = new Arquivo(arquivoAnexoStr, Long.valueOf(10), "tipo",
											URL_DOWNLOAD + arquivoAnexoStr, "conteudo".getBytes());
									publicacaoAnexo = new PublicacaoAnexo(null, arquivoAnexoStr,
											arquivoAnexo.getId(), true);
									break;
								}
							} while (true);

							coletorService.salvarPublicacao(fonte, URL_DOWNLOAD + arquivoStr,
									date, arquivoStr, Boolean.TRUE,
									Boolean.valueOf(publicacaoAnexo != null), "Sucesso", publicacaoAnexo,
									arquivoAnexo, codigo, publicacaoName);
							
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
						isFinalPaginacao = Boolean.FALSE;
					} else {
						isFinalPaginacao = Boolean.TRUE;
						return isFinalPaginacao;
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
		Matcher matcherATag = Pattern.compile("[>]+[0-9A-Za-z\\s|-]+[</a>]")
				.matcher(linhaHTML);
		if (matcherATag.find()) {
			Matcher matcherName = Pattern.compile("[0-9A-Za-z\\s|-]+")
					.matcher(matcherATag.group());
			if (matcherName.find()) {
				publicacaoName = matcherName.group();
			}
		}
		return publicacaoName;
	}

	private String matchDiario(String arquivoStr) {
		String codigo = "";
		Matcher matcherDOM = Pattern.compile("DOM+[\\d]+").matcher(arquivoStr);
		if (matcherDOM.find()) {
			Matcher matcherCodigo = Pattern.compile("[\\d]+").matcher(matcherDOM.group());
			if (matcherCodigo.find()) {
				codigo = matcherCodigo.group();
			}
		}
		return codigo;
	}
}