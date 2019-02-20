package br.gov.pi.tce.siscap.timer.client;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import br.gov.pi.tce.siscap.timer.model.NotificacaoConfig;
import br.gov.pi.tce.siscap.timer.model.enums.NotificacaoTipo;

@Component
public class NotificacaoConfigClient extends AbstractServiceClient {

	private static final String uriNotificacaoConfig = "notificacoesconfig/";

	public List<NotificacaoConfig> consultarNotificacaoConfigPorTipo(NotificacaoTipo tipo, Boolean ativo) {
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> entity = getAuthenticatedHttpEntity();

		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(getUriApi() + uriNotificacaoConfig)
				.queryParam("ativo", ativo).queryParam("tipo", tipo.toString());

		ResponseEntity<List<NotificacaoConfig>> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity,
				new ParameterizedTypeReference<List<NotificacaoConfig>>() {});

		if (response.getStatusCode() == HttpStatus.NOT_FOUND) 
			return null;

		List<NotificacaoConfig> notificacoesConfig = response.getBody();
		return notificacoesConfig;

	}
}
