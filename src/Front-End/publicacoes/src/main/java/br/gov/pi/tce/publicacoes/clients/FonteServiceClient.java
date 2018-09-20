package br.gov.pi.tce.publicacoes.clients;

import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import br.gov.pi.tce.publicacoes.modelo.Fonte;
import br.gov.pi.tce.publicacoes.modelo.TipoFonte;

@Local
@Stateless(name="FonteServiceClient")
public class FonteServiceClient {
	
	
	private static final String RESPONSE_TYPE = "application/json;charset=UTF-8";
	private String URI_FONTES = "http://localhost:7788/fontes/";
	private String URI_TIPOS_FONTES = "http://localhost:7788/tiposfonte/";

	private Client client;
	private WebTarget webTarget;
	
	public FonteServiceClient(){
		this.client = ClientBuilder.newClient();  
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
	
	public List<TipoFonte> consultarTodasTipoFontes(){
		try {
			this.webTarget = this.client.target(URI_TIPOS_FONTES);
			Invocation.Builder invocationBuilder =  this.webTarget.request(RESPONSE_TYPE);
			Response response = invocationBuilder.get();
			List<TipoFonte> list = response.readEntity(new GenericType<List<TipoFonte>>() {});
			return list;
		}
		catch (Exception e) {
			throw e;
		}
	}
	
	public Fonte cadastrarFonte(Fonte fonte) throws Exception{
		this.webTarget = this.client.target(URI_FONTES);
		Invocation.Builder invocationBuilder =  this.webTarget.request(RESPONSE_TYPE);
		Response response = invocationBuilder.post(Entity.entity(fonte, RESPONSE_TYPE));
		trataRetorno(response);
		return response.readEntity(Fonte.class);
	}
	
	public TipoFonte cadastrarTipoFonte(TipoFonte tipoFonte) throws Exception{
		this.webTarget = this.client.target(URI_TIPOS_FONTES);
		Invocation.Builder invocationBuilder =  this.webTarget.request(RESPONSE_TYPE);
		Response response = invocationBuilder.post(Entity.entity(tipoFonte, RESPONSE_TYPE));
		trataRetorno(response);
		return response.readEntity(TipoFonte.class);
	}

	public Fonte alterarFonte(Fonte fonte) throws Exception{
		this.webTarget = this.client.target(URI_FONTES).path(String.valueOf(fonte.getId()));
		Invocation.Builder invocationBuilder =  this.webTarget.request(RESPONSE_TYPE);
		Response response = invocationBuilder.put(Entity.entity(fonte, RESPONSE_TYPE));
		trataRetorno(response);
		return response.readEntity(Fonte.class);
	}
	
	public TipoFonte alterarTipoFonte(TipoFonte tipoFonte) throws Exception{
		this.webTarget = this.client.target(URI_TIPOS_FONTES).path(String.valueOf(tipoFonte.getId()));
		Invocation.Builder invocationBuilder =  this.webTarget.request(RESPONSE_TYPE);
		Response response = invocationBuilder.put(Entity.entity(tipoFonte, RESPONSE_TYPE));
		trataRetorno(response);
		return response.readEntity(TipoFonte.class);
	}

	public Fonte consultarFontePorCodigo(Long id){
		this.webTarget = this.client.target(URI_FONTES).path(String.valueOf(id));
		Invocation.Builder invocationBuilder =  this.webTarget.request(RESPONSE_TYPE);
		Response response = invocationBuilder.get();
		if(response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
			return null;
		}	
		else {
			return  response.readEntity(Fonte.class);
		}
	}
	
	public TipoFonte consultarTipoFontePorCodigo(Long id){
		this.webTarget = this.client.target(URI_TIPOS_FONTES).path(String.valueOf(id));
		Invocation.Builder invocationBuilder =  this.webTarget.request(RESPONSE_TYPE);
		Response response = invocationBuilder.get();
		if(response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
			return null;
		}	
		else {
			return  response.readEntity(TipoFonte.class);
		}
	}

	public String excluirFontePorCodigo(Long id){
		this.webTarget = this.client.target(URI_FONTES).path(String.valueOf(id));
		Invocation.Builder invocationBuilder =  this.webTarget.request(RESPONSE_TYPE);
		Response response = invocationBuilder.delete();
		if(response.getStatus() == Response.Status.NO_CONTENT.getStatusCode()) {
			return null;
		}	
		return response.readEntity(String.class);
	}
	
	public String excluirTipoFontePorCodigo(Long id){
		this.webTarget = this.client.target(URI_TIPOS_FONTES).path(String.valueOf(id));
		Invocation.Builder invocationBuilder =  this.webTarget.request(RESPONSE_TYPE);
		Response response = invocationBuilder.delete();
		if(response.getStatus() == Response.Status.NO_CONTENT.getStatusCode()) {
			return null;
		}	
		return response.readEntity(String.class);
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
