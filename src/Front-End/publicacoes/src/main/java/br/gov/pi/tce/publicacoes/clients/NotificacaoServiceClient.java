package br.gov.pi.tce.publicacoes.clients;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import br.gov.pi.tce.publicacoes.modelo.Notificacao;

/**
 * 
 * @author Erick Guilherme Cavalcanti 
 *
 */
@Local
@Stateless(name="NotificacaoServiceClient")
public class NotificacaoServiceClient{
	
	private static final String RESPONSE_TYPE = "application/json;charset=UTF-8";
	private String URI_NOTIFICACOES = "http://localhost:7788/notificacoes/";
	
	private static final Logger LOGGER = Logger.getLogger(NotificacaoServiceClient.class);

	private Client client;
	private WebTarget webTarget;
	
	public NotificacaoServiceClient(){
		this.client = ClientBuilder.newClient();  
	}
	
	public List<Notificacao> consultarNotificacoesPorIdPublicacao(Long idPublicacao) throws Exception{
		this.webTarget = this.client.target(URI_NOTIFICACOES).queryParam("idPublicacao", idPublicacao);
		Invocation.Builder invocationBuilder =  this.webTarget.request(RESPONSE_TYPE);
		Response response = invocationBuilder.get();
		if(response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
			return null;
		}	
		else {
			List<Notificacao> notificacoes = response.readEntity(new GenericType<List<Notificacao>>() {});
			return  notificacoes;
		}
	}

}
