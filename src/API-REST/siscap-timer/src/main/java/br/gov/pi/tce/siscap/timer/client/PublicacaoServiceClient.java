package br.gov.pi.tce.siscap.timer.client;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import br.gov.pi.tce.siscap.timer.model.Arquivo;
import br.gov.pi.tce.siscap.timer.model.Publicacao;
import br.gov.pi.tce.siscap.timer.model.PublicacaoAnexo;
import br.gov.pi.tce.siscap.timer.util.DateUtil;
import br.gov.pi.tce.siscap.timer.util.FileDownload;
import br.gov.pi.tce.siscap.timer.util.UriUtil;

@Component
public class PublicacaoServiceClient extends AbstractServiceClient {
	private static final Logger logger = LoggerFactory.getLogger(PublicacaoServiceClient.class);

	private static final String uriPublicacao = "publicacoes/";

	public List<Publicacao> consultarPublicacaoPorFonteDataNome(Long idFonte, LocalDate data, String nome) {

		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> entity = getAuthenticatedHttpEntity();

		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(getUriApi() + uriPublicacao)
				.queryParam("idFonte", idFonte).queryParam("data", data).queryParam("nome", nome);

		ResponseEntity<List<Publicacao>> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity,
				new ParameterizedTypeReference<List<Publicacao>>() {});

		if (response.getStatusCode() == HttpStatus.NOT_FOUND) 
			return null;

		List<Publicacao> publicacoes = response.getBody();
		return publicacoes;
	}

	public Publicacao cadastrarPublicacao(Publicacao publicacao, Arquivo arquivo) {
		HttpEntity<MultiValueMap<Object, Object>> requestEntity = criaRequestEntity(publicacao, arquivo);

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Publicacao> response = restTemplate.postForEntity(getUriApi() + uriPublicacao, requestEntity,
				Publicacao.class);

		Publicacao publicacaoSalva = response.getBody();
		return publicacaoSalva;
	}

	private HttpEntity<MultiValueMap<Object, Object>> criaRequestEntity(Publicacao publicacao, Arquivo arquivo) {
		HttpEntity<String> entity = getAuthenticatedHttpEntity();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		headers.put("Authorization", entity.getHeaders().get("Authorization"));

		MultiValueMap<Object, Object> body = criaPublicacaoBody(publicacao, arquivo);
		HttpEntity<MultiValueMap<Object, Object>> requestEntity = new HttpEntity<>(body, headers);
		return requestEntity;
	}

	private MultiValueMap<Object,Object> criaPublicacaoBody(Publicacao publicacao, Arquivo arquivo) {
		logger.info("Criando publicacao " + (StringUtils.isEmpty(arquivo.getLink()) ? "inexistente: " : "") 
				+ ": " + publicacao);
		
		MultiValueMap<Object, Object> body = new LinkedMultiValueMap<>();

		body.add("partFile", StringUtils.isEmpty(arquivo.getLink()) ? "" :
				realizarDownload(arquivo.getLink()));
		body.add("nome", publicacao.getNome());
		body.add("fonte", publicacao.getFonte().getId());
		body.add("data", DateUtil.convertLocalDateToString(publicacao.getData()));
		body.add("codigo", publicacao.getCodigo());
		body.add("sucesso", publicacao.getSucesso());
		body.add("possuiAnexo", publicacao.getPossuiAnexo());
		body.add("quantidadeTentativas", publicacao.getQuantidadeTentativas());
		body.add("link", UriUtil.encodeString(arquivo.getLink()));

		return body;
	}

	private FileSystemResource realizarDownload(String link) {
		FileDownload fileDownload = new FileDownload(link);
		return fileDownload.realizarDownload();
	}

	public PublicacaoAnexo cadastrarPublicacaoAnexo(PublicacaoAnexo publicacaoAnexo, Arquivo arquivoAnexo, boolean b) {
		// TODO Auto-generated method stub
		logger.info("*** cadastrarPublicacaoAnexo ainda não implementada ");
		return null;
	}

	public Publicacao alterarPublicacao(Publicacao publicacao, Arquivo arquivo, boolean b) {
		HttpEntity<MultiValueMap<Object, Object>> requestEntity = criaRequestEntity(publicacao, arquivo);

		RestTemplate restTemplate = new RestTemplate();
		restTemplate.put(getUriApi() + uriPublicacao + publicacao.getId(), requestEntity);

		return publicacao;
	}

}
