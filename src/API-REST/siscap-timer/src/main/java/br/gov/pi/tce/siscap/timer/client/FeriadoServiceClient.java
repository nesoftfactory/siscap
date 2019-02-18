package br.gov.pi.tce.siscap.timer.client;

import java.time.LocalDate;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import br.gov.pi.tce.siscap.timer.model.Feriado;

@Component
public class FeriadoServiceClient extends AbstractServiceClient {
	
	private static final String uriFeriados = "feriados/";

	public List<Feriado> feriadosPorFontePeriodo(Long idFonte, LocalDate periodoDe, LocalDate periodoAte) {
		// TODO refatorar acesso a API

		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> entity = getAuthenticatedHttpEntity();
		
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(getUriApi() + uriFeriados)
				// Add query parameter
				.queryParam("idFonte", idFonte).queryParam("periodoDe", periodoDe).queryParam("periodoAte", periodoAte);

		ResponseEntity<List<Feriado>> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity,
				new ParameterizedTypeReference<List<Feriado>>() {
				});

		if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
			return null;
		}

		List<Feriado> feriados = response.getBody();
		return feriados;
		
	}

}
