package br.gov.pi.tce.siscap.timer.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import br.gov.pi.tce.siscap.timer.model.Notificacao;

@Component
public class NotificacaoClient extends AbstractServiceClient {

	private static final Logger logger = LoggerFactory.getLogger(NotificacaoClient.class);
	private static final String uriNotificacao = "notificacoes/";

	public Notificacao cadastrarNotificacao(Notificacao notificacao) {
		logger.info("Cadastrando notificação: " + notificacao.getTipo());
		HttpEntity<Notificacao> requestEntity = criaRequestEntity(notificacao);

		RestTemplate restTemplate = new RestTemplate();
		Notificacao notificacaoSalva = restTemplate.postForObject(getUriApi() + uriNotificacao, requestEntity,
				Notificacao.class);

		return notificacaoSalva;
	}

	private HttpEntity<Notificacao> criaRequestEntity(Notificacao notificacao) {
		HttpEntity<String> entity = getAuthenticatedHttpEntity();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		headers.put("Authorization", entity.getHeaders().get("Authorization"));

		HttpEntity<Notificacao> requestEntity = new HttpEntity<>(notificacao, headers);
		return requestEntity;
	}
}
