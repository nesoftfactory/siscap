package br.gov.pi.tce.publicacoes.clients;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;

import br.gov.pi.tce.publicacoes.autenticacao.AutenticadorToken;
import br.gov.pi.tce.publicacoes.modelo.Arquivo;
import br.gov.pi.tce.publicacoes.modelo.Feriado;
import br.gov.pi.tce.publicacoes.modelo.Fonte;
import br.gov.pi.tce.publicacoes.modelo.Notificacao;
import br.gov.pi.tce.publicacoes.modelo.NotificacaoConfig;
import br.gov.pi.tce.publicacoes.modelo.NotificacaoN;
import br.gov.pi.tce.publicacoes.modelo.Publicacao;
import br.gov.pi.tce.publicacoes.modelo.PublicacaoAnexo;
import br.gov.pi.tce.publicacoes.modelo.enums.NotificacaoTipo;
import br.gov.pi.tce.publicacoes.util.Propriedades;

/**
 * 
 * @author Erick Guilherme Cavalcanti
 *
 */
@Local
@Stateless(name = "PublicacaoServiceClient")
public class PublicacaoServiceClient {

	private static final int BUFFER_SIZE = 6124;

	private static final Logger LOGGER = Logger.getLogger(PublicacaoServiceClient.class);

	private Client client;
	private WebTarget webTarget;

	public PublicacaoServiceClient() {
		this.client = ClientBuilder.newClient().register(new AutenticadorToken());
	}

	public PublicacaoServiceClient(String token) {
		this.client = ClientBuilder.newClient().register(new AutenticadorToken(token));
	}

	public List<Publicacao> consultarTodasPublicacoes(Boolean sucesso) {
		try {
			Propriedades propriedades = Propriedades.getInstance();
			if (sucesso == null) {
				this.webTarget = this.client.target(
						propriedades.getValorString("URI_API") + propriedades.getValorString("URI_PUBLICACOES"));
			} else {
				this.webTarget = this.client
						.target(propriedades.getValorString("URI_API") + propriedades.getValorString("URI_PUBLICACOES"))
						.queryParam("sucesso", sucesso);
			}
			Invocation.Builder invocationBuilder = this.webTarget.request(propriedades.getValorString("RESPONSE_TYPE"));
			Response response = invocationBuilder.get();
			List<Publicacao> list = response.readEntity(new GenericType<List<Publicacao>>() {
			});
			return list;
		} catch (Exception e) {
			LOGGER.error("Erro ao consultar todas as publicacoes");
			throw e;
		}
	}

	public void cadastrarPublicacaoPorUpload(Publicacao publicacao, Arquivo arquivo, PublicacaoAnexo publicacaoAnexo,
			Arquivo arquivoAnexo) throws Exception {

		List<Publicacao> publicacoes = consultarTodasPublicacoes(null);

		for (Publicacao publicacaoElement : publicacoes) {
			if (publicacaoElement.getNome().equals(publicacao.getNome())) {
				throw new Exception("Este nome já existe em outra publicacão. Por favor renomeie esta publicação.");
			}

			if (publicacaoElement.getFonte().equals(publicacao.getFonte())
					&& publicacaoElement.getData().equals(publicacao.getData())) {
				throw new Exception(
						"Há um cadastro de uma publicação desta fonte para esta data. Por favor consulte as publicações já existentes.");
			}
		}

		publicacao.setSucesso(true);
		publicacao.setQuantidadeTentativas((long) 1);
		publicacao = cadastrarPublicacao(publicacao, arquivo, true);
		publicacaoAnexo.setPublicacao(publicacao);
		publicacaoAnexo.setSucesso(true);
		if (publicacao.getPossuiAnexo()) {
			cadastrarPublicacaoAnexo(publicacaoAnexo, arquivoAnexo, true);
		}
	}

	public Publicacao cadastrarPublicacao(Publicacao publicacao, Arquivo arquivo, boolean isUploadManual)
			throws Exception {
		MultipartFormDataOutput dataOutput = new MultipartFormDataOutput();
		Propriedades propriedades = Propriedades.getInstance();
		if (arquivo.getLink() == null || arquivo.getLink().equals("")) {
			dataOutput.addFormData("partFile", "",
					MediaType.TEXT_PLAIN_TYPE.withCharset(propriedades.getValorString("ENCODE_PADRAO")), "");
		} else {
			if (isUploadManual) {
				dataOutput.addFormData("partFile", arquivo.getInputStream(),
						MediaType.TEXT_PLAIN_TYPE.withCharset(propriedades.getValorString("ENCODE_PADRAO")),
						arquivo.getNome());
			} else {
				dataOutput.addFormData("partFile", realizarDownload(arquivo.getLink()),
						MediaType.TEXT_PLAIN_TYPE.withCharset(propriedades.getValorString("ENCODE_PADRAO")),
						arquivo.getNome());
			}
		}

		dataOutput.addFormData("nome", publicacao.getNome(), MediaType.TEXT_PLAIN_TYPE);
		dataOutput.addFormData("fonte", publicacao.getFonte().getId(), MediaType.TEXT_PLAIN_TYPE);
		dataOutput.addFormData("data", asLocalDate(convertStringToDate(publicacao.getData())),
				MediaType.TEXT_PLAIN_TYPE);
		dataOutput.addFormData("codigo", publicacao.getCodigo(), MediaType.TEXT_PLAIN_TYPE);
		dataOutput.addFormData("sucesso", publicacao.getSucesso(), MediaType.TEXT_PLAIN_TYPE);
		dataOutput.addFormData("possuiAnexo", publicacao.getPossuiAnexo(), MediaType.TEXT_PLAIN_TYPE);
		dataOutput.addFormData("quantidadeTentativas", publicacao.getQuantidadeTentativas(), MediaType.TEXT_PLAIN_TYPE);
		dataOutput.addFormData("link", encodeString(arquivo.getLink()), MediaType.TEXT_PLAIN_TYPE);

		GenericEntity<MultipartFormDataOutput> entity = new GenericEntity<MultipartFormDataOutput>(dataOutput) {
		};

		this.webTarget = this.client
				.target(propriedades.getValorString("URI_API") + propriedades.getValorString("URI_PUBLICACOES"));
		Invocation.Builder invocationBuilder = this.webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.post(Entity.entity(entity, MediaType.MULTIPART_FORM_DATA_TYPE));
		trataRetorno(response);
		return response.readEntity(Publicacao.class);
	}

	public PublicacaoAnexo cadastrarPublicacaoAnexo(PublicacaoAnexo publicacaoAnexo, Arquivo arquivo,
			boolean isUploadManual) throws Exception {
		MultipartFormDataOutput dataOutput = new MultipartFormDataOutput();
		Propriedades propriedades = Propriedades.getInstance();
		if (arquivo.getLink() == null || arquivo.getLink().equals("")) {
			dataOutput.addFormData("partFile", "",
					MediaType.TEXT_PLAIN_TYPE.withCharset(propriedades.getValorString("ENCODE_PADRAO")), "");
		} else {
			if (isUploadManual) {
				dataOutput.addFormData("partFile", arquivo.getInputStream(),
						MediaType.TEXT_PLAIN_TYPE.withCharset(propriedades.getValorString("ENCODE_PADRAO")),
						arquivo.getNome());
			} else {
				dataOutput.addFormData("partFile", realizarDownload(arquivo.getLink()),
						MediaType.TEXT_PLAIN_TYPE.withCharset(propriedades.getValorString("ENCODE_PADRAO")),
						arquivo.getNome());
			}
		}

		dataOutput.addFormData("nome", publicacaoAnexo.getNome(), MediaType.TEXT_PLAIN_TYPE);
		dataOutput.addFormData("publicacao", publicacaoAnexo.getPublicacao().getId(), MediaType.TEXT_PLAIN_TYPE);
		dataOutput.addFormData("sucesso", publicacaoAnexo.isSucesso(), MediaType.TEXT_PLAIN_TYPE);
		dataOutput.addFormData("link", encodeString(arquivo.getLink()), MediaType.TEXT_PLAIN_TYPE);

		GenericEntity<MultipartFormDataOutput> entity = new GenericEntity<MultipartFormDataOutput>(dataOutput) {
		};

		this.webTarget = this.client
				.target(propriedades.getValorString("URI_API") + propriedades.getValorString("URI_PUBLICACOES_ANEXOS"));
		Invocation.Builder invocationBuilder = this.webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.post(Entity.entity(entity, MediaType.MULTIPART_FORM_DATA_TYPE));
		trataRetorno(response);
		return response.readEntity(PublicacaoAnexo.class);
	}

	public String encodeString(String palavra) {
		char one;
		StringBuffer n = new StringBuffer(palavra.length());
		for (int i = 0; i < palavra.length(); i++) {
			one = palavra.charAt(i);
			switch (one) {
			case ' ':
				n.append('%');
				n.append('2');
				n.append('0');
				break;
			default:
				n.append(one);
			}
		}
		return n.toString();
	}

	public void armazenarArquivo(Arquivo arquivo) {
		try {

			File result = new File(arquivo.getLink());
			FileOutputStream fileOutputStream = new FileOutputStream(result);
			byte[] buffer = new byte[BUFFER_SIZE];
			int bulk;

			while (true) {
				bulk = arquivo.getInputStream().read(buffer);
				if (bulk < 0) {
					break;
				}

				fileOutputStream.write(buffer, 0, bulk);
				fileOutputStream.flush();
			}

			fileOutputStream.close();

		} catch (IOException e) {
			e.printStackTrace();
			FacesMessage error = new FacesMessage(FacesMessage.SEVERITY_ERROR, "The files were not uploaded!", "");
			FacesContext.getCurrentInstance().addMessage(null, error);
		}
	}

	public FileInputStream realizarDownload(String linkArquivo) {
		URL url;
		FileInputStream fileInputStream = null;
		try {
			url = new URL(encodeString(linkArquivo));
			Propriedades propriedades = Propriedades.getInstance();
			File file = new File(System.getProperty("java.io.tmpdir") + File.separator
					+ propriedades.getValorString("DOWNLOAD_TEMPORARIO"));
			FileUtils.copyURLToFile(url, file);
			fileInputStream = new FileInputStream(file);

		} catch (MalformedURLException e) {
			LOGGER.error(e.getMessage());
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		}
		return fileInputStream;
	}

	public Publicacao alterarPublicacao(Publicacao publicacao, Arquivo arquivo, boolean isUploadManual)
			throws Exception {
		MultipartFormDataOutput dataOutput = new MultipartFormDataOutput();
		Propriedades propriedades = Propriedades.getInstance();
		if (arquivo.getLink() == null || arquivo.getLink().equals("")) {
			dataOutput.addFormData("partFile", "",
					MediaType.TEXT_PLAIN_TYPE.withCharset(propriedades.getValorString("ENCODE_PADRAO")), "");
		} else {
			if (isUploadManual) {
				dataOutput.addFormData("partFile", arquivo.getInputStream(),
						MediaType.TEXT_PLAIN_TYPE.withCharset(propriedades.getValorString("ENCODE_PADRAO")),
						arquivo.getNome());
			} else {
				dataOutput.addFormData("partFile", realizarDownload(arquivo.getLink()),
						MediaType.TEXT_PLAIN_TYPE.withCharset(propriedades.getValorString("ENCODE_PADRAO")),
						arquivo.getNome());
			}
		}

		dataOutput.addFormData("nome", publicacao.getNome(), MediaType.TEXT_PLAIN_TYPE);
		dataOutput.addFormData("fonte", publicacao.getFonte().getId(), MediaType.TEXT_PLAIN_TYPE);
		dataOutput.addFormData("data", asLocalDate(convertStringToDate(publicacao.getData())),
				MediaType.TEXT_PLAIN_TYPE);
		dataOutput.addFormData("codigo", publicacao.getCodigo(), MediaType.TEXT_PLAIN_TYPE);
		dataOutput.addFormData("sucesso", publicacao.getSucesso(), MediaType.TEXT_PLAIN_TYPE);
		dataOutput.addFormData("possuiAnexo", publicacao.getPossuiAnexo(), MediaType.TEXT_PLAIN_TYPE);
		dataOutput.addFormData("quantidadeTentativas", publicacao.getQuantidadeTentativas(), MediaType.TEXT_PLAIN_TYPE);
		dataOutput.addFormData("link", encodeString(arquivo.getLink()), MediaType.TEXT_PLAIN_TYPE);

		GenericEntity<MultipartFormDataOutput> entity = new GenericEntity<MultipartFormDataOutput>(dataOutput) {
		};

		this.webTarget = this.client
				.target(propriedades.getValorString("URI_API") + propriedades.getValorString("URI_PUBLICACOES"))
				.path(String.valueOf(publicacao.getId()));
		Invocation.Builder invocationBuilder = this.webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.put(Entity.entity(entity, MediaType.MULTIPART_FORM_DATA_TYPE));
		trataRetorno(response);
		return response.readEntity(Publicacao.class);
	}

	public Publicacao consultarPublicacaoPorCodigo(Long id) {
		Propriedades propriedades = Propriedades.getInstance();
		this.webTarget = this.client
				.target(propriedades.getValorString("URI_API") + propriedades.getValorString("URI_PUBLICACOES"))
				.path(String.valueOf(id));
		Invocation.Builder invocationBuilder = this.webTarget.request(propriedades.getValorString("RESPONSE_TYPE"));
		Response response = invocationBuilder.get();
		if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
			return null;
		} else {
			return response.readEntity(Publicacao.class);
		}
	}

	public Fonte consultarFontePorCodigo(Long id) {
		Propriedades propriedades = Propriedades.getInstance();
		this.webTarget = this.client
				.target(propriedades.getValorString("URI_API") + propriedades.getValorString("URI_FONTES"))
				.path(String.valueOf(id));
		Invocation.Builder invocationBuilder = this.webTarget.request(propriedades.getValorString("RESPONSE_TYPE"));
		Response response = invocationBuilder.get();
		if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
			return null;
		} else {
			Fonte tf = response.readEntity(Fonte.class);
			return tf;
		}
	}

	public List<Feriado> consultarFeriadoPorFontePeriodo(Long idFonte, LocalDate periodoDe, LocalDate periodoAte) {
		Propriedades propriedades = Propriedades.getInstance();
		this.webTarget = this.client
				.target(propriedades.getValorString("URI_API") + propriedades.getValorString("URI_FERIADOS"))
				.queryParam("idFonte", idFonte).queryParam("periodoDe", periodoDe).queryParam("periodoAte", periodoAte);
		Invocation.Builder invocationBuilder = this.webTarget.request(propriedades.getValorString("RESPONSE_TYPE"));
		Response response = invocationBuilder.get();
		if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
			return null;
		} else {
			List<Feriado> tf = response.readEntity(new GenericType<List<Feriado>>() {
			});
			return tf;
		}
	}

	public List<Publicacao> consultarPublicacaoPorFonteDataNome(Long idFonte, LocalDate data, String nome) {
		Propriedades propriedades = Propriedades.getInstance();
		LOGGER.info("INICIANDO RESPONSE PUBLICACAO");

		this.webTarget = this.client
				.target(propriedades.getValorString("URI_API") + propriedades.getValorString("URI_PUBLICACOES"))
				.queryParam("idFonte", idFonte).queryParam("data", data).queryParam("nome", nome);
		Invocation.Builder invocationBuilder = this.webTarget.request(propriedades.getValorString("RESPONSE_TYPE"));
		Response response = invocationBuilder.get();

		if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
			return null;
		} else {
			List<Publicacao> tf = response.readEntity(new GenericType<List<Publicacao>>() {
			});
			LOGGER.info("FINALIZANDO O RESPONSE PUBLICACAO");

			return tf;
		}
	}

	public List<NotificacaoConfig> consultarNotificacaoConfigPorTipoAtivo(NotificacaoTipo tipo, Boolean ativo) {
		Propriedades propriedades = Propriedades.getInstance();

		this.webTarget = this.client
				.target(propriedades.getValorString("URI_API") + propriedades.getValorString("URI_NOTIFICACOES_CONFIG"))
				.queryParam("ativo", ativo).queryParam("tipo", tipo.toString());
		Invocation.Builder invocationBuilder = this.webTarget.request(propriedades.getValorString("RESPONSE_TYPE"));
		Response response = invocationBuilder.get();
		if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
			return null;
		} else {
			List<NotificacaoConfig> tf = response.readEntity(new GenericType<List<NotificacaoConfig>>() {
			});

			return tf;
		}

	}

	public Notificacao cadastrarNotificacao(NotificacaoN notificacao) throws Exception {
		Propriedades propriedades = Propriedades.getInstance();
		this.webTarget = this.client
				.target(propriedades.getValorString("URI_API") + propriedades.getValorString("URI_NOTIFICACOES"));
		Invocation.Builder invocationBuilder = this.webTarget.request(propriedades.getValorString("RESPONSE_TYPE"));
		Response response = invocationBuilder
				.post(Entity.entity(notificacao, propriedades.getValorString("RESPONSE_TYPE")));
		trataRetorno(response);
		return response.readEntity(Notificacao.class);
	}

	public List<Publicacao> consultarPublicacaoPorFiltro(Long idFonte, String nome, String dataInicio, String dataFim,
			Boolean sucesso, String data) throws Exception {
		LocalDate dtInicio = null;
		LocalDate dtFim = null;
		LocalDate dt = null;
		if (data == null) {
			if (dataInicio == null || dataFim == null) {
				//throw new Exception("Data Inicio e Data Fim são obrigatórios");
			}else {
				try {
					dtInicio = LocalDate.parse(dataInicio, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
					dtFim = LocalDate.parse(dataFim, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
				} catch (Exception e) {
					throw new Exception("Data Inválida");
				}
			}
		} else {
			try {
				dt = LocalDate.parse(data, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			} catch (Exception e) {
				throw new Exception("Data Inválida");
			}
		}

		Propriedades propriedades = Propriedades.getInstance();
		this.webTarget = this.client
				.target(propriedades.getValorString("URI_API") + propriedades.getValorString("URI_PUBLICACOES"))
				.queryParam("idFonte", idFonte).queryParam("dataInicio", dtInicio).queryParam("dataFim", dtFim)
				.queryParam("nome", nome).queryParam("sucesso", sucesso).queryParam("data", dt);
		Invocation.Builder invocationBuilder = this.webTarget.request(propriedades.getValorString("RESPONSE_TYPE"));
		Response response = invocationBuilder.get();
		trataRetorno(response);
		if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
			return null;
		} else {
			List<Publicacao> tf = response.readEntity(new GenericType<List<Publicacao>>() {
			});
			return tf;
		}
	}

	private void trataRetorno(Response response) throws Exception {
		if (response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
			List erros = response.readEntity(List.class);
			if (erros != null && !erros.isEmpty()) {
				Map p;
				String msg = (String) ((Map) erros.get(0)).get("mensagemUsuario");
				throw new Exception(msg);
			} else {
				LOGGER.error("Erro interno.");
				throw new Exception("Erro interno.");
			}
		}
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
	 * Converte um Date em LocalDate.
	 * 
	 * @param date
	 * @return
	 */
	public LocalDate asLocalDate(Date date) {
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public void alterarPublicacaoPorUpload(Publicacao publicacaoExistente, Publicacao publicacao, Arquivo arquivo,
			PublicacaoAnexo publicacaoAnexo, Arquivo arquivoAnexo) throws Exception {
		List<Publicacao> publicacoes = consultarTodasPublicacoes(null);

		for (Publicacao publicacaoElement : publicacoes) {
			if (publicacaoElement.getNome().equals(publicacao.getNome())) {
				throw new Exception("Este nome já existe em outra publicacão. Por favor renomeie esta publicação.");
			}

			if (publicacaoElement.getFonte().equals(publicacao.getFonte())
					&& publicacaoElement.getData().equals(publicacao.getData())) {
				throw new Exception(
						"Há um cadastro de uma publicação desta fonte para esta data. Por favor consulte as publicações já existentes.");
			}
		}

		publicacao.setSucesso(true);
		publicacao.setQuantidadeTentativas(publicacaoExistente.getQuantidadeTentativas() + 1);

		publicacaoExistente.setQuantidadeTentativas(publicacaoExistente.getQuantidadeTentativas() + 1);
		publicacaoExistente.setNome(publicacao.getNome());
		publicacaoExistente.setFonte(publicacao.getFonte());
		publicacaoExistente.setData(publicacao.getData());
		publicacaoExistente.setCodigo(publicacao.getCodigo());
		publicacaoExistente.setSucesso(publicacao.getSucesso());
		publicacaoExistente.setPossuiAnexo(publicacao.getPossuiAnexo());

		publicacaoExistente = alterarPublicacao(publicacaoExistente, arquivo, true);

		publicacaoAnexo.setPublicacao(publicacao);
		publicacaoAnexo.setSucesso(true);
		if (publicacao.getPossuiAnexo()) {
			publicacaoAnexo.setPublicacao(publicacaoExistente);
			cadastrarPublicacaoAnexo(publicacaoAnexo, arquivoAnexo, true);
		}

	}

}
