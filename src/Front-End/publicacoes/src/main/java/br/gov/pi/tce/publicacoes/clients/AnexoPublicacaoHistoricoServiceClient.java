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
import br.gov.pi.tce.publicacoes.modelo.AnexoPublicacaoHistorico;

@Local
@Stateless(name = "AnexoPublicacaoHistoricoServiceClient")
public class AnexoPublicacaoHistoricoServiceClient {

	private static final String RESPONSE_TYPE = "application/json;charset=UTF-8";
	private String URI_HISTORICO_PUBLICACAO = "http://localhost:7788/historico_anexo_publicacao/";

	private Client client;
	private WebTarget webTarget;

	private static final Logger LOGGER = Logger.getLogger(AnexoPublicacaoHistoricoServiceClient.class);

	public AnexoPublicacaoHistoricoServiceClient() {
		this.client = ClientBuilder.newClient().register(new AutenticadorToken());
	}

	public List<AnexoPublicacaoHistorico> consultarAnexoPublicacaoHistoricoPeloIdAnexoPublicacao(Long id) {
		try {
			this.webTarget = this.client.target(URI_HISTORICO_PUBLICACAO + id);
			Response response = this.webTarget.request(RESPONSE_TYPE).get();
			List<AnexoPublicacaoHistorico> list = response
					.readEntity(new GenericType<List<AnexoPublicacaoHistorico>>() {
					});
			return list;
		} catch (Exception e) {
			LOGGER.error("Erro ao consultar todas as publicacoes");
			throw e;
		}
	}

}
