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

import br.gov.pi.tce.publicacoes.modelo.HistoricoPublicacao;

/**
 * @author Erick Guilherme Cavalcanti 
 *
 */
@Local
@Stateless(name="HistoricoPublicacaoServiceClient")
public class HistoricoPublicacaoServiceClient{
	
	private static final String PATH_GET_HISTORICO_PUBLICACAO = "getHistoricoPublicacao";
	private static final String PATH_ALTERAR = "alterar";
	private static final String PATH_CADASTRAR = "historicoPublicacoes/novo";
	private static final String RESPONSE_TYPE = "application/json;charset=UTF-8";
	private final String URL_SERVICE = "http://localhost:8080/apirestsiscap/rest/";
	private final String PATH_CONSULTA_TODOS = "historicoPublicacoes";

	private Client client;
	private WebTarget webTarget;
	
	public HistoricoPublicacaoServiceClient(){
		this.client = ClientBuilder.newClient();  
	}
	
	public List<HistoricoPublicacao> consultarTodos(){
		try {
			this.webTarget = this.client.target(URL_SERVICE).path(PATH_CONSULTA_TODOS);
			Invocation.Builder invocationBuilder =  this.webTarget.request(RESPONSE_TYPE);
			Response response = invocationBuilder.get();
			List<HistoricoPublicacao> list = response.readEntity(new GenericType<List<HistoricoPublicacao>>() {});
			return list;
		}
		catch (Exception e) {
			throw e;
		}
	}
	
	public HistoricoPublicacao cadastrarHistoricoPublicacao(HistoricoPublicacao historicoPublicacao){
		this.webTarget = this.client.target(URL_SERVICE).path(PATH_CADASTRAR);
		Invocation.Builder invocationBuilder =  this.webTarget.request(RESPONSE_TYPE);
		Response response = invocationBuilder.post(Entity.entity(historicoPublicacao, RESPONSE_TYPE));
		return response.readEntity(HistoricoPublicacao.class);
	}

	public HistoricoPublicacao alterarHistoricoPublicacao(HistoricoPublicacao historicoPublicacao){
		this.webTarget = this.client.target(URL_SERVICE).path(PATH_ALTERAR);
		Invocation.Builder invocationBuilder =  this.webTarget.request(RESPONSE_TYPE);
		Response response = invocationBuilder.put(Entity.entity(historicoPublicacao, RESPONSE_TYPE));
		return response.readEntity(HistoricoPublicacao.class);

	}

	public HistoricoPublicacao consultarHistoricoPublicacaoPorCodigo(Long id){
		this.webTarget = this.client.target(URL_SERVICE).path(PATH_GET_HISTORICO_PUBLICACAO).path(String.valueOf(id));
		Invocation.Builder invocationBuilder =  this.webTarget.request(RESPONSE_TYPE);
		Response response = invocationBuilder.get();
		return response.readEntity(HistoricoPublicacao.class);
	}

}
