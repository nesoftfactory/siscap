package br.gov.pi.tce.siscap.timer.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import br.gov.pi.tce.siscap.timer.model.Notificacao;
import br.gov.pi.tce.siscap.timer.model.dto.NotificacaoDTO;

@Component
public class NotificacaoClient extends AbstractServiceClient {

	private static final Logger logger = LoggerFactory.getLogger(NotificacaoClient.class);
	private static final String uriNotificacao = "notificacoes/";

	public Notificacao cadastrarNotificacao(NotificacaoDTO notificacao) {
		logger.info("Cadastrando notificação: " + notificacao.getTipo());
		HttpEntity<NotificacaoDTO> requestEntity = criaRequestEntity(notificacao);

		RestTemplate restTemplate = new RestTemplate();
		/*
		ResponseEntity<Notificacao> response= restTemplate.postForEntity(getUriApi() + uriNotificacao, 
				requestEntity, Notificacao.class);

		Notificacao notificacaoSalva = response.getBody();
		/* 
		/*
		 * Notificacao notificacaoSalva = restTemplate.postForObject(getUriApi() +
		 * uriNotificacao, requestEntity, Notificacao.class);
		 */

		ResponseEntity<Notificacao> response= restTemplate.exchange(getUriApi() + uriNotificacao, 
				HttpMethod.POST, requestEntity, Notificacao.class);

		Notificacao notificacaoSalva = response.getBody();
		return notificacaoSalva;
	}

	private HttpEntity<NotificacaoDTO> criaRequestEntity(NotificacaoDTO notificacao) {
		HttpEntity<String> entity = getAuthenticatedHttpEntity();

		HttpHeaders headers = new HttpHeaders();
		headers.put("Authorization", entity.getHeaders().get("Authorization"));

		HttpEntity<NotificacaoDTO> requestEntity = new HttpEntity<>(notificacao, headers);
		return requestEntity;
	}
	
}
