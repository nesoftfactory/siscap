package br.gov.pi.tce.publicacoes.controller.beans;

import static java.util.Objects.isNull;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.Format;
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

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.GenericEntity;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;

import br.gov.pi.tce.publicacoes.clients.PublicacaoServiceClient;
import br.gov.pi.tce.publicacoes.modelo.Arquivo;
import br.gov.pi.tce.publicacoes.modelo.Feriado;
import br.gov.pi.tce.publicacoes.modelo.Fonte;
import br.gov.pi.tce.publicacoes.modelo.Publicacao;
import br.gov.pi.tce.publicacoes.modelo.PublicacaoAnexo;

/**
 * 
 * Classe responsável por executar as coletas das publicações nos sites.
 * 
 * @author Erick Guilherme Cavalcanti
 *
 */	
@Stateless
public class PublicacaoController extends BeanController{

	private static final long serialVersionUID = 1L;
	
	// URL das fontes dos diários oficiais
	public final static Long URL_FONTE_DIARIO_OFICIAL_DOS_MUNICIPIOS = Long.valueOf(2); //"http://www.diarioficialdosmunicipios.org";
	public final static Long URL_FONTE_DIARIO_OFICIAL_PIAUI = Long.valueOf(3); //"http://www.diariooficial.pi.gov.br";
	public final static Long URL_FONTE_DIARIO_OFICIAL_TERESINA = Long.valueOf(4); //"http://www.dom.teresina.pi.gov.br";
	public final static Long URL_FONTE_DIARIO_OFICIAL_PARNAIBA = Long.valueOf(5); //"http://dom.parnaiba.pi.gov.br";

	// URL das fontes utiliziadas na coleta dos diários oficiais
	public final static String URL_COLETA_DIARIO_OFICIAL_PARNAIBA = "http://dom.parnaiba.pi.gov.br/home?d=";
	public final static String URL_COLETA_DIARIO_OFICIAL_TERESINA = "http://www.dom.teresina.pi.gov.br/lista_diario.php?pagina=";
	public final static String URL_COLETA_DIARIO_OFICIAL_DOS_MUNICIPIOS = "http://www.diarioficialdosmunicipios.org/";
	public final static String URL_COLETA_DIARIO_OFICIAL_PIAUI = "http://www.diariooficial.pi.gov.br/diario.php?dia=";

	// URL Específica para realizar downloads dos diarios oficiais
	public final static String URL_DOWNLOAD_DOM_PARNAIBA = "http://dom.parnaiba.pi.gov.br/assets/diarios/";
	public final static String URL_DOWNLOAD_DOM_TERESINA = "http://www.dom.teresina.pi.gov.br/admin/upload/";
	public final static String URL_DOWNLOAD_DIARIO_OFICIAL_DOS_MUNICIPIOS = "http://www.diarioficialdosmunicipios.org/PDF/";
	public final static String URL_DOWNLOAD_DIARIO_OFICIAL_PIAUI = "http://www.diariooficial.pi.gov.br/diario/";

	public final static String PALAVRA_FINAL_DO_ARQUIVO_DOM_PARNAIBA = "assets/diarios/";
	
	@Inject
	private PublicacaoServiceClient publicacaoServiceClient;

	/**
	 * @param date
	 * @return
	 */
	private Date convertDate(String date) {

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
	 * Método resposável por chamar a API para consultar Fonte por idFonte.
	 * 
	 * @param idFonte
	 * @return fonte
	 */
	private Fonte consultarFontePorIdFonte(Long idFonte){
		Fonte fonte = publicacaoServiceClient.consultarFontePorCodigo(idFonte);
		return fonte;
	}

	/**
	 * Busca de diário oficial nas fontes de Parnaíba, Teresina e Municípios.
	 * 
	 * @param idFonte
	 * @param dataInicial
	 * @param dataFinal
	 */
	public void getDiariosDOM(Long idFonte, Date dataInicial, Date dataFinal) {

		int pageDomTeresina = 1;
		int pageDomParnaiba = 1;
		Boolean isFinalPaginacao = Boolean.FALSE;
		List<String> arquivoList = new ArrayList<String>();

		List<LocalDate> diasUteisList = getDiasUteis(dataInicial, dataFinal);

		// Lista do objeto Fontes em que consta a data e o nome do pdf
		List<Publicacao> diarios = new ArrayList<Publicacao>();
		
		Fonte fonte = consultarFontePorIdFonte(idFonte);

		if (URL_FONTE_DIARIO_OFICIAL_TERESINA.equals(idFonte)) {
			for (pageDomTeresina = 1; isFinalPaginacao.equals(Boolean.FALSE); pageDomTeresina++) {
				isFinalPaginacao = getPaginasDiariosDOM(fonte, String.valueOf(pageDomTeresina), arquivoList,
						diarios, dataInicial, dataFinal, diasUteisList);
				if (isFinalPaginacao == null) {
					System.out.println("Erro na fonte:" + idFonte + " - pagina - " + pageDomTeresina);
					isFinalPaginacao = Boolean.FALSE;
				}
			}
		} else if (URL_FONTE_DIARIO_OFICIAL_PARNAIBA.equals(idFonte)) {
			for (pageDomParnaiba = 1; isFinalPaginacao.equals(Boolean.FALSE); pageDomParnaiba++) {
				isFinalPaginacao = getPaginasDiariosDOM(fonte, String.valueOf(pageDomParnaiba), arquivoList,
						diarios, dataInicial, dataFinal, diasUteisList);
				if (isFinalPaginacao == null) {
					System.out.println("Erro na fonte:" + idFonte + " - pagina - " + pageDomParnaiba);
					isFinalPaginacao = Boolean.FALSE;
				}
			}
		} else if (URL_FONTE_DIARIO_OFICIAL_DOS_MUNICIPIOS.equals(idFonte)) {
			List<String> listaMesAnoHtml = getListaMesAnoHtml(dataInicial, dataFinal);
			for (String mesAnoHtml : listaMesAnoHtml) {
				List<String> listHtmls = getPaginasAnoMesDiarioOficialMunicipios(
						URL_COLETA_DIARIO_OFICIAL_DOS_MUNICIPIOS + mesAnoHtml);
				for (String html : listHtmls) {
					isFinalPaginacao = getPaginasDiariosDOM(fonte, html, arquivoList, diarios, dataInicial,
							dataFinal, diasUteisList);
				}
			}
		}

		for (LocalDate localDate : diasUteisList) {
			Date date = asDate(localDate);
			Boolean isFeriado = isFeriado(date, fonte.getId());
			if (!isFeriado) {
				SimpleDateFormat formatoDeData = new SimpleDateFormat("dd/MM/yyyy");
				System.out.println(idFonte + " - " + formatoDeData.format(date) + " - " + "Diario Não Encontrado");
				salvarPublicacao(fonte, "", diarios, convertDateToString(date), "", Boolean.FALSE, Boolean.FALSE, "Erro: Diario Não Encontrado", null, null, "", "");
			}
		}
	}

	/**
	 * Verifica se a data é um feriado para uma determinada fonte.
	 * 
	 * @param date
	 * @param idFonte
	 * @return retorno
	 */
	private Boolean isFeriado(Date date, Long idFonte) {
		List<Feriado> feriadoList = publicacaoServiceClient.consultarFeriadoPorFontePeriodo(idFonte, asLocalDate(date),
				asLocalDate(date));
		Boolean retorno = (feriadoList != null && !(feriadoList.isEmpty()));
		return retorno;
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
	private Boolean getPaginasDiariosDOM(Fonte fonte, String pageDom, List<String> arquivoList,
			List<Publicacao> diarios, Date dataInicial, Date dataFinal, List<LocalDate> diasUteisList) {

		String htmlCompleto = "";
		Boolean isFinalPaginacao = null;
		Boolean erroUrlFonte = Boolean.TRUE;

		try {
			String regexForDate = null, regexForPDF = null;

			// A expressão regular das datas e PDFs estão diferentes para cada Fonte.
			if (URL_FONTE_DIARIO_OFICIAL_TERESINA.equals(fonte.getId())) {
				regexForDate = "\\d{2}/\\d{2}/\\d{4}";
				regexForPDF = "[0-9A-Za-z|\\s|-]+.(pdf)";
				erroUrlFonte = Boolean.FALSE;
			} else if (URL_FONTE_DIARIO_OFICIAL_PARNAIBA.equals(fonte.getId())) {
				regexForDate = "\\d{2}[\\.\\-]{1,2}\\d{2}-\\d{4}";
				regexForPDF = "[0-9A-Za-z]+.(pdf)";
				erroUrlFonte = Boolean.FALSE;
			} else if (URL_FONTE_DIARIO_OFICIAL_DOS_MUNICIPIOS.equals(fonte.getId())) {
				regexForDate = "\\d{2}\\s+((Janeiro)|(Fevereiro)|(Março)|(Abril)|(Maio)|(Junho)|(Julho)|(Agosto)|(Setembro)|(Outubro)|(Novembro)|(Dezembro))\\s+[d][e]\\s+\\d{4}";
				regexForPDF = "[D][M]\\s+[0-9A-Za-z]+.(pdf)";
				erroUrlFonte = Boolean.FALSE;
			}

			if (erroUrlFonte) {
				System.out.println("A Fonte " + fonte.getUrl() + " não foi Encontrada.");
			} else {
				isFinalPaginacao = lerPaginaDiarioDOM(fonte, pageDom, arquivoList, diarios, htmlCompleto,
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
	private Boolean lerPaginaDiarioDOM(Fonte fonte, String pageDom, List<String> arquivoList,
			List<Publicacao> diarios, String htmlCompleto, Boolean isFinalPaginacao, String regexForDate,
			String regexForPDF, Date dataInicial, Date dataFinal, List<LocalDate> diasUteisList)
			throws MalformedURLException, IOException {

		String linhaHTML;
		Date date = null;
		String urlFonte = fonte.getUrl();

		URL url = new URL(fonte.getUrl());
		if (URL_FONTE_DIARIO_OFICIAL_PARNAIBA.equals(fonte.getId())) {
			urlFonte = URL_COLETA_DIARIO_OFICIAL_PARNAIBA + pageDom;
		}else if (URL_FONTE_DIARIO_OFICIAL_TERESINA.equals(fonte.getId())) {
			urlFonte = URL_COLETA_DIARIO_OFICIAL_TERESINA + pageDom;
		}else if (URL_FONTE_DIARIO_OFICIAL_DOS_MUNICIPIOS.equals(fonte.getId())) {
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
						if (URL_FONTE_DIARIO_OFICIAL_PARNAIBA.equals(fonte.getId())
								|| URL_FONTE_DIARIO_OFICIAL_DOS_MUNICIPIOS.equals(fonte.getId())) {

							if (dataInicial.compareTo(date) <= 0 && dataFinal.compareTo(date) >= 0) {
								if (URL_FONTE_DIARIO_OFICIAL_PARNAIBA.equals(fonte.getId())) {
									
									String codigo = "";
									Matcher matcherDOM = Pattern.compile("DOM_+[\\d]+").matcher(linhaHTML);
									if (matcherDOM.find()) {
										Matcher matcherCodigo = Pattern.compile("[\\d]+").matcher(matcherDOM.group());
										if (matcherCodigo.find()) {
											codigo = matcherCodigo.group();
										}
									}
									
									String publicacaoName = "";
									
									Matcher matcherTitleTag = Pattern.compile("[title=\"]+[0-9A-Za-z|\\s|_|Ã|š|-|/]+(- EDIÇÃO EXTRA)?+(-EDIÃ‡ÃƒO EXTRA)?+(_PESQUISÃVEL)?+(_EDIÃ‡ÃƒO EXTRA)?+(_EDIÃ‡ÃƒO_EXTRA_Caderno_Ãšnico)?+[\"]").matcher(linhaHTML);
									if (matcherTitleTag.find()) {
										publicacaoName = matcherTitleTag.group().replace("title=", "").replace("\"", "").replace("Ãš", "U").replace("EDIÃ‡ÃƒO", "EDIÇÃO").replace("_PESQUISÃVEL", "_PESQUISÁVEL");
									}
									
									salvarPublicacao(fonte, URL_DOWNLOAD_DOM_PARNAIBA + arquivoStr, diarios, convertDateToString(date),
											arquivoStr, Boolean.TRUE, Boolean.FALSE, "Sucesso", null, null, codigo, publicacaoName);// incluirDiarioOficial(urlFonte, diarios, date, arquivoStr);
								} else {
									Calendar c = Calendar.getInstance();
									c.setTime(date);
									String mes = String.format("%02d", c.get(Calendar.MONTH) + 1);
									String ano = String.valueOf(c.get(Calendar.YEAR));
									salvarPublicacao(fonte, URL_DOWNLOAD_DIARIO_OFICIAL_DOS_MUNICIPIOS + ano + mes + "/" + arquivoStr,
											diarios, convertDateToString(date), arquivoStr, Boolean.TRUE, Boolean.FALSE, "Sucesso", null, null, "", arquivoStr);// incluirDiarioOficial(urlFonte, diarios, date, arquivoStr);
								}
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
						} else if (URL_FONTE_DIARIO_OFICIAL_TERESINA.equals(fonte.getId())) {
							
							if (!arquivoList.contains(arquivoStr)) {
								arquivoList.add(arquivoStr);
								if (dataInicial.compareTo(date) <= 0 && dataFinal.compareTo(date) >= 0) {
									
									PublicacaoAnexo publicacaoAnexo = null;
									Arquivo arquivoAnexo = null;
									String publicacaoName = "";
									
									Matcher matcherATag = Pattern.compile("[>]+[0-9A-Za-z\\s|-]+[</a>]").matcher(linhaHTML);
									if (matcherATag.find()) {
										Matcher matcherName = Pattern.compile("[0-9A-Za-z\\s|-]+").matcher(matcherATag.group());
										if (matcherName.find()) {
											publicacaoName = matcherName.group();
										}
									}
									
									String codigo = "";
									Matcher matcherDOM = Pattern.compile("DOM+[\\d]+").matcher(arquivoStr);
									if (matcherDOM.find()) {
										Matcher matcherCodigo = Pattern.compile("[\\d]+").matcher(matcherDOM.group());
										if (matcherCodigo.find()) {
											codigo = matcherCodigo.group();
										}
									}
									
									do {
									linhaHTML = fonteHTML.readLine();
									Matcher matcherPositive = Pattern.compile("[0-9A-Za-z\\s|-]+.(pdf)").matcher(linhaHTML);
									Matcher matcherNegative = Pattern.compile("--").matcher(linhaHTML);

									if (matcherNegative.find()) {
										break;
									}
									
									if (matcherPositive.find()) {
										String arquivoAnexoStr = matcherPositive.group();
										arquivoAnexo = new Arquivo(arquivoAnexoStr, Long.valueOf(10), "tipo", URL_DOWNLOAD_DOM_TERESINA + arquivoAnexoStr, "conteudo".getBytes());
										publicacaoAnexo = new PublicacaoAnexo(null, arquivoAnexoStr, arquivoAnexo.getId(), true);
										break;
									}
									
									} while(true);
									
									salvarPublicacao(fonte, URL_DOWNLOAD_DOM_TERESINA + arquivoStr, diarios, convertDateToString(date), arquivoStr, Boolean.TRUE, Boolean.valueOf(publicacaoAnexo!=null), "Sucesso", publicacaoAnexo, arquivoAnexo, codigo, publicacaoName);//incluirDiarioOficial(urlFonte, diarios, date, arquivoStr);
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
		if (URL_FONTE_DIARIO_OFICIAL_PARNAIBA.equals(fonte.getId())) {
			if (htmlCompleto.contains(PALAVRA_FINAL_DO_ARQUIVO_DOM_PARNAIBA)) {
				isFinalPaginacao = Boolean.FALSE;
			} else {
				isFinalPaginacao = Boolean.TRUE;
			}
		}
		return isFinalPaginacao;
	}


	/**
	 * Método resposável por chamar a API para incluir ou alterar publicação.
	 * 
	 * @param fonte
	 * @param linkArquivoPublicacao
	 * @param diarios
	 * @param dataPublicacao
	 * @param nomeArquivoPublicacao
	 * @param isSucesso
	 * @param isAnexo
	 * @param mensagemErro
	 * @param publicacaoAnexo
	 * @param codigo
	 * @param publicacaoName
	 */
	private void salvarPublicacao(Fonte fonte, String linkArquivoPublicacao, List<Publicacao> diarios, String dataPublicacao,
			String nomeArquivoPublicacao, Boolean isSucesso, Boolean isAnexo, String mensagemErro, PublicacaoAnexo publicacaoAnexo, Arquivo arquivoAnexo, String codigo, String publicacaoName) {

		Arquivo arquivo = new Arquivo(nomeArquivoPublicacao, Long.valueOf(10),"tipo", linkArquivoPublicacao, "conteudo".getBytes());
		Publicacao publicacao = new Publicacao(fonte, publicacaoName, dataPublicacao, codigo, arquivo.getId(), isSucesso, isAnexo,Long.valueOf(1));
		Publicacao publicacaoConsultada = consultarPublicacaoPorFonteDataNomeArquivo(publicacao);
		if (publicacaoConsultada == null) {
			diarios.add(publicacao);
			try {
				publicacao = publicacaoServiceClient.cadastrarPublicacao(publicacao, arquivo);
				if(publicacaoAnexo!=null) {
					publicacaoAnexo.setPublicacao(publicacao);
					publicacaoAnexo = publicacaoServiceClient.cadastrarPublicacaoAnexo(publicacaoAnexo, arquivoAnexo);
				}
			} catch (Exception e) {
//				 TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Deu Erro.");
			}
		} else {
			if (!publicacao.getSucesso()) {
				//se quantidade for igual a 3 ou mais  - atualizar que não vai buscar mais (status) e futuramente gerar notificacao
				publicacao.setQuantidadeTentativas(publicacaoConsultada.getQuantidadeTentativas() + 1);
				//publicacaoServiceClient.alterarPublicacao(publicacao);
			}
		}
	}
	
	/**
	 * Método resposável por chamar a API para consultar publicação por Fonte, Data e NomeArquivo.
	 * 
	 * @param publicacao
	 * @return publicacaoConsultada
	 */
	private Publicacao consultarPublicacaoPorFonteDataNomeArquivo(Publicacao publicacao){
		Publicacao publicacaoConsultada = null;//new Publicacao();
		return publicacaoConsultada;
	}

	/**
	 * Método responsável por buscar os diários oficiais na fonte do Estado do Piauí.
	 * 
	 * @param dataInicial
	 * @param dataFinal
	 */
	public void getDiariosEmDiarioOficialPI(Date dataInicial, Date dataFinal) {

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
				URL url = new URL(URL_COLETA_DIARIO_OFICIAL_PIAUI + ano + mes + dia);
				BufferedReader fonteHTML = new BufferedReader(new InputStreamReader(url.openStream()));

				Fonte fonte = consultarFontePorIdFonte(URL_FONTE_DIARIO_OFICIAL_PIAUI);
				
				buscaPDF: while ((linhaHTML = fonteHTML.readLine()) != null) {
					regexForPDF = "DIARIO+[0-9]+[_]+[0-9A-Za-z]+[.][Pp][Dd][Ff]";
					Pattern pdfPattern = Pattern.compile(regexForPDF);

					matcher = pdfPattern.matcher(linhaHTML);
					while (matcher.find()) {
						if (!isNull(date)) {
							salvarPublicacao(fonte, URL_DOWNLOAD_DIARIO_OFICIAL_PIAUI  + ano + mes + "/"+ matcher.group(), diarios, convertDateToString(date), matcher.group(), Boolean.TRUE, Boolean.FALSE, "Sucesso", null, null, "", matcher.group());
							diarioEncontrado = Boolean.TRUE;
							// Ao encontrar o pdf sai do loop mais externo
							break buscaPDF;
						}
					}
				}
				if (!diarioEncontrado) {
					if (!isFeriado(date, fonte.getId())) {
						SimpleDateFormat formatoDeData = new SimpleDateFormat("dd/MM/yyyy");
						System.out.println(URL_DOWNLOAD_DIARIO_OFICIAL_PIAUI  + ano + mes + "/"+ matcher.group() + " - " + formatoDeData.format(date) + " - "
								+ "Diario Não Encontrado");
						salvarPublicacao(fonte, URL_DOWNLOAD_DIARIO_OFICIAL_PIAUI  + ano + mes + "/"+ matcher.group(), diarios, convertDateToString(date), matcher.group(), Boolean.FALSE, Boolean.FALSE, "Erro: Diario Não Encontrado", null, null, "", "");
					}
				}
				fonteHTML.close();

			} catch (MalformedURLException excecao) {
				excecao.printStackTrace();
			} catch (IOException excecao) {
				excecao.printStackTrace();
			}
		}
	}

	/**
	 * Método responsável por retornar uma lista de LocalDate dos dias uteis existentes no intervalo das datas do parâmetro.
	 * 
	 * @param dataInicial
	 * @param dataFinal
	 * @return
	 */
	private List<LocalDate> getDiasUteis(Date dataInicial, Date dataFinal) {
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

	/**
	 * Método responsável por retornar uma lista de string com os meses e anos concatenados existentes no intervalo das datas do parâmetro.
	 * 
	 * @param dataInicial
	 * @param dataFinal
	 * @return
	 */
	private List<String> getListaMesAnoHtml(Date dataInicial, Date dataFinal) {
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

	/**
	 * Converte um Date em LocalDate.
	 * 
	 * @param date
	 * @return
	 */
	public LocalDate asLocalDate(Date date) {
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
	}

	/**
	 *  Converte um LocalDate em Date.
	 * 
	 * @param localDate
	 * @return
	 */
	public Date asDate(LocalDate localDate) {
		return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}
	
	public String convertDateToString(Date data) {
		Format formatter = new SimpleDateFormat("dd/MM/yyyy");
		String dataStr = formatter.format(data);
		return dataStr;
	}

	/**
	 * Converte uma string no formato dd/MM/yyyy HH:mm:ss em data.
	 * 
	 * @param dataStr
	 * @return data
	 */
	public Date getData(String dataStr) {
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

//	public void upload() {
//		Part arquivo = null;
//		String token = tokenManager.getToken();
//		if (token == null) {
//			return;
//		}

//		MultipartFormDataOutput dataOutput = new MultipartFormDataOutput();
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
//		GenericEntity<MultipartFormDataOutput> entity = new GenericEntity<MultipartFormDataOutput>(dataOutput) {
//		};
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
//	}
	
//	public void realizarDownload() {
//		URL url;
//		try {
//			url = new URL("http://dom.parnaiba.pi.gov.br/assets/diarios/858e1f1d6fceedca3fd70610b4eb1097.pdf");
//			File file = new File("C:\\Temp\\arquivo-baixado.pdf");
//			FileUtils.copyURLToFile(url, file);
//			FileInputStream fileInputStream = new FileInputStream(file);
//			//fileInputStream.close();.asopdskjdvsok;vsdovf
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}

}
