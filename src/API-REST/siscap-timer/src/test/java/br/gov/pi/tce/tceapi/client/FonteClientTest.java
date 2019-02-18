package br.gov.pi.tce.tceapi.client;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class FonteClientTest {

	private final String uriFonte = "http://localhost:7788/fontes";
	private final String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1NTIzOTcyNjIsInVzZXJfbmFtZSI6ImNvbXVtIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9DT01VTSJdLCJqdGkiOiJhYWRjNWYzMS1kM2YyLTQ4NjUtOGIxMS0zNzZjZWNiZWY0MzAiLCJjbGllbnRfaWQiOiJwdWJsaWNhY29lcyIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSJdfQ.6vCivqHuTx4ym7sygWDUVVvWHfdlmO0N4UUsjIUdb_0";

	@Test
	public void deveEncontrarFonteNaApi() {
		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + token);
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		
		ResponseEntity<String> result = restTemplate.exchange(uriFonte + "/13", HttpMethod.GET, entity, String.class);
//		ResponseEntity<String> response  = restTemplate.getForEntity(uriFonte + "/1", String.class);
		System.out.println(result);
		assertEquals(result.getStatusCode(), HttpStatus.OK);
	}
}
