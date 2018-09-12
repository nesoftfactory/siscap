package br.gov.pi.tce.publicacoes.clients;

import java.util.ArrayList;
import java.util.List;

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
	
	private static final String PATH_EXCLUIR = "excluir";
	private static final String PATH_GET_FONTE = "getFonte";
	private static final String PATH_ALTERAR = "alterar";
	private static final String PATH_CADASTRAR = "fontes/novo";
	private static final String RESPONSE_TYPE = "application/json;charset=UTF-8";
	private final String URL_SERVICE = "http://localhost:8080/apirestsiscap/rest/";
	private final String PATH_CONSULTA_TODOS = "fontes";

	private Client client;
	private WebTarget webTarget;
	private ArrayList<TipoFonte> tipoFontes = new ArrayList<TipoFonte>();
	
	private void iniciaTipoFontes() {
		this.tipoFontes.add(new TipoFonte(TipoFonte.TIPO_FONTE_PADRAO_1));
		this.tipoFontes.add(new TipoFonte(TipoFonte.TIPO_FONTE_PADRAO_2));
	}
	
	public FonteServiceClient(){
		this.client = ClientBuilder.newClient();  
	}
	
	public ArrayList<TipoFonte> getDefaultTipoFontes() {
		if (tipoFontes.size() == 0) {
			iniciaTipoFontes();
		}
		return tipoFontes;
	}
	
	public TipoFonte consultarTipoFontePorNome(String nome) {
		if (tipoFontes.size() == 0) {
			iniciaTipoFontes();
		}
		
		for (TipoFonte tipoFonte: tipoFontes) {
			if (tipoFonte.getNome().equals(nome)) {
				return tipoFonte;
			}
		}
		
		return null;
	}
	
	public List<Fonte> consultarTodos(){
		try {
			this.webTarget = this.client.target(URL_SERVICE).path(PATH_CONSULTA_TODOS);
			Invocation.Builder invocationBuilder =  this.webTarget.request(RESPONSE_TYPE);
			Response response = invocationBuilder.get();
			List<Fonte> list = response.readEntity(new GenericType<List<Fonte>>() {});
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

	public Fonte alterarFonte(Fonte fonte){
		this.webTarget = this.client.target(URL_SERVICE).path(PATH_ALTERAR);
		Invocation.Builder invocationBuilder =  this.webTarget.request(RESPONSE_TYPE);
		Response response = invocationBuilder.put(Entity.entity(fonte, RESPONSE_TYPE));
		return response.readEntity(Fonte.class);

	}

	public Fonte consultarFontePorCodigo(Long id){
		this.webTarget = this.client.target(URL_SERVICE).path(PATH_GET_FONTE).path(String.valueOf(id));
		Invocation.Builder invocationBuilder =  this.webTarget.request(RESPONSE_TYPE);
		Response response = invocationBuilder.get();
		return response.readEntity(Fonte.class);
	}

	public String excluirFontePorCodigo(Long id){
		this.webTarget = this.client.target(URL_SERVICE).path(PATH_EXCLUIR).path(String.valueOf(id));
		Invocation.Builder invocationBuilder =  this.webTarget.request(RESPONSE_TYPE);
		Response response = invocationBuilder.delete();
		return response.readEntity(String.class);

	}

}
