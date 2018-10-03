package br.gov.pi.tce.publicacoes.clients;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.ejb.Stateless;
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

import br.gov.pi.tce.publicacoes.modelo.Fonte;
import br.gov.pi.tce.publicacoes.modelo.Publicacao;

/**
 * @author Erick Guilherme Cavalcanti 
 *
 */
@Local
@Stateless(name="PublicacaoServiceClient")
public class PublicacaoServiceClient{
	
	private static final String RESPONSE_TYPE = "application/json;charset=UTF-8";
	private String URI_PUBLICACOES = "http://localhost:7788/publicacoes/";
	private String URI_FONTES = "http://localhost:7788/fontes/";

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
	
	public List<Fonte> consultarTodasFontes(){
		try {
			this.webTarget = this.client.target(URI_FONTES);
			Invocation.Builder invocationBuilder =  this.webTarget.request(RESPONSE_TYPE);
			Response response = invocationBuilder.get();
			List<Fonte> list = response.readEntity(new GenericType<List<Fonte>>() {});
			return list;
		}
		catch (Exception e) {
			throw e;
		}
	}
	
	public Publicacao cadastrarPublicacao(Publicacao publicacao) throws Exception{
		MultipartFormDataOutput dataOutput = new MultipartFormDataOutput();
		dataOutput.addFormData("partFile", "", MediaType.TEXT_PLAIN_TYPE.withCharset("utf-8")
				, "");
//		dataOutput.addFormData("partFile", realizarDownload(), MediaType.TEXT_PLAIN_TYPE.withCharset("utf-8")
//				, "teste.pdf");
		
		dataOutput.addFormData("nome", publicacao.getNome(), MediaType.TEXT_PLAIN_TYPE);
		dataOutput.addFormData("fonte", publicacao.getFonte().getId(), MediaType.TEXT_PLAIN_TYPE);
		dataOutput.addFormData("data", publicacao.getData(), MediaType.TEXT_PLAIN_TYPE);
		dataOutput.addFormData("codigo", publicacao.getCodigo(), MediaType.TEXT_PLAIN_TYPE);
		dataOutput.addFormData("sucesso", publicacao.getSucesso(), MediaType.TEXT_PLAIN_TYPE);
		dataOutput.addFormData("possuiAnexo", publicacao.getPossuiAnexo(), MediaType.TEXT_PLAIN_TYPE);
		dataOutput.addFormData("quantidadeTentativas", publicacao.getQuantidadeTentativas(), MediaType.TEXT_PLAIN_TYPE);
		dataOutput.addFormData("link", publicacao.getArquivo().getLink(), MediaType.TEXT_PLAIN_TYPE);
		
		GenericEntity<MultipartFormDataOutput> entity = new GenericEntity<MultipartFormDataOutput>(dataOutput) { };
		
		this.webTarget = this.client.target(URI_PUBLICACOES);
		Invocation.Builder invocationBuilder =  this.webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.post(Entity.entity(entity, MediaType.MULTIPART_FORM_DATA_TYPE));
		trataRetorno(response);
		return response.readEntity(Publicacao.class);
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
	
//	public TipoFonte cadastrarTipoFonte(TipoFonte tipoFonte) throws Exception{
//		this.webTarget = this.client.target(URI_TIPOS_FONTES);
//		Invocation.Builder invocationBuilder =  this.webTarget.request(RESPONSE_TYPE);
//		Response response = invocationBuilder.post(Entity.entity(tipoFonte, RESPONSE_TYPE));
//		trataRetorno(response);
//		return response.readEntity(TipoFonte.class);
//	}

	public Publicacao alterarPublicacao(Publicacao publicacao) throws Exception{
		this.webTarget = this.client.target(URI_PUBLICACOES).path(String.valueOf(publicacao.getId()));
		Invocation.Builder invocationBuilder =  this.webTarget.request(RESPONSE_TYPE);
		Response response = invocationBuilder.put(Entity.entity(publicacao, RESPONSE_TYPE));
		trataRetorno(response);
		return response.readEntity(Publicacao.class);
	}
	
//	public TipoFonte alterarTipoFonte(TipoFonte tipoFonte) throws Exception{
//		this.webTarget = this.client.target(URI_TIPOS_FONTES).path(String.valueOf(tipoFonte.getId()));
//		Invocation.Builder invocationBuilder =  this.webTarget.request(RESPONSE_TYPE);
//		Response response = invocationBuilder.put(Entity.entity(tipoFonte, RESPONSE_TYPE));
//		trataRetorno(response);
//		return response.readEntity(TipoFonte.class);
//	}

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

//	public String excluirFontePorCodigo(Long id){
//		this.webTarget = this.client.target(URI_FONTES).path(String.valueOf(id));
//		Invocation.Builder invocationBuilder =  this.webTarget.request(RESPONSE_TYPE);
//		Response response = invocationBuilder.delete();
//		if(response.getStatus() == Response.Status.NO_CONTENT.getStatusCode()) {
//			return null;
//		}	
//		return response.readEntity(String.class);
//	}
	
//	public String excluirTipoFontePorCodigo(Long id){
//		this.webTarget = this.client.target(URI_TIPOS_FONTES).path(String.valueOf(id));
//		Invocation.Builder invocationBuilder =  this.webTarget.request(RESPONSE_TYPE);
//		Response response = invocationBuilder.delete();
//		if(response.getStatus() == Response.Status.NO_CONTENT.getStatusCode()) {
//			return null;
//		}	
//		return response.readEntity(String.class);
//	}
	
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
	
//	public PublicacaoServiceClient(){
//		this.client = ClientBuilder.newClient();  
//	}
//	
//	public List<Publicacao> consultarTodos(){
//		try {
//			this.webTarget = this.client.target(URL_SERVICE).path(PATH_CONSULTA_TODOS);
//			Invocation.Builder invocationBuilder =  this.webTarget.request(RESPONSE_TYPE);
//			Response response = invocationBuilder.get();
//			List<Publicacao> list = response.readEntity(new GenericType<List<Publicacao>>() {});
//			return list;
//		}
//		catch (Exception e) {
//			throw e;
//		}
//	}
//	
//	public Publicacao cadastrarPublicacao(Publicacao publicacao){
//		this.webTarget = this.client.target(URL_SERVICE).path(PATH_CADASTRAR);
//		Invocation.Builder invocationBuilder =  this.webTarget.request(RESPONSE_TYPE);
//		Response response = invocationBuilder.post(Entity.entity(publicacao, RESPONSE_TYPE));
//		return response.readEntity(Publicacao.class);
//	}
//
//	public Publicacao alterarPublicacao(Publicacao publicacao){
//		this.webTarget = this.client.target(URL_SERVICE).path(PATH_ALTERAR);
//		Invocation.Builder invocationBuilder =  this.webTarget.request(RESPONSE_TYPE);
//		Response response = invocationBuilder.put(Entity.entity(publicacao, RESPONSE_TYPE));
//		return response.readEntity(Publicacao.class);
//
//	}
//
//	public Publicacao consultarPublicacaoPorCodigo(Long id){
//		this.webTarget = this.client.target(URL_SERVICE).path(PATH_GET_PUBLICACAO).path(String.valueOf(id));
//		Invocation.Builder invocationBuilder =  this.webTarget.request(RESPONSE_TYPE);
//		Response response = invocationBuilder.get();
//		return response.readEntity(Publicacao.class);
//	}

}
