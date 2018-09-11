package br.gov.pi.tce.publicacoes.clients;

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

import br.gov.pi.tce.publicacoes.modelo.Feriado;

/**
 * @author Erick Guilherme Cavalcanti 
 *
 */
@Local
@Stateless(name="FeriadoServiceClient")
public class FeriadoServiceClient{
	
	private static final String PATH_EXCLUIR = "excluir";
	private static final String PATH_GET_FERIADO = "getFeriado";
	private static final String PATH_ALTERAR = "alterar";
	private static final String PATH_CADASTRAR = "feriados/novo";
	private static final String RESPONSE_TYPE = "application/json;charset=UTF-8";
	private final String URL_SERVICE = "http://localhost:8080/apirestsiscap/rest/";
	private final String PATH_CONSULTA_TODOS = "feriados";

	private Client client;
	private WebTarget webTarget;
	
	public FeriadoServiceClient(){
		this.client = ClientBuilder.newClient();  
	}
	
	public List<Feriado> consultarTodos(){
		try {
			this.webTarget = this.client.target(URL_SERVICE).path(PATH_CONSULTA_TODOS);
			Invocation.Builder invocationBuilder =  this.webTarget.request(RESPONSE_TYPE);
			Response response = invocationBuilder.get();
			List<Feriado> list = response.readEntity(new GenericType<List<Feriado>>() {});
			return list;
		}
		catch (Exception e) {
			throw e;
		}
	}
	
	public Feriado cadastrarFeriado(Feriado feriado){
		this.webTarget = this.client.target(URL_SERVICE).path(PATH_CADASTRAR);
		Invocation.Builder invocationBuilder =  this.webTarget.request(RESPONSE_TYPE);
		Response response = invocationBuilder.post(Entity.entity(feriado, RESPONSE_TYPE));
		return response.readEntity(Feriado.class);
	}

	public Feriado alterarFeriado(Feriado feriado){
		this.webTarget = this.client.target(URL_SERVICE).path(PATH_ALTERAR);
		Invocation.Builder invocationBuilder =  this.webTarget.request(RESPONSE_TYPE);
		Response response = invocationBuilder.put(Entity.entity(feriado, RESPONSE_TYPE));
		return response.readEntity(Feriado.class);

	}

	public Feriado consultarFeriadoPorCodigo(Long id){
		this.webTarget = this.client.target(URL_SERVICE).path(PATH_GET_FERIADO).path(String.valueOf(id));
		Invocation.Builder invocationBuilder =  this.webTarget.request(RESPONSE_TYPE);
		Response response = invocationBuilder.get();
		return response.readEntity(Feriado.class);
	}

	public String excluirFeriadoPorCodigo(Long id){
		this.webTarget = this.client.target(URL_SERVICE).path(PATH_EXCLUIR).path(String.valueOf(id));
		Invocation.Builder invocationBuilder =  this.webTarget.request(RESPONSE_TYPE);
		Response response = invocationBuilder.delete();
		return response.readEntity(String.class);

	}

}
