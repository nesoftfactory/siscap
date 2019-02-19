package br.gov.pi.tce.siscap.timer.client;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import br.gov.pi.tce.siscap.timer.model.Notificacao;
import br.gov.pi.tce.siscap.timer.model.NotificacaoConfig;
import br.gov.pi.tce.siscap.timer.model.enums.NotificacaoTipo;

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
	
	public List<NotificacaoConfig> consultarNotificacaoConfigPorTipo(NotificacaoTipo tipo, Boolean ativo) {
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> entity = getAuthenticatedHttpEntity();

		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(getUriApi() + uriNotificacao)
				.queryParam("ativo", ativo).queryParam("tipo", tipo.toString());

		ResponseEntity<List<NotificacaoConfig>> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity,
				new ParameterizedTypeReference<List<NotificacaoConfig>>() {});

		if (response.getStatusCode() == HttpStatus.NOT_FOUND) 
			return null;

		List<NotificacaoConfig> notificacoesConfig = response.getBody();
		return notificacoesConfig;

	}
}
