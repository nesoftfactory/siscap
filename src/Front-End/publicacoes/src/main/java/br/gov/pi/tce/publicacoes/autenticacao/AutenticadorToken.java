package br.gov.pi.tce.publicacoes.autenticacao;

import java.io.IOException;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.MultivaluedMap;

import br.gov.pi.tce.publicacoes.util.SessionUtil;

public class AutenticadorToken implements ClientRequestFilter {

	private final String token;

    public AutenticadorToken() {
        this.token = (String) SessionUtil.getParam("token");
    }
    
    public AutenticadorToken(String token) {
		if (token != null) {
			this.token = token;
		}else {
			this.token = (String) SessionUtil.getParam("token");
		}
    }

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        MultivaluedMap<String, Object> headers = requestContext.getHeaders();
        final String basicAuthentication = getBasicAuthentication();
        headers.add("Authorization", basicAuthentication);

    }

    private String getBasicAuthentication() {
        return "Bearer " + this.token;
    }
	
}
