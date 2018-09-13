package br.gov.pi.tce.publicacoes.clients;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
	
	// Rotas da Entidade Fonte
	private final String PATH_CONSULTA_TODOS_FONTE = "fontes";
	
	//Rotas da Entidade TipoFonte
	private final String PATH_CONSULTA_TODOS_TIPOFONTE = "tipofontes";
	
	//Rotas para Fonte e TipoFonte
	private static final String PATH_CADASTRAR = "cadastrar";
	private static final String PATH_CONSULTAR_POR_ID = "consultar";
	private static final String PATH_ALTERAR = "alterar";
	private static final String PATH_EXCLUIR = "excluir";
	
	private static final String RESPONSE_TYPE = "application/json;charset=UTF-8";
	private final String URL_SERVICE = "http://localhost:8080/apirestsiscap/rest/";

	private Client client;
	private WebTarget webTarget;
	
	public FonteServiceClient(){
		this.client = ClientBuilder.newClient();  
	}
	
	public List<Fonte> consultarTodasFontes(){
		try {
			this.webTarget = this.client.target(URL_SERVICE).path(PATH_CONSULTA_TODOS_FONTE);
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
			this.webTarget = this.client.target(URL_SERVICE).path(PATH_CONSULTA_TODOS_TIPOFONTE);
			Invocation.Builder invocationBuilder =  this.webTarget.request(RESPONSE_TYPE);
			Response response = invocationBuilder.get();
			List<TipoFonte> list = response.readEntity(new GenericType<List<TipoFonte>>() {});
			return list;
		}
		catch (Exception e) {
			throw e;
		}
	}
	
	public Fonte cadastrarFonte(Fonte fonte){
		this.webTarget = this.client.target(URL_SERVICE).path(PATH_CADASTRAR);
		Invocation.Builder invocationBuilder =  this.webTarget.request(RESPONSE_TYPE);
		Response response = invocationBuilder.post(Entity.entity(fonte, RESPONSE_TYPE));
		return response.readEntity(Fonte.class);
	}
	
	public TipoFonte cadastrarTipoFonte(TipoFonte tipoFonte){
		this.webTarget = this.client.target(URL_SERVICE).path(PATH_CADASTRAR);
		Invocation.Builder invocationBuilder =  this.webTarget.request(RESPONSE_TYPE);
		Response response = invocationBuilder.post(Entity.entity(tipoFonte, RESPONSE_TYPE));
		return response.readEntity(TipoFonte.class);
	}

	public Fonte alterarFonte(Fonte fonte){
		this.webTarget = this.client.target(URL_SERVICE).path(PATH_ALTERAR);
		Invocation.Builder invocationBuilder =  this.webTarget.request(RESPONSE_TYPE);
		Response response = invocationBuilder.put(Entity.entity(fonte, RESPONSE_TYPE));
		return response.readEntity(Fonte.class);
	}
	
	public TipoFonte alterarTipoFonte(TipoFonte tipoFonte){
		this.webTarget = this.client.target(URL_SERVICE).path(PATH_ALTERAR);
		Invocation.Builder invocationBuilder =  this.webTarget.request(RESPONSE_TYPE);
		Response response = invocationBuilder.put(Entity.entity(tipoFonte, RESPONSE_TYPE));
		return response.readEntity(TipoFonte.class);
	}

	public Fonte consultarFontePorCodigo(UUID id){
		this.webTarget = this.client.target(URL_SERVICE).path(PATH_CONSULTAR_POR_ID).path(String.valueOf(id));
		Invocation.Builder invocationBuilder =  this.webTarget.request(RESPONSE_TYPE);
		Response response = invocationBuilder.get();
		return response.readEntity(Fonte.class);
	}
	
	public TipoFonte consultarTipoFontePorCodigo(UUID id){
		this.webTarget = this.client.target(URL_SERVICE).path(PATH_CONSULTAR_POR_ID).path(String.valueOf(id));
		Invocation.Builder invocationBuilder =  this.webTarget.request(RESPONSE_TYPE);
		Response response = invocationBuilder.get();
		return response.readEntity(TipoFonte.class);
	}

	public Fonte excluirFontePorCodigo(UUID id){
		this.webTarget = this.client.target(URL_SERVICE).path(PATH_EXCLUIR).path(String.valueOf(id));
		Invocation.Builder invocationBuilder =  this.webTarget.request(RESPONSE_TYPE);
		Response response = invocationBuilder.delete();
		return response.readEntity(Fonte.class);
	}
	
	public TipoFonte excluirTipoFontePorCodigo(UUID id){
		this.webTarget = this.client.target(URL_SERVICE).path(PATH_EXCLUIR).path(String.valueOf(id));
		Invocation.Builder invocationBuilder =  this.webTarget.request(RESPONSE_TYPE);
		Response response = invocationBuilder.delete();
		return response.readEntity(TipoFonte.class);
	}

}
