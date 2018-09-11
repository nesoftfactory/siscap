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

import br.gov.pi.tce.publicacoes.modelo.Publicacao;

/**
 * @author Erick Guilherme Cavalcanti 
 *
 */
@Local
@Stateless(name="PublicacaoServiceClient")
public class PublicacaoServiceClient{
	
	private static final String PATH_GET_PUBLICACAO = "getPublicacao";
	private static final String PATH_ALTERAR = "alterar";
	private static final String PATH_CADASTRAR = "publicacoes/novo";
	private static final String RESPONSE_TYPE = "application/json;charset=UTF-8";
	private final String URL_SERVICE = "http://localhost:8080/apirestsiscap/rest/";
	private final String PATH_CONSULTA_TODOS = "publicacoes";

	private Client client;
	private WebTarget webTarget;
	
	public PublicacaoServiceClient(){
		this.client = ClientBuilder.newClient();  
	}
	
	public List<Publicacao> consultarTodos(){
		try {
			this.webTarget = this.client.target(URL_SERVICE).path(PATH_CONSULTA_TODOS);
			Invocation.Builder invocationBuilder =  this.webTarget.request(RESPONSE_TYPE);
			Response response = invocationBuilder.get();
			List<Publicacao> list = response.readEntity(new GenericType<List<Publicacao>>() {});
			return list;
		}
		catch (Exception e) {
			throw e;
		}
	}
	
	public Publicacao cadastrarPublicacao(Publicacao publicacao){
		this.webTarget = this.client.target(URL_SERVICE).path(PATH_CADASTRAR);
		Invocation.Builder invocationBuilder =  this.webTarget.request(RESPONSE_TYPE);
		Response response = invocationBuilder.post(Entity.entity(publicacao, RESPONSE_TYPE));
		return response.readEntity(Publicacao.class);
	}

	public Publicacao alterarPublicacao(Publicacao publicacao){
		this.webTarget = this.client.target(URL_SERVICE).path(PATH_ALTERAR);
		Invocation.Builder invocationBuilder =  this.webTarget.request(RESPONSE_TYPE);
		Response response = invocationBuilder.put(Entity.entity(publicacao, RESPONSE_TYPE));
		return response.readEntity(Publicacao.class);

	}

	public Publicacao consultarPublicacaoPorCodigo(Long id){
		this.webTarget = this.client.target(URL_SERVICE).path(PATH_GET_PUBLICACAO).path(String.valueOf(id));
		Invocation.Builder invocationBuilder =  this.webTarget.request(RESPONSE_TYPE);
		Response response = invocationBuilder.get();
		return response.readEntity(Publicacao.class);
	}

}
