package br.gov.pi.tce.publicacoes.controller.beans.utils;

import static java.util.Objects.isNull;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Part;
import javax.ws.rs.core.GenericEntity;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;

import br.gov.pi.tce.publicacoes.modelo.Fonte;
import br.gov.pi.tce.publicacoes.modelo.Publicacao;

/**
 * 
 * Classe responsável por executar as coletas das publicações nos sites.
 * 
 * @author Erick Guilherme Cavalcanti
 *
 */
public class ColetorPublicacaoUtil {

	// URL das fontes dos diários oficiais
	public final static String URL_FONTE_DIARIO_OFICIAL_PARNAIBA = "http://dom.parnaiba.pi.gov.br";
	public final static String URL_FONTE_DIARIO_OFICIAL_TERESINA = "http://www.dom.teresina.pi.gov.br";
	public final static String URL_FONTE_DIARIO_OFICIAL_DOS_MUNICIPIOS = "http://www.diarioficialdosmunicipios.org";
	public final static String URL_FONTE_DIARIO_OFICIAL_PIAUI = "http://www.diariooficial.pi.gov.br";

	// URL das fontes utiliziadas na coleta dos diários oficiais
	public final static String URL_COLETA_DIARIO_OFICIAL_PARNAIBA = "http://dom.parnaiba.pi.gov.br/home?d=";
	public final static String URL_COLETA_DIARIO_OFICIAL_TERESINA = "http://www.dom.teresina.pi.gov.br/lista_diario.php?pagina=";
	public final static String URL_COLETA_DIARIO_OFICIAL_DOS_MUNICIPIOS = "http://www.diarioficialdosmunicipios.org/";
	public final static String URL_COLETA_DIARIO_OFICIAL_PIAUI = "http://www.diariooficial.pi.gov.br/diarios.php";

	// URL Específica para realizar downloads dos diarios oficiais
	public final static String URL_DOWNLOAD_DOM_PARNAIBA = "http://dom.parnaiba.pi.gov.br/assets/diarios/";
	public final static String URL_DOWNLOAD_DOM_TERESINA = "http://www.dom.teresina.pi.gov.br/admin/upload/";
	public final static String URL_DOWNLOAD_DIARIO_OFICIAL_DOS_MUNICIPIOS = URL_COLETA_DIARIO_OFICIAL_DOS_MUNICIPIOS;
	public final static String URL_DOWNLOAD_DIARIO_OFICIAL_PIAUI = "http://www.diariooficial.pi.gov.br/diario.php?dia=";

	public final static String PALAVRA_FINAL_DO_ARQUIVO_DOM_PARNAIBA = "assets/diarios/";

	/**
	 * @param date
	 * @return
	 */
	private static Date convertDate(String date) {

		if (date.isEmpty()) {
			return null;
		}

		List<String> dateFormats = new ArrayList<String>(
				Arrays.asList("dd/MM/yyyy", "dd--MM-yyyy", "dd-MM-yyyy", "d' 'MMMM' de 'yyyy"));

		for (String dateFormatString : dateFormats) {
			try {
				SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatString);
				dateFormat.setLenient(false);
				return dateFormat.parse(date.trim());
			} catch (ParseException pe) {
			}
		}
		return null;
	}

	/**
	 * Busca de diário oficial nas fontes de Parnaíba, Teresina e Municípios.
	 * 
	 * @param urlString
	 * @param dataInicial
	 * @param dataFinal
	 */
	public static void getDiariosDOM(String urlString, Date dataInicial, Date dataFinal) {

		int pageDomTeresina = 1;
		int pageDomParnaiba = 1;
		Boolean isFinalPaginacao = Boolean.FALSE;
		List<String> arquivoList = new ArrayList<String>();

		List<LocalDate> diasUteisList = getDiasUteis(dataInicial, dataFinal);

		// Lista do objeto Fontes em que consta a data e o nome do pdf
		List<Publicacao> diarios = new ArrayList<Publicacao>();

		if (urlString.equals(URL_FONTE_DIARIO_OFICIAL_TERESINA)) {
			for (pageDomTeresina = 1; isFinalPaginacao.equals(Boolean.FALSE); pageDomTeresina++) {
				isFinalPaginacao = getPaginasDiariosDOM(urlString, String.valueOf(pageDomTeresina), arquivoList,
						diarios, dataInicial, dataFinal, diasUteisList);
				if (isFinalPaginacao == null) {
					System.out.println("Erro na fonte:" + urlString + " - pagina - " + pageDomTeresina);
					isFinalPaginacao = Boolean.FALSE;
				}
			}
		} else if (urlString.equals(URL_FONTE_DIARIO_OFICIAL_PARNAIBA)) {
			for (pageDomParnaiba = 1; isFinalPaginacao.equals(Boolean.FALSE); pageDomParnaiba++) {
				isFinalPaginacao = getPaginasDiariosDOM(urlString, String.valueOf(pageDomParnaiba), arquivoList,
						diarios, dataInicial, dataFinal, diasUteisList);
				if (isFinalPaginacao == null) {
					System.out.println("Erro na fonte:" + urlString + " - pagina - " + pageDomParnaiba);
					isFinalPaginacao = Boolean.FALSE;
				}
			}
		} else if (urlString.equals(URL_FONTE_DIARIO_OFICIAL_DOS_MUNICIPIOS)) {
			List<String> listaMesAnoHtml = getListaMesAnoHtml(dataInicial, dataFinal);
			for (String mesAnoHtml : listaMesAnoHtml) {
				List<String> listHtmls = getPaginasAnoMesDiarioOficialMunicipios(
						URL_COLETA_DIARIO_OFICIAL_DOS_MUNICIPIOS + mesAnoHtml);
				for (String html : listHtmls) {
					isFinalPaginacao = getPaginasDiariosDOM(urlString, html, arquivoList, diarios, dataInicial,
							dataFinal, diasUteisList);
				}
			}
		}

		exibirDiariosConsole(diarios);

		for (LocalDate localDate : diasUteisList) {
			Date date = asDate(localDate);
			if (!isFeriado(date, urlString)) {
				SimpleDateFormat formatoDeData = new SimpleDateFormat("dd/MM/yyyy");
				System.out.println(urlString + " - " + formatoDeData.format(date) + " - " + "Diario Não Encontrado");
			}
		}
	}

	/**
	 * Verifica se a data é um feriado para uma determinada fonte.
	 * 
	 * @param date
	 * @param idFonte
	 * @return
	 */
	private static Boolean isFeriado(Date date, String idFonte) {
		return Boolean.TRUE;
	}

	/**
	 * @param urlString
	 * @param pageDom
	 * @param arquivoList
	 * @param diarios
	 * @param dataInicial
	 * @param dataFinal
	 * @param diasUteisList
	 * @return
	 */
	private static Boolean getPaginasDiariosDOM(String urlString, String pageDom, List<String> arquivoList,
			List<Publicacao> diarios, Date dataInicial, Date dataFinal, List<LocalDate> diasUteisList) {

		String htmlCompleto = "";
		Boolean isFinalPaginacao = null;
		Boolean erroUrlFonte = Boolean.TRUE;

		try {
			String regexForDate = null, regexForPDF = null;

			// A expressão regular das datas e PDFs estão diferentes para cada URL
			if (urlString.equals(URL_FONTE_DIARIO_OFICIAL_TERESINA)) {
				regexForDate = "\\d{2}/\\d{2}/\\d{4}";
				regexForPDF = "DOM+[0-9]+A?+[-]+[A|B-B-|A-A-|0-9]+[-]?+\\s?+[0-9A-Za-z]+\\s?+[(]?+[0-9]?+[)]?+.(pdf|rar|exe)";
				erroUrlFonte = Boolean.FALSE;
			} else if (urlString.equals(URL_FONTE_DIARIO_OFICIAL_PARNAIBA)) {
				regexForDate = "\\d{2}[\\.\\-]{1,2}\\d{2}-\\d{4}";// regexForDate = "\\d{2}-\\d{2}-\\d{4}";
				regexForPDF = "[0-9A-Za-z]+[.][Pp][Dd][Ff]";
				erroUrlFonte = Boolean.FALSE;
			} else if (urlString.equals(URL_FONTE_DIARIO_OFICIAL_DOS_MUNICIPIOS)) {
				regexForDate = "\\d{2}\\s+((Janeiro)|(Fevereiro)|(Março)|(Abril)|(Maio)|(Junho)|(Julho)|(Agosto)|(Setembro)|(Outubro)|(Novembro)|(Dezembro))\\s+[d][e]\\s+\\d{4}";
				regexForPDF = "[D][M]\\s+[0-9A-Za-z]+[.][Pp][Dd][Ff]";// regexForPDF =
																		// "[D][M]\\s+[0-9]+[.][Pp][Dd][Ff]";
				// Em particular, o diário oficial dos municipios também requer a busca em uma
				// página específica
				//urlString = URL_COLETA_DIARIO_OFICIAL_DOS_MUNICIPIOS;
				erroUrlFonte = Boolean.FALSE;
			}

			if (erroUrlFonte) {
				System.out.println("A Fonte " + urlString + " não foi Encontrada.");
			} else {
				isFinalPaginacao = lerPaginaDiarioDOM(urlString, pageDom, arquivoList, diarios, htmlCompleto,
						isFinalPaginacao, regexForDate, regexForPDF, dataInicial, dataFinal, diasUteisList);
			}

		} catch (MalformedURLException excecao) {
			excecao.printStackTrace();
		} catch (IOException excecao) {
			excecao.printStackTrace();
		}

		return isFinalPaginacao;
	}

	/**
	 * Método responsável por buscar os htmls de um dos meses da fonte do diário oficial dos municípios.
	 * 
	 * @param urlString
	 * @return
	 */
	private static List<String> getPaginasAnoMesDiarioOficialMunicipios(String urlString) {
		String regexForPDF = "[0-9A-Za-z]+[.][Hh][Tt][Mm][Ll]";// String regexForPDF = "[0-9]+[.][Hh][Tt][Mm][Ll]";
		String linhaHTML = "";
		List<String> listHtmls = new ArrayList<String>();
		Matcher matcher = null;

		try {
			URL url = new URL(urlString);
			BufferedReader fonteHTML = new BufferedReader(new InputStreamReader(url.openStream()));
			Pattern pdfPattern = Pattern.compile(regexForPDF);

			while ((linhaHTML = fonteHTML.readLine()) != null) {
				if ((!isNull(regexForPDF))) {
					matcher = pdfPattern.matcher(linhaHTML);
					while (matcher.find()) {
						String arquivoStr = matcher.group();
						listHtmls.add(arquivoStr);
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();// não encontrou o html
		} catch (IOException e) {
			e.printStackTrace();
		}

		return listHtmls;
	}

	/**
	 * @param urlString
	 * @param pageDom
	 * @param arquivoList
	 * @param diarios
	 * @param htmlCompleto
	 * @param isFinalPaginacao
	 * @param regexForDate
	 * @param regexForPDF
	 * @param dataInicial
	 * @param dataFinal
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private static Boolean lerPaginaDiarioDOM(String urlString, String pageDom, List<String> arquivoList,
			List<Publicacao> diarios, String htmlCompleto, Boolean isFinalPaginacao, String regexForDate,
			String regexForPDF, Date dataInicial, Date dataFinal, List<LocalDate> diasUteisList)
			throws MalformedURLException, IOException {

		String linhaHTML;
		Date date = null;
		String urlFonte = urlString;

		URL url = new URL(urlString);
		if (urlString.equals(URL_FONTE_DIARIO_OFICIAL_PARNAIBA)) {
			urlFonte = URL_COLETA_DIARIO_OFICIAL_PARNAIBA + pageDom;
		}else if (urlString.equals(URL_FONTE_DIARIO_OFICIAL_TERESINA)) {
			urlFonte = URL_COLETA_DIARIO_OFICIAL_TERESINA + pageDom;
		}else if (urlString.equals(URL_FONTE_DIARIO_OFICIAL_DOS_MUNICIPIOS)) {
			urlFonte = URL_COLETA_DIARIO_OFICIAL_DOS_MUNICIPIOS + pageDom;
		}
		url = new URL(urlFonte);
		BufferedReader fonteHTML = new BufferedReader(new InputStreamReader(url.openStream()));
		Pattern datePattern = Pattern.compile(regexForDate);
		Pattern pdfPattern = Pattern.compile(regexForPDF);

		while ((linhaHTML = fonteHTML.readLine()) != null) {

			htmlCompleto = htmlCompleto + linhaHTML;
			if ((!isNull(regexForDate)) && (!isNull(regexForPDF))) {

				Matcher matcher = datePattern.matcher(linhaHTML);
				while (matcher.find()) {
					date = convertDate(matcher.group());
				}

				matcher = pdfPattern.matcher(linhaHTML);
				while (matcher.find()) {
					if (!isNull(date)) {
						String arquivoStr = matcher.group();
						if (urlString.equals(URL_FONTE_DIARIO_OFICIAL_PARNAIBA)
								|| urlString.equals(URL_FONTE_DIARIO_OFICIAL_DOS_MUNICIPIOS)) {

							if (dataInicial.compareTo(date) <= 0 && dataFinal.compareTo(date) >= 0) {
								incluirDiarioOficial(urlString, diarios, date, arquivoStr);//incluirDiarioOficial(urlFonte, diarios, date, arquivoStr);
								LocalDate localDate = asLocalDate(date);
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
						} else if (urlString.equals(URL_FONTE_DIARIO_OFICIAL_TERESINA)) {
							if (!arquivoList.contains(arquivoStr)) {
								arquivoList.add(arquivoStr);
								if (dataInicial.compareTo(date) <= 0 && dataFinal.compareTo(date) >= 0) {
									incluirDiarioOficial(urlString, diarios, date, arquivoStr);//incluirDiarioOficial(urlFonte, diarios, date, arquivoStr);
									LocalDate localDate = asLocalDate(date);
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
			}
		}
		fonteHTML.close();
		if (urlString.equals(URL_FONTE_DIARIO_OFICIAL_PARNAIBA)) {
			if (htmlCompleto.contains(PALAVRA_FINAL_DO_ARQUIVO_DOM_PARNAIBA)) {
				isFinalPaginacao = Boolean.FALSE;
			} else {
				isFinalPaginacao = Boolean.TRUE;
			}
		}
		return isFinalPaginacao;
	}


	/**
	 * Método resposável por chamar a API para incluir a publicação.
	 * 
	 * @param linkArquivoPublicacao
	 * @param diarios
	 * @param dataPublicacao
	 * @param nomeArquivoPublicacao
	 */
	private static void incluirDiarioOficial(String linkArquivoPublicacao, List<Publicacao> diarios,
			Date dataPublicacao, String nomeArquivoPublicacao) {
		diarios.add(new Publicacao(new Fonte(), "nomePublicacao", dataPublicacao, "codigoPublicacao", nomeArquivoPublicacao, linkArquivoPublicacao,
				"arquivoPublicacao", Boolean.TRUE, Boolean.TRUE, Long.valueOf(1)));
		// diarios.add(new Publicacao(urlString, date, arquivoStr));
	}

	public static void getDiariosEmDiarioOficialPI(Date dataInicial, Date dataFinal) {

		// Lista do objeto Fontes em que consta a data e o nome do pdf
		List<Publicacao> diarios = new ArrayList<Publicacao>();

		List<LocalDate> localDateList = getDiasUteis(dataInicial, dataFinal);
		for (LocalDate localDate : localDateList) {

			try {
				Boolean diarioEncontrado = Boolean.FALSE;
				String linhaHTML, regexForPDF = null;
				Date date = asDate(localDate);
				Calendar c = Calendar.getInstance();
				c.setTime(date);
				String dia = String.format("%02d", c.get(Calendar.DATE));
				String mes = String.format("%02d", c.get(Calendar.MONTH) + 1); // janeiro = 0
				String ano = String.valueOf(c.get(Calendar.YEAR));
				Matcher matcher = null;

				// Constrói nova url para coletar o PDF e Busca em nova fonte
				URL url = new URL(URL_DOWNLOAD_DIARIO_OFICIAL_PIAUI + ano + mes + dia);
				BufferedReader fonteHTML = new BufferedReader(new InputStreamReader(url.openStream()));

				buscaPDF: while ((linhaHTML = fonteHTML.readLine()) != null) {
					regexForPDF = "DIARIO+[0-9]+[_]+[0-9A-Za-z]+[.][Pp][Dd][Ff]";
					Pattern pdfPattern = Pattern.compile(regexForPDF);

					matcher = pdfPattern.matcher(linhaHTML);
					while (matcher.find()) {
						if (!isNull(date)) {
							incluirDiarioOficial(URL_COLETA_DIARIO_OFICIAL_PIAUI, diarios, date, matcher.group());
							diarioEncontrado = Boolean.TRUE;
							// Ao encontrar o pdf sai do loop mais externo
							break buscaPDF;
						}
					}
				}
				if (!diarioEncontrado) {
					if (!isFeriado(date, URL_DOWNLOAD_DIARIO_OFICIAL_PIAUI)) {
						SimpleDateFormat formatoDeData = new SimpleDateFormat("dd/MM/yyyy");
						System.out.println(URL_COLETA_DIARIO_OFICIAL_PIAUI + " - " + formatoDeData.format(date) + " - "
								+ "Diario Não Encontrado");
					}
				}
				fonteHTML.close();

			} catch (MalformedURLException excecao) {
				excecao.printStackTrace();
			} catch (IOException excecao) {
				excecao.printStackTrace();
			}
		}

		exibirDiariosConsole(diarios);
	}

	/**
	 * 
	 * 
	 * @param dataInicial
	 * @param dataFinal
	 * @return
	 */
	private static List<LocalDate> getDiasUteis(Date dataInicial, Date dataFinal) {
		LocalDate lda = asLocalDate(dataInicial);
		LocalDate ldb = asLocalDate(dataFinal);

		List<LocalDate> dates = new ArrayList<>();
		for (LocalDate ld = lda; !ld.isAfter(ldb); ld = ld.plusDays(1)) {
			if (!ld.getDayOfWeek().equals(DayOfWeek.SATURDAY) && !ld.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
				dates.add(ld);
			}
		}
		return dates;
	}

	private static List<String> getListaMesAnoHtml(Date dataInicial, Date dataFinal) {
		LocalDate lda = asLocalDate(dataInicial);
		LocalDate ldb = asLocalDate(dataFinal);

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

	public static LocalDate asLocalDate(Date date) {
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
	}

	/**
	 *  
	 * 
	 * @param localDate
	 * @return
	 */
	public static Date asDate(LocalDate localDate) {
		return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}

	/**
	 *
	 */
	private static void exibirDiariosConsole(List<Publicacao> diarios) {
		// Formato de exibicao da data
		SimpleDateFormat formatoDeData = new SimpleDateFormat("dd/MM/yyyy");
		for (Publicacao diario : diarios) {
			System.out.println(diario.getLinkArquivoPublicacao() + " - "
					+ formatoDeData.format(diario.getDataPublicacao()) + " - " + diario.getNomeArquivoPublicacao());
		}
	}

	/**
	 * Converte uma string no formato dd/MM/yyyy HH:mm:ss em data.
	 * 
	 * @param dataStr
	 * @return data
	 */
	public static Date getData(String dataStr) {
		Date data = null;
		try {
			SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Date dataParseada = formatoData.parse(dataStr);
			data = dataParseada;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return data;
	}

	public void upload() {

		Part arquivo = null;

//		String token = tokenManager.getToken();
//		if (token == null) {
//			return;
//		}

		MultipartFormDataOutput dataOutput = new MultipartFormDataOutput();
//		try {
//			dataOutput.addFormData("uploadedFile", arquivo.getInputStream(), MediaType.TEXT_PLAIN_TYPE.withCharset("utf-8")
//					, arquivo.getSubmittedFileName());
//		} catch (IOException e) {
//			FacesContext.getCurrentInstance().addMessage(null, 
//					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao recuperar o conteúdo do arquivo", 
//							"Erro ao recuperar o conteúdo do arquivo"));
//		}
//		
//		dataOutput.addFormData("login", this.getLogin(), MediaType.TEXT_PLAIN_TYPE);
//		dataOutput.addFormData("protocolo", this.getProtocolo(), MediaType.TEXT_PLAIN_TYPE);
//		dataOutput.addFormData("tipo", this.getTipoAto(), MediaType.TEXT_PLAIN_TYPE);
//
		GenericEntity<MultipartFormDataOutput> entity = new GenericEntity<MultipartFormDataOutput>(dataOutput) {
		};
//		
//		Client client = ClientBuilder.newClient();
//
////		String urlApp = "http://jabbah:8081/protocolows/";
//		String urlApp = "http://localhost:8080/ProtocoloWS/";
//		Response response = client.target(urlApp + "upload/ato")
//				.request(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + tokenManager.getToken())
//				.post(Entity.entity(entity, MediaType.MULTIPART_FORM_DATA_TYPE));
//
//		if (response.getStatus() == Status.OK.getStatusCode()) {
//			FacesContext.getCurrentInstance().addMessage(null, 
//					new FacesMessage(FacesMessage.SEVERITY_INFO, response.getStatusInfo().toString(), 
//							response.getStatusInfo().toString()));
//		} else {
//			FacesContext.getCurrentInstance().addMessage(null, 
//					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Não foi possível incluir o ato: " + response.getStatusInfo().toString(), 
//							response.getStatusInfo().toString()));
//		}
	}

}
