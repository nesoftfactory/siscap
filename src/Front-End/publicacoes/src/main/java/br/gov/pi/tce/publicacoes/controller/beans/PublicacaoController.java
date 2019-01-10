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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import br.gov.pi.tce.publicacoes.clients.PublicacaoServiceClient;
import br.gov.pi.tce.publicacoes.modelo.Arquivo;
import br.gov.pi.tce.publicacoes.modelo.Feriado;
import br.gov.pi.tce.publicacoes.modelo.Fonte;
import br.gov.pi.tce.publicacoes.modelo.Notificacao;
import br.gov.pi.tce.publicacoes.modelo.NotificacaoConfig;
import br.gov.pi.tce.publicacoes.modelo.NotificacaoN;
import br.gov.pi.tce.publicacoes.modelo.Publicacao;
import br.gov.pi.tce.publicacoes.modelo.PublicacaoAnexo;
import br.gov.pi.tce.publicacoes.modelo.PublicacaoN;
import br.gov.pi.tce.publicacoes.modelo.enums.NotificacaoTipo;
import br.gov.pi.tce.publicacoes.services.NotificacaoService;
import br.gov.pi.tce.publicacoes.util.Propriedades;

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
	
	private static final Logger LOGGER = Logger.getLogger(PublicacaoController.class);
	
	@Inject
	private PublicacaoServiceClient publicacaoServiceClient;
	
	@EJB
	private NotificacaoService notificacao;
	
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
	private Fonte consultarFontePorIdFonte(Long idFonte, String token){
		if (token != null) {
			publicacaoServiceClient = new PublicacaoServiceClient(token);
		}
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
	public void getDiariosDOM(Long idFonte, Date dataInicial, Date dataFinal, String token) {

		int pageDomTeresina = 1;
		int pageDomParnaiba = 1;
		Boolean isFinalPaginacao = Boolean.FALSE;
		List<String> arquivoList = new ArrayList<String>();
		
		Propriedades propriedades = Propriedades.getInstance();
		//notificacao.sendEmail(propriedades.getValorString("EMAIL_TO"), propriedades.getValorString("EMAIL_FROM"), propriedades.getValorString("EMAIL_SUBJECT"), propriedades.getValorString("EMAIL_CONTENT"));

		List<LocalDate> diasUteisList = getDiasUteis(dataInicial, dataFinal);

		Fonte fonte = consultarFontePorIdFonte(idFonte, token);

		if (propriedades.getValorLong("ID_FONTE_DIARIO_OFICIAL_TERESINA").equals(idFonte)) {
			for (pageDomTeresina = 1; isFinalPaginacao.equals(Boolean.FALSE); pageDomTeresina++) {
				isFinalPaginacao = getPaginasDiariosDOM(fonte, String.valueOf(pageDomTeresina), arquivoList,
						dataInicial, dataFinal, diasUteisList, token);
				if (isFinalPaginacao == null) {
					LOGGER.error("Erro na fonte:" + idFonte + " - pagina - " + pageDomTeresina);
					isFinalPaginacao = Boolean.FALSE;
				}
			}
			salvarPublicacaoInexistente(diasUteisList, fonte, token);
		} else if (propriedades.getValorLong("ID_FONTE_DIARIO_OFICIAL_PARNAIBA").equals(idFonte)) {
			for (pageDomParnaiba = 1; isFinalPaginacao.equals(Boolean.FALSE); pageDomParnaiba++) {
				isFinalPaginacao = getPaginasDiariosDOM(fonte, String.valueOf(pageDomParnaiba), arquivoList,
						dataInicial, dataFinal, diasUteisList, token);
				if (isFinalPaginacao == null) {
					LOGGER.error("Erro na fonte:" + idFonte + " - pagina - " + pageDomParnaiba);
					isFinalPaginacao = Boolean.FALSE;
				}
			}
			salvarPublicacaoInexistente(diasUteisList, fonte, token);
		} else if (propriedades.getValorLong("ID_FONTE_DIARIO_OFICIAL_DOS_MUNICIPIOS").equals(idFonte)) {
			List<String> listaMesAnoHtml = getListaMesAnoHtml(dataInicial, dataFinal);
			for (String mesAnoHtml : listaMesAnoHtml) {
				List<String> listHtmls = getPaginasAnoMesDiarioOficialMunicipios(
						propriedades.getValorString("URL_COLETA_DIARIO_OFICIAL_DOS_MUNICIPIOS") + mesAnoHtml);
				for (String html : listHtmls) {
					isFinalPaginacao = getPaginasDiariosDOM(fonte, html, arquivoList, dataInicial, dataFinal,
							diasUteisList, token);
				}
			}
			salvarPublicacaoInexistente(diasUteisList, fonte, token);
		} else {
			salvarPublicacaoInexistente(diasUteisList, fonte, token);
		}

	}

	/**
	 * Metodo resposnavel por salvar as publicacoes inexistentes
	 * 
	 * @param diasUteisList
	 * @param fonte
	 */
	private void salvarPublicacaoInexistente(List<LocalDate> diasUteisList, Fonte fonte, String token) {
		for (LocalDate localDate : diasUteisList) {
			Date date = asDate(localDate);
			Boolean isFeriado = isFeriado(date, fonte.getId());
			if (!isFeriado) {
				Propriedades propriedades = Propriedades.getInstance();
				LOGGER.info(propriedades.getValorString("EMAIL_CONTENT") + fonte.getNome() + propriedades.getValorString("EMAIL_CONTENT_2") + convertDateToString(date) + ".");
				salvarPublicacao(fonte, "", convertDateToString(date), "", Boolean.FALSE, Boolean.FALSE, propriedades.getValorString("MENSAGEM_ERRO_DIARIO_NAO_ENCONTRADO"), null, null, "", propriedades.getValorString("MENSAGEM_DIARIO_INDISPONIVEL"), token);
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
	 * @param dataInicial
	 * @param dataFinal
	 * @param diasUteisList
	 * @return
	 */
	private Boolean getPaginasDiariosDOM(Fonte fonte, String pageDom, List<String> arquivoList,
			Date dataInicial, Date dataFinal, List<LocalDate> diasUteisList, String token) {

		String htmlCompleto = "";
		Boolean isFinalPaginacao = null;
		Boolean erroUrlFonte = Boolean.TRUE;
		Propriedades propriedades = Propriedades.getInstance();

		try {
			String regexForDate = null, regexForPDF = null;

			// A expressão regular das datas e PDFs estão diferentes para cada Fonte.
			if (propriedades.getValorLong("ID_FONTE_DIARIO_OFICIAL_TERESINA").equals(fonte.getId())) {
				regexForDate = "\\d{2}/\\d{2}/\\d{4}";
				regexForPDF = "[0-9A-Za-z|\\s|-]+[\\(\\d\\)]*+.(pdf)";
				erroUrlFonte = Boolean.FALSE;
			} else if (propriedades.getValorLong("ID_FONTE_DIARIO_OFICIAL_PARNAIBA").equals(fonte.getId())) {
				regexForDate = "\\d{2}[\\.\\-]{1,2}\\d{2}-\\d{4}";
				regexForPDF = "[0-9A-Za-z]+.(pdf)";
				erroUrlFonte = Boolean.FALSE;
			} else if (propriedades.getValorLong("ID_FONTE_DIARIO_OFICIAL_DOS_MUNICIPIOS").equals(fonte.getId())) {
				regexForDate = "\\d{2}\\s+((Janeiro)|(Fevereiro)|(Março)|(Abril)|(Maio)|(Junho)|(Julho)|(Agosto)|(Setembro)|(Outubro)|(Novembro)|(Dezembro))\\s+[d][e]\\s+\\d{4}";
				regexForPDF = "[D][M]\\s+[0-9A-Za-z]+.(pdf)";
				erroUrlFonte = Boolean.FALSE;
			}

			if (erroUrlFonte) {
				LOGGER.error("Erro: A Fonte " + fonte.getUrl() + " não foi Encontrada.");
			} else {
				isFinalPaginacao = lerPaginaDiarioDOM(fonte, pageDom, arquivoList, htmlCompleto,
						isFinalPaginacao, regexForDate, regexForPDF, dataInicial, dataFinal, diasUteisList, token);
			}

		} catch (MalformedURLException excecao) {
			LOGGER.error(excecao.getMessage());
		} catch (IOException excecao) {
			LOGGER.error(excecao.getMessage());
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
			LOGGER.error("Erro: HTML da página não foi encontrado.");
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		}

		return listHtmls;
	}

	/**
	 * @param urlString
	 * @param pageDom
	 * @param arquivoList
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
			String htmlCompleto, Boolean isFinalPaginacao, String regexForDate,
			String regexForPDF, Date dataInicial, Date dataFinal, List<LocalDate> diasUteisList, String token)
			throws MalformedURLException, IOException {

		String linhaHTML;
		Date date = null;
		String urlFonte = fonte.getUrl();
		Propriedades propriedades = Propriedades.getInstance();

		URL url = new URL(fonte.getUrl());
		if (propriedades.getValorLong("ID_FONTE_DIARIO_OFICIAL_PARNAIBA").equals(fonte.getId())) {
			urlFonte = propriedades.getValorString("URL_COLETA_DIARIO_OFICIAL_PARNAIBA") + pageDom;
		}else if (propriedades.getValorLong("ID_FONTE_DIARIO_OFICIAL_TERESINA").equals(fonte.getId())) {
			urlFonte = propriedades.getValorString("URL_COLETA_DIARIO_OFICIAL_TERESINA") + pageDom;
		}else if (propriedades.getValorLong("ID_FONTE_DIARIO_OFICIAL_DOS_MUNICIPIOS").equals(fonte.getId())) {
			urlFonte = propriedades.getValorString("URL_COLETA_DIARIO_OFICIAL_DOS_MUNICIPIOS") + pageDom;
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
						if (propriedades.getValorLong("ID_FONTE_DIARIO_OFICIAL_PARNAIBA").equals(fonte.getId())
								|| propriedades.getValorLong("ID_FONTE_DIARIO_OFICIAL_DOS_MUNICIPIOS").equals(fonte.getId())) {

							if (dataInicial.compareTo(date) <= 0 && dataFinal.compareTo(date) >= 0) {
								if (propriedades.getValorLong("ID_FONTE_DIARIO_OFICIAL_PARNAIBA").equals(fonte.getId())) {
									
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
									
									salvarPublicacao(fonte, propriedades.getValorString("URL_DOWNLOAD_DOM_PARNAIBA") + arquivoStr, convertDateToString(date),
											arquivoStr, Boolean.TRUE, Boolean.FALSE, "Sucesso", null, null, codigo, publicacaoName, token);
								} else {
									Calendar c = Calendar.getInstance();
									c.setTime(date);
									String mes = String.format("%02d", c.get(Calendar.MONTH) + 1);
									String ano = String.valueOf(c.get(Calendar.YEAR));
									salvarPublicacao(fonte, propriedades.getValorString("URL_DOWNLOAD_DIARIO_OFICIAL_DOS_MUNICIPIOS") + ano + mes + "/" + arquivoStr,
											convertDateToString(date), arquivoStr, Boolean.TRUE, Boolean.FALSE, "Sucesso", null, null, "", arquivoStr, token);
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
						} else if (propriedades.getValorLong("ID_FONTE_DIARIO_OFICIAL_TERESINA").equals(fonte.getId())) {
							
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
										Matcher matcherPositive = Pattern.compile("[0-9A-Za-z\\s|-]+.(pdf)")
												.matcher(linhaHTML);
										Matcher matcherNegative = Pattern.compile("--").matcher(linhaHTML);

										if (matcherNegative.find()) {
											break;
										}

										if (matcherPositive.find()) {
											String arquivoAnexoStr = matcherPositive.group();
											arquivoAnexo = new Arquivo(arquivoAnexoStr, Long.valueOf(10), "tipo",
													propriedades.getValorString("URL_DOWNLOAD_DOM_TERESINA") + arquivoAnexoStr, "conteudo".getBytes());
											publicacaoAnexo = new PublicacaoAnexo(null, arquivoAnexoStr,
													arquivoAnexo.getId(), true);
											break;
										}
									} while (true);
									
									salvarPublicacao(fonte, propriedades.getValorString("URL_DOWNLOAD_DOM_TERESINA") + arquivoStr, convertDateToString(date), arquivoStr, Boolean.TRUE, Boolean.valueOf(publicacaoAnexo!=null), "Sucesso", publicacaoAnexo, arquivoAnexo, codigo, publicacaoName, token);
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
		if (propriedades.getValorLong("ID_FONTE_DIARIO_OFICIAL_PARNAIBA").equals(fonte.getId())) {
			if (htmlCompleto.contains(propriedades.getValorString("PALAVRA_FINAL_DO_ARQUIVO_DOM_PARNAIBA"))) {
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
	 * @param dataPublicacao
	 * @param nomeArquivoPublicacao
	 * @param isSucesso
	 * @param isAnexo
	 * @param mensagemErro
	 * @param publicacaoAnexo
	 * @param codigo
	 * @param publicacaoName
	 */
	private void salvarPublicacao(Fonte fonte, String linkArquivoPublicacao, String dataPublicacao,
			String nomeArquivoPublicacao, Boolean isSucesso, Boolean isAnexo, String mensagemErro, PublicacaoAnexo publicacaoAnexo, Arquivo arquivoAnexo, String codigo, String publicacaoName, String token) {

		if (token != null) {
			publicacaoServiceClient = new PublicacaoServiceClient(token);
		}
		Propriedades propriedades = Propriedades.getInstance();
		Arquivo arquivo = new Arquivo(nomeArquivoPublicacao, Long.valueOf(10),"tipo", linkArquivoPublicacao, "conteudo".getBytes());
		Publicacao publicacao = new Publicacao(fonte, publicacaoName, dataPublicacao, codigo, arquivo.getId(), isSucesso, isAnexo,Long.valueOf(1));
		Publicacao publicacaoConsultada = consultarPublicacaoPorFonteDataNomeArquivo(publicacao);
		if (publicacaoConsultada == null) {
			Publicacao publicacaoIndisponivel = new Publicacao(fonte, propriedades.getValorString("MENSAGEM_DIARIO_INDISPONIVEL"), dataPublicacao, codigo, arquivo.getId(), isSucesso, isAnexo,Long.valueOf(1));
			Publicacao publicacaoConsultadaIndisponivel = consultarPublicacaoPorFonteDataNomeArquivo(publicacaoIndisponivel);
			if (publicacaoConsultadaIndisponivel != null) {
				publicacaoConsultada = publicacaoConsultadaIndisponivel;
			}
		}
		Publicacao publicacaoRetorno = publicacao;
		try {
			if (publicacaoConsultada == null) {
				publicacaoRetorno = publicacaoServiceClient.cadastrarPublicacao(publicacao, arquivo, false);
				//publicacaoRetorno = publicacao;
				if (publicacaoAnexo != null) {
					publicacaoAnexo.setPublicacao(publicacaoRetorno);
					publicacaoAnexo = publicacaoServiceClient.cadastrarPublicacaoAnexo(publicacaoAnexo, arquivoAnexo, false);
				}
			} else {
				if (!publicacaoConsultada.getSucesso()) {
					// se quantidade for igual a 3 ou mais - atualizar que não vai buscar mais
					// (status) e futuramente gerar notificacao
					publicacaoConsultada.setQuantidadeTentativas(publicacaoConsultada.getQuantidadeTentativas() + 1);
					publicacaoConsultada.setNome(publicacao.getNome());
					publicacaoConsultada.setFonte(publicacao.getFonte());
					publicacaoConsultada.setData(publicacao.getData());
					publicacaoConsultada.setCodigo(publicacao.getCodigo());
					publicacaoConsultada.setSucesso(publicacao.getSucesso());
					publicacaoConsultada.setPossuiAnexo(publicacao.getPossuiAnexo());
					publicacaoRetorno = publicacaoServiceClient.alterarPublicacao(publicacaoConsultada, arquivo, false);
					//publicacaoRetorno = publicacaoConsultada;
				}
			}
		} catch (Exception e) {
			LOGGER.error("Erro ao Criar/Atualizar Publicacao.");
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		}
		if (!isSucesso) {
			if (publicacaoRetorno.getQuantidadeTentativas() > 0
					&& (propriedades.getValorInt("QUANTIDADE_VEZES_NOTIFICAR") == 0
					|| (publicacaoRetorno.getQuantidadeTentativas() % propriedades.getValorLong("QUANTIDADE_VEZES_NOTIFICAR")) == 0)) {
				List<NotificacaoConfig> notificacaoConfigList = consultarNotificacaoConfigPorTipoAtivo(NotificacaoTipo.CAPTURA, Boolean.TRUE);
				PublicacaoN publicacaoNotif = new PublicacaoN(publicacaoRetorno.getId());
				String tituloNotificacao = propriedades.getValorString("EMAIL_SUBJECT") + publicacaoRetorno.getFonte().getNome() + propriedades.getValorString("EMAIL_SUBJECT_2") + publicacaoRetorno.getData();
				String conteudoNotificacao = propriedades.getValorString("EMAIL_CONTENT") + publicacaoRetorno.getFonte().getNome() + propriedades.getValorString("EMAIL_CONTENT_2") + publicacaoRetorno.getData();
				NotificacaoN notificacaoN = new NotificacaoN(NotificacaoTipo.CAPTURA, notificacaoConfigList.get(0).getUsuarios(), publicacaoNotif, conteudoNotificacao);
				cadastrarNotificacao(notificacaoN);
				notificacao.sendEmail(propriedades.getValorString("EMAIL_TO"), propriedades.getValorString("EMAIL_FROM"), tituloNotificacao, conteudoNotificacao);
			}
		}
	}
	
	/**
	 * Método resposável por chamar a API para consultar publicação por Fonte, Data e Nome.
	 * 
	 * @param publicacao
	 * @return publicacaoConsultada
	 */
	private Publicacao consultarPublicacaoPorFonteDataNomeArquivo(Publicacao publicacao) {
		Publicacao publicacaoConsultada = null;
		List<Publicacao> publicacaolist = publicacaoServiceClient.consultarPublicacaoPorFonteDataNome(
				publicacao.getFonte().getId(), asLocalDate(convertStringToDate(publicacao.getData())), publicacao.getNome());
		if (publicacaolist != null && !publicacaolist.isEmpty()) {
			publicacaoConsultada = publicacaolist.get(0);
		}
		return publicacaoConsultada;
	}
	
	/**
	 * Método resposável por chamar a API para consultar Notificação Config por Tipo, e Ativo.
	 * 
	 * @param tipo
	 * @param ativo
	 * @return notificacaoConfigList
	 */
	private List<NotificacaoConfig> consultarNotificacaoConfigPorTipoAtivo(NotificacaoTipo tipo, Boolean ativo) {
		List<NotificacaoConfig> notificacaoConfigList = publicacaoServiceClient.consultarNotificacaoConfigPorTipoAtivo(tipo, ativo);
		return notificacaoConfigList;
	}
	
	/**
	 * Método resposável por chamar a API para Cadastrar uma Notificação.
	 * 
	 * @param notif
	 * @return notificacao
	 */
	private Notificacao cadastrarNotificacao(NotificacaoN notif) {
		Notificacao notificacao = null;
		try {
			notificacao = publicacaoServiceClient.cadastrarNotificacao(notif);
		} catch (Exception e) {
			LOGGER.error("Erro na criação da notificacao");
			LOGGER.error(e.getMessage());
		}
		return notificacao;
	}

	/**
	 * Método responsável por buscar os diários oficiais na fonte do Estado do Piauí.
	 * 
	 * @param dataInicial
	 * @param dataFinal
	 */
	public void getDiariosEmDiarioOficialPI(Date dataInicial, Date dataFinal, String token) {

		Propriedades propriedades = Propriedades.getInstance();
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
				URL url = new URL(propriedades.getValorString("URL_COLETA_DIARIO_OFICIAL_PIAUI") + ano + mes + dia);
				BufferedReader fonteHTML = new BufferedReader(new InputStreamReader(url.openStream()));

				Fonte fonte = consultarFontePorIdFonte(propriedades.getValorLong("ID_FONTE_DIARIO_OFICIAL_PIAUI"), token);
				
				buscaPDF: while ((linhaHTML = fonteHTML.readLine()) != null) {
					regexForPDF = "[0-9]+[/]+DIARIO+[0-9]+[_]+[0-9A-Za-z]+[.][Pp][Dd][Ff]";
					Pattern pdfPattern = Pattern.compile(regexForPDF);

					matcher = pdfPattern.matcher(linhaHTML);
					while (matcher.find()) {
						if (!isNull(date)) {
							String localPublicacao[] = matcher.group().split("/");
							//salvarPublicacao(fonte, propriedades.getValorString("URL_DOWNLOAD_DIARIO_OFICIAL_PIAUI")  + ano + mes + "/"+ matcher.group(), convertDateToString(date), matcher.group(), Boolean.TRUE, Boolean.FALSE, "Sucesso", null, null, "", matcher.group());
							salvarPublicacao(fonte, propriedades.getValorString("URL_DOWNLOAD_DIARIO_OFICIAL_PIAUI") + matcher.group(), convertDateToString(date), localPublicacao[1], Boolean.TRUE, Boolean.FALSE, "Sucesso", null, null, "", localPublicacao[1], token);
							diarioEncontrado = Boolean.TRUE;
							// Ao encontrar o pdf sai do loop mais externo
							break buscaPDF;
						}
					}
				}
				if (!diarioEncontrado) {
					if (!isFeriado(date, fonte.getId())) {
						LOGGER.info(propriedades.getValorString("EMAIL_CONTENT") + fonte.getNome() + propriedades.getValorString("EMAIL_CONTENT_2") + convertDateToString(date) + ".");
						salvarPublicacao(fonte, "", convertDateToString(date), "", Boolean.FALSE, Boolean.FALSE, propriedades.getValorString("MENSAGEM_ERRO_DIARIO_NAO_ENCONTRADO"), null, null, "", propriedades.getValorString("MENSAGEM_DIARIO_INDISPONIVEL"), token);
					}
				}
				fonteHTML.close();

			} catch (MalformedURLException excecao) {
				LOGGER.error(excecao.getMessage());
			} catch (IOException excecao) {
				LOGGER.error(excecao.getMessage());
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
	 * Converte uma string no formato dd/MM/yyyy em data.
	 * 
	 * @param dataStr
	 * @return data
	 */
	public Date convertStringToDate(String dataStr) {
		Date data = null;
		try {
			SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy");
			Date dataParseada = formatoData.parse(dataStr);
			data = dataParseada;
		} catch (ParseException e) {
			LOGGER.error("Erro ao converter String no formato dd/MM/yyyy em Data.");
			LOGGER.error(e.getMessage());
		}
		return data;
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
			LOGGER.error("Erro ao converter String no formato dd/MM/yyyy HH:mm:ss em Data.");
			LOGGER.error(e.getMessage());
		}
		return data;
	}

}
