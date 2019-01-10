package br.gov.pi.tce.publicacoes.clients;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import br.gov.pi.tce.publicacoes.autenticacao.AutenticadorToken;
import br.gov.pi.tce.publicacoes.modelo.PublicacaoHistorico;

@Local
@Stateless(name="PublicacaoHistoricoServiceClient")
public class PublicacaoHistoricoServiceClient {
	
	private static final String RESPONSE_TYPE = "application/json;charset=UTF-8";
	private String URI_HISTORICO_PUBLICACAO = "http://localhost:7788/historico_publicacao/";

	private Client client;
	private WebTarget webTarget;
	
	private static final Logger LOGGER = Logger.getLogger(PublicacaoHistoricoServiceClient.class);
	
	public PublicacaoHistoricoServiceClient(){
		this.client = ClientBuilder.newClient().register(new AutenticadorToken());  
	}
	
	
	public List<PublicacaoHistorico> consultarPublicacaoHistoricoPeloIdPublicacao(Long id){
		try {
			this.webTarget = this.client.target(URI_HISTORICO_PUBLICACAO+ id);
			Response response =  this.webTarget.request(RESPONSE_TYPE).get();
			List<PublicacaoHistorico> list = response.readEntity(new GenericType<List<PublicacaoHistorico>>() {});
			return list;
		}	
		catch (Exception e) {
			LOGGER.error("Erro ao consultar todas as publicacoes");
			throw e;
		}
	}
	

}
