package br.gov.pi.tce.publicacoes.clients;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import br.gov.pi.tce.publicacoes.modelo.Arquivo;

@Local
@Stateless(name="ArquivoServiceClient")
public class ArquivoServiceClient {
	
	private static final String RESPONSE_TYPE = "application/json;charset=UTF-8";
	private String URI_ARQUIVOS = "http://localhost:7788/arquivos/";

	private Client client;
	private WebTarget webTarget;
	
	public ArquivoServiceClient(){
		this.client = ClientBuilder.newClient();  
	}
	
	
	public Arquivo consultarArquivoPorCodigo(Long id){
		this.webTarget = this.client.target(URI_ARQUIVOS).path(String.valueOf(id));
		Response response =  this.webTarget.request(RESPONSE_TYPE).get();
		if(response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
			return null;
		}	
		else {
			return  response.readEntity(Arquivo.class);
		}
	}
	

}
