package br.gov.pi.tce.publicacoes.autenticacao;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.xml.bind.DatatypeConverter;

import br.gov.pi.tce.publicacoes.util.Propriedades;

public class AutenticadorLogin implements ClientRequestFilter {

	private final String username;
    private final String password;

    public AutenticadorLogin() {
    	Propriedades propriedades = Propriedades.getInstance();
        this.username = propriedades.getValorString("AUTHORIZATION_USERNAME");
        this.password = propriedades.getValorString("AUTHORIZATION_PASSWORD");
    }

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        MultivaluedMap<String, Object> headers = requestContext.getHeaders();
        final String basicAuthentication = getBasicAuthentication();
        headers.add("Authorization", basicAuthentication);

    }

    private String getBasicAuthentication() {
        String token = this.username + ":" + this.password;
        try {
            return "Basic " + DatatypeConverter.printBase64Binary(token.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            throw new IllegalStateException("Cannot encode with UTF-8", ex);
        }
    }

	
}
