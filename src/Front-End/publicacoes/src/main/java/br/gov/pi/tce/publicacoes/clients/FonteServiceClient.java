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
	
	private Invocation.Builder chamadaFontesAPI(Long id) {
		if (id == null) {
			this.webTarget = this.client.target(URI_FONTES);
		} else {
			this.webTarget = this.client.target(URI_FONTES).path(String.valueOf(id));
		}
		return this.webTarget.request(RESPONSE_TYPE);
	}
	
	private Invocation.Builder chamadaTipoFontesAPI(Long id) {
		if (id == null) {
			this.webTarget = this.client.target(URI_TIPOS_FONTES);
		} else {
			this.webTarget = this.client.target(URI_TIPOS_FONTES).path(String.valueOf(id));
		}
		return this.webTarget.request(RESPONSE_TYPE);
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
	
	public List<Fonte> consultarTodasFontes(){
		try {
			Response response = chamadaFontesAPI(null).get();
			List<Fonte> list = response.readEntity(new GenericType<List<Fonte>>() {});
			return list;
		}
		catch (Exception e) {
			throw e;
		}
	}
	
	public Fonte cadastrarFonte(Fonte fonte) throws Exception{
		Response response = chamadaFontesAPI(null).post(Entity.entity(fonte, RESPONSE_TYPE));
		trataRetorno(response);
		return response.readEntity(Fonte.class);
	}
	
	public Fonte alterarFonte(Fonte fonte) throws Exception{
		Response response = chamadaFontesAPI(fonte.getId()).put(Entity.entity(fonte, RESPONSE_TYPE));
		trataRetorno(response);
		return response.readEntity(Fonte.class);
	}
	
	public Fonte consultarFontePorCodigo(Long id){
		Response response = chamadaFontesAPI(id).get();
		if(response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
			return null;
		}	
		else {
			return  response.readEntity(Fonte.class);
		}
	}
	
	public String excluirFontePorCodigo(Long id){
		Response response = chamadaFontesAPI(id).delete();
		if(response.getStatus() == Response.Status.NO_CONTENT.getStatusCode()) {
			return null;
		}	
		return response.readEntity(String.class);
	}
	
	public List<TipoFonte> consultarTodasTipoFontes(){
		try {
			Response response = chamadaTipoFontesAPI(null).get();
			List<TipoFonte> list = response.readEntity(new GenericType<List<TipoFonte>>() {});
			return list;
		}
		catch (Exception e) {
			throw e;
		}
	}
	
	public TipoFonte cadastrarTipoFonte(TipoFonte tipoFonte) throws Exception{
		Response response = chamadaTipoFontesAPI(null).post(Entity.entity(tipoFonte, RESPONSE_TYPE));
		trataRetorno(response);
		return response.readEntity(TipoFonte.class);
	}
	
	public TipoFonte alterarTipoFonte(TipoFonte tipoFonte) throws Exception{
		Response response = chamadaTipoFontesAPI(tipoFonte.getId()).put(Entity.entity(tipoFonte, RESPONSE_TYPE));
		trataRetorno(response);
		return response.readEntity(TipoFonte.class);
	}
	
	public TipoFonte consultarTipoFontePorCodigo(Long id){
		Response response = chamadaTipoFontesAPI(id).get();
		if(response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
			return null;
		}	
		else {
			TipoFonte tf = response.readEntity(TipoFonte.class);
			return  tf;
		}
	}

	public String excluirTipoFontePorCodigo(Long id){
		Response response = chamadaTipoFontesAPI(id).delete();
		if(response.getStatus() == Response.Status.NO_CONTENT.getStatusCode()) {
			return null;
		}	
		return response.readEntity(String.class);
	}

}
