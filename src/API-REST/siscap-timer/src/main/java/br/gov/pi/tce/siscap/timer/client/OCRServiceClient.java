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

import br.gov.pi.tce.siscap.timer.model.Publicacao;

@Component
public class OCRServiceClient extends AbstractServiceClient {

	private static final String uriOCRPublicacao = "ocr_publicacoes/";

	public List<Publicacao> consultarPublicacaoAptaParaOCR(Long idFonte) {

		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> entity = getAuthenticatedHttpEntity();

		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(getUriApi() + uriOCRPublicacao)
				.path(String.valueOf(idFonte))
				.path("/publicacoes_aptas");

		ResponseEntity<List<Publicacao>> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity,
				new ParameterizedTypeReference<List<Publicacao>>() {});

		if (response.getStatusCode() == HttpStatus.NOT_FOUND) 
			return null;

		List<Publicacao> publicacoes = response.getBody();
		return publicacoes;
	}
	
	public Publicacao realizarOCRPublicacao(Publicacao publicacao) {
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> entity = getAuthenticatedHttpEntity();

		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(getUriApi() + uriOCRPublicacao)
				.path(String.valueOf(publicacao.getId() + "/publicacao"));
		
		ResponseEntity<Publicacao> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity,
				Publicacao.class);

		if (response.getStatusCode() == HttpStatus.NOT_FOUND) 
			return null;

		Publicacao publicacaoOCR = response.getBody();
		return publicacaoOCR;
	}

	
}
