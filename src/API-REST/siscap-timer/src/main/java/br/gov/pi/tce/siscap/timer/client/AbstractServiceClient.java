package br.gov.pi.tce.siscap.timer.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import br.gov.pi.tce.siscap.timer.config.property.SiscapTimerProperty;

public abstract class AbstractServiceClient {
	
	private static final String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1NTIzOTcyNjIsInVzZXJfbmFtZSI6ImNvbXVtIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9DT01VTSJdLCJqdGkiOiJhYWRjNWYzMS1kM2YyLTQ4NjUtOGIxMS0zNzZjZWNiZWY0MzAiLCJjbGllbnRfaWQiOiJwdWJsaWNhY29lcyIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSJdfQ.6vCivqHuTx4ym7sygWDUVVvWHfdlmO0N4UUsjIUdb_0";

	@Autowired
	private SiscapTimerProperty siscapTimerProperty;
	
	protected HttpEntity<String> getAuthenticatedHttpEntity() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + token);
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		return entity;
	}
	
	protected String getUriApi() {
		return siscapTimerProperty.getUriApiSicap();
	}

}
