package br.gov.pi.tce.siscap.timer.collect;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
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
public class MunicipiosColetor implements Coletor {
	private static final Logger logger = LoggerFactory.getLogger(ColetorService.class);

	private final String URL_COLETA = "http://www.diarioficialdosmunicipios.org/"; 
	private final String URL_DOWNLOAD = "http://www.diarioficialdosmunicipios.org/PDF/";
	private final String REGEX_FOR_DATE = "\\d{2}\\s+((Janeiro)|(Fevereiro)|(Março)|(Abril)|(Maio)|(Junho)|(Julho)|(Agosto)|(Setembro)|(Outubro)|(Novembro)|(Dezembro))\\s+[d][e]\\s+\\d{4}";
	private final String REGEX_FOR_PDF = "[D][M]\\s+[0-9A-Za-z]+.(pdf)";

	private ColetorService coletorService;

	@Override
	public void coletar(ColetorService coletorService, Fonte fonte, 
			Date dataInicial, Date dataFinal) {
		this.coletorService = coletorService;
		
		List<String> arquivoList = new ArrayList<String>();
		List<LocalDate> diasUteisList = DateUtil.getDiasUteis(dataInicial, dataFinal);
		
		List<String> listaMesAnoHtml = getListaMesAnoHtml(dataInicial, dataFinal);
		for (String mesAnoHtml : listaMesAnoHtml) {
			List<String> listHtmls = 
					getPaginasAnoMesDiarioOficialMunicipios(URL_COLETA + mesAnoHtml);
			for (String html : listHtmls) {
				getPaginasDiarios(fonte, html, arquivoList, dataInicial, dataFinal, diasUteisList);
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
		
		logger.info("Lendo página: " + URL_COLETA + pageDom);
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

							Calendar c = Calendar.getInstance();
							c.setTime(date);
							String mes = String.format("%02d", c.get(Calendar.MONTH) + 1);
							String ano = String.valueOf(c.get(Calendar.YEAR));

							coletorService.salvarPublicacao(fonte, URL_DOWNLOAD + ano + mes + "/" + arquivoStr,
									date, arquivoStr, Boolean.TRUE, Boolean.FALSE,
									"Sucesso", null, null, "", arquivoStr);

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
		return isFinalPaginacao;
	}

	private List<String> getListaMesAnoHtml(Date dataInicial, Date dataFinal) {
		LocalDate lda = DateUtil.asLocalDate(dataInicial);
		LocalDate ldb = DateUtil.asLocalDate(dataFinal);

		List<String> listaMesAno = new ArrayList<String>();
		String mes = "";
		String ano = "";
		String anoMesHtml = "";
		for (LocalDate ld = lda; !ld.isAfter(ldb); ld = ld.plusDays(1)) {
			ano = String.valueOf(ld.getYear());
			mes = String.format("%02d", ld.getMonthValue());
			anoMesHtml = ano + mes + ".html";
			if (!listaMesAno.contains(anoMesHtml)) {
				listaMesAno.add(anoMesHtml);
			}
		}
		return listaMesAno;
	}

	private List<String> getPaginasAnoMesDiarioOficialMunicipios(String urlString) {
		String regexForPDF = "[0-9A-Za-z]+[.][Hh][Tt][Mm][Ll]";
		String linhaHTML = "";
		List<String> listHtmls = new ArrayList<String>();
		Matcher matcher = null;

		try {
			URL url = new URL(urlString);
			BufferedReader fonteHTML = new BufferedReader(new InputStreamReader(url.openStream()));
			Pattern pdfPattern = Pattern.compile(regexForPDF);

			while ((linhaHTML = fonteHTML.readLine()) != null) {
				if ((regexForPDF != null)) {
					matcher = pdfPattern.matcher(linhaHTML);
					while (matcher.find()) {
						String arquivoStr = matcher.group();
						listHtmls.add(arquivoStr);
					}
				}
			}

		} catch (FileNotFoundException e) {
			logger.error("Erro: HTML da página não foi encontrado.");
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}

		return listHtmls;
	}
}