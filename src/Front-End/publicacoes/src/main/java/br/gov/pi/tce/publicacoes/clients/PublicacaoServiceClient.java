package br.gov.pi.tce.publicacoes.clients;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.validation.ValidationException;
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
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;

import br.gov.pi.tce.publicacoes.modelo.Arquivo;
import br.gov.pi.tce.publicacoes.modelo.Feriado;
import br.gov.pi.tce.publicacoes.modelo.Fonte;
import br.gov.pi.tce.publicacoes.modelo.Publicacao;
import br.gov.pi.tce.publicacoes.modelo.PublicacaoAnexo;

/**
 * @author Erick Guilherme Cavalcanti 
 *
 */
@Local
@Stateless(name="PublicacaoServiceClient")
public class PublicacaoServiceClient{
	
	private static final String RESPONSE_TYPE = "application/json;charset=UTF-8";
	private String URI_PUBLICACOES = "http://localhost:7788/publicacoes/";
	private String URI_PUBLICACOES_ANEXOS = "http://localhost:7788/publicacoes_anexos/";
	private String URI_FONTES = "http://localhost:7788/fontes/";
	private String URI_FERIADOS = "http://localhost:7788/feriados/";
	private static final int BUFFER_SIZE = 6124;

	private Client client;
	private WebTarget webTarget;
	
	public PublicacaoServiceClient(){
		this.client = ClientBuilder.newClient();  
	}
	
	public List<Publicacao> consultarTodasPublicacoes(){
		try {
			this.webTarget = this.client.target(URI_PUBLICACOES);
			Invocation.Builder invocationBuilder =  this.webTarget.request(RESPONSE_TYPE);
			Response response = invocationBuilder.get();
			List<Publicacao> list = response.readEntity(new GenericType<List<Publicacao>>() {});
			return list;
		}
		catch (Exception e) {
			throw e;
		}
	}
	
	public void cadastrarPublicacaoPorUpload (Publicacao publicacao, Arquivo arquivo, PublicacaoAnexo publicacaoAnexo, Arquivo arquivoAnexo) throws Exception {
		
		List<Publicacao> publicacoes = consultarTodasPublicacoes();
		
		for (Publicacao publicacaoElement : publicacoes) {
	        if (publicacaoElement.getNome().equals(publicacao.getNome())) {
	        	throw new ValidationException("Este nome já existe em outra publicacão. Por favor renomeie esta publicação.");
	        }
	        
	        if (publicacaoElement.getCodigo().equals(publicacao.getCodigo())) {
	        	throw new ValidationException("Este código já existe em outra publicacão. Por favor use outro código para esta publicação.");
	        }
	        
	        if (publicacaoElement.getFonte().equals(publicacao.getFonte()) && publicacaoElement.getData().equals(publicacao.getData())) {
	        	throw new ValidationException("Há um cadastro de uma publicação desta fonte para esta data. Por favor consulte as publicações já existentes.");
	        }
	    }
		
		publicacao.setSucesso(true);
		publicacao.setQuantidadeTentativas((long) 1);
		publicacao = cadastrarPublicacao(publicacao,  arquivo);
		publicacaoAnexo.setPublicacao(publicacao);
		publicacaoAnexo.setSucesso(true);
		cadastrarPublicacaoAnexo(publicacaoAnexo, arquivoAnexo);
	}
	
	public Publicacao cadastrarPublicacao(Publicacao publicacao, Arquivo arquivo) throws Exception{
		MultipartFormDataOutput dataOutput = new MultipartFormDataOutput();
		if (arquivo.getLink() == null || arquivo.getLink().equals("")) {
			dataOutput.addFormData("partFile", "", MediaType.TEXT_PLAIN_TYPE.withCharset("utf-8"), "");
		} else {
			dataOutput.addFormData("partFile", realizarDownload(), MediaType.TEXT_PLAIN_TYPE.withCharset("utf-8"),
					"teste.pdf");
		}
		
		dataOutput.addFormData("nome", publicacao.getNome(), MediaType.TEXT_PLAIN_TYPE);
		dataOutput.addFormData("fonte", publicacao.getFonte().getId(), MediaType.TEXT_PLAIN_TYPE);
		dataOutput.addFormData("data", publicacao.getData(), MediaType.TEXT_PLAIN_TYPE);
		dataOutput.addFormData("codigo", publicacao.getCodigo(), MediaType.TEXT_PLAIN_TYPE);
		dataOutput.addFormData("sucesso", publicacao.getSucesso(), MediaType.TEXT_PLAIN_TYPE);
		dataOutput.addFormData("possuiAnexo", publicacao.getPossuiAnexo(), MediaType.TEXT_PLAIN_TYPE);
		dataOutput.addFormData("quantidadeTentativas", publicacao.getQuantidadeTentativas(), MediaType.TEXT_PLAIN_TYPE);
		dataOutput.addFormData("link", arquivo.getLink(), MediaType.TEXT_PLAIN_TYPE);
		
		GenericEntity<MultipartFormDataOutput> entity = new GenericEntity<MultipartFormDataOutput>(dataOutput) { };
		
		this.webTarget = this.client.target(URI_PUBLICACOES);
		Invocation.Builder invocationBuilder =  this.webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.post(Entity.entity(entity, MediaType.MULTIPART_FORM_DATA_TYPE));
		trataRetorno(response);
		return response.readEntity(Publicacao.class);
	}
	
	public PublicacaoAnexo cadastrarPublicacaoAnexo(PublicacaoAnexo publicacaoAnexo, Arquivo arquivo) throws Exception{
		MultipartFormDataOutput dataOutput = new MultipartFormDataOutput();
		if (arquivo.getLink() == null || arquivo.getLink().equals("")) {
			dataOutput.addFormData("partFile", "", MediaType.TEXT_PLAIN_TYPE.withCharset("utf-8"), "");
		} else {
			dataOutput.addFormData("partFile", realizarDownload(), MediaType.TEXT_PLAIN_TYPE.withCharset("utf-8"),
					"teste.pdf");
		}
		
		dataOutput.addFormData("nome", publicacaoAnexo.getNome(), MediaType.TEXT_PLAIN_TYPE);
		dataOutput.addFormData("publicacao", publicacaoAnexo.getPublicacao().getId(), MediaType.TEXT_PLAIN_TYPE);
		dataOutput.addFormData("sucesso", publicacaoAnexo.isSucesso(), MediaType.TEXT_PLAIN_TYPE);
		dataOutput.addFormData("link", arquivo.getLink(), MediaType.TEXT_PLAIN_TYPE);
		
		GenericEntity<MultipartFormDataOutput> entity = new GenericEntity<MultipartFormDataOutput>(dataOutput) { };
		
		this.webTarget = this.client.target(URI_PUBLICACOES_ANEXOS);
		Invocation.Builder invocationBuilder =  this.webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.post(Entity.entity(entity, MediaType.MULTIPART_FORM_DATA_TYPE));
		trataRetorno(response);
		return response.readEntity(PublicacaoAnexo.class);
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
	
	public FileInputStream realizarDownload() {
		URL url;
		FileInputStream fileInputStream = null;
		try {
			url = new URL("http://www.casadodivinomestre.com.br/teste.pdf");
			File file = new File("C:\\Temp\\arquivo-baixado3.pdf");
			FileUtils.copyURLToFile(url, file);
			fileInputStream = new FileInputStream(file);
			//fileInputStream.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileInputStream;
	}

//	public Publicacao alterarPublicacao(Publicacao publicacao) throws Exception{
//		this.webTarget = this.client.target(URI_PUBLICACOES).path(String.valueOf(publicacao.getId()));
//		Invocation.Builder invocationBuilder =  this.webTarget.request(RESPONSE_TYPE);
//		Response response = invocationBuilder.put(Entity.entity(publicacao, RESPONSE_TYPE));
//		trataRetorno(response);
//		return response.readEntity(Publicacao.class);
//	}
	public Publicacao alterarPublicacao(Publicacao publicacao, Arquivo arquivo) throws Exception{
		MultipartFormDataOutput dataOutput = new MultipartFormDataOutput();
		if (arquivo.getLink() == null || arquivo.getLink().equals("")) {
			dataOutput.addFormData("partFile", "", MediaType.TEXT_PLAIN_TYPE.withCharset("utf-8"), "");
		} else {
			dataOutput.addFormData("partFile", realizarDownload(), MediaType.TEXT_PLAIN_TYPE.withCharset("utf-8"),
					"teste.pdf");
		}
		
		dataOutput.addFormData("nome", publicacao.getNome(), MediaType.TEXT_PLAIN_TYPE);
		dataOutput.addFormData("fonte", publicacao.getFonte().getId(), MediaType.TEXT_PLAIN_TYPE);
		dataOutput.addFormData("data", publicacao.getData(), MediaType.TEXT_PLAIN_TYPE);
		dataOutput.addFormData("codigo", publicacao.getCodigo(), MediaType.TEXT_PLAIN_TYPE);
		dataOutput.addFormData("sucesso", publicacao.getSucesso(), MediaType.TEXT_PLAIN_TYPE);
		dataOutput.addFormData("possuiAnexo", publicacao.getPossuiAnexo(), MediaType.TEXT_PLAIN_TYPE);
		dataOutput.addFormData("quantidadeTentativas", publicacao.getQuantidadeTentativas(), MediaType.TEXT_PLAIN_TYPE);
		dataOutput.addFormData("link", arquivo.getLink(), MediaType.TEXT_PLAIN_TYPE);
		
		GenericEntity<MultipartFormDataOutput> entity = new GenericEntity<MultipartFormDataOutput>(dataOutput) { };
		
		this.webTarget = this.client.target(URI_PUBLICACOES).path(String.valueOf(publicacao.getId()));
		Invocation.Builder invocationBuilder =  this.webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.put(Entity.entity(entity, MediaType.MULTIPART_FORM_DATA_TYPE));
		trataRetorno(response);
		return response.readEntity(Publicacao.class);
	}

	public Publicacao consultarPublicacaoPorCodigo(Long id){
		this.webTarget = this.client.target(URI_PUBLICACOES).path(String.valueOf(id));
		Invocation.Builder invocationBuilder =  this.webTarget.request(RESPONSE_TYPE);
		Response response = invocationBuilder.get();
		if(response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
			return null;
		}	
		else {
			return  response.readEntity(Publicacao.class);
		}
	}
	
	public Fonte consultarFontePorCodigo(Long id){
		this.webTarget = this.client.target(URI_FONTES).path(String.valueOf(id));
		Invocation.Builder invocationBuilder =  this.webTarget.request(RESPONSE_TYPE);
		Response response = invocationBuilder.get();
		if(response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
			return null;
		}	
		else {
			Fonte tf = response.readEntity(Fonte.class);
			return  tf;
		}
	}
	
	public List<Feriado> consultarFeriadoPorFontePeriodo(Long idFonte, LocalDate periodoDe, LocalDate periodoAte){
		this.webTarget = this.client.target(URI_FERIADOS).queryParam("idFonte", idFonte).queryParam("periodoDe", periodoDe).queryParam("periodoAte", periodoAte);
		Invocation.Builder invocationBuilder =  this.webTarget.request(RESPONSE_TYPE);
		Response response = invocationBuilder.get();
		if(response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
			return null;
		}	
		else {
			List<Feriado> tf = response.readEntity(new GenericType<List<Feriado>>() {});
			return  tf;
		}
	}
	
	public List<Publicacao> consultarPublicacaoPorFonteDataNome(Long idFonte, LocalDate data, String nome){
		this.webTarget = this.client.target(URI_PUBLICACOES).queryParam("fonte", idFonte).queryParam("data", data).queryParam("nome", nome);
		Invocation.Builder invocationBuilder =  this.webTarget.request(RESPONSE_TYPE);
		Response response = invocationBuilder.get();
		if(response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
			return null;
		}	
		else {
			List<Publicacao> tf = response.readEntity(new GenericType<List<Publicacao>>() {});
			return  tf;
		}
	}
	
	private void trataRetorno(Response response) throws Exception {
		if(response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
			List erros = response.readEntity(List.class);
			if(erros != null && !erros.isEmpty()) {
				Map p;
				String msg = (String)((Map)erros.get(0)).get("mensagemUsuario");
				throw new Exception(msg);
			}
			else {
				throw new Exception("Erro interno.");
			}
		}
	}

}
