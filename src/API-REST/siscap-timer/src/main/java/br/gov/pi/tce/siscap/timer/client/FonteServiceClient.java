package br.gov.pi.tce.siscap.timer.client;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import br.gov.pi.tce.siscap.timer.model.Fonte;

@Component
public class FonteServiceClient extends AbstractServiceClient {
	
	private static final String uriFonte = "fontes/";

	public Fonte consultarFontePorIdFonte(Long idFonte) {
		// TODO refatorar acesso a API

		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> entity = getAuthenticatedHttpEntity();
		
		ResponseEntity<Fonte> response = restTemplate.exchange(getUriApi() + uriFonte + idFonte, 
				HttpMethod.GET, entity, Fonte.class);
		Fonte fonte = response.getBody();
		
		return fonte;
	}

}
