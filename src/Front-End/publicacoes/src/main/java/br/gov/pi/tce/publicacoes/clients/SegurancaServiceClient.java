package br.gov.pi.tce.publicacoes.clients;

import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;

import br.gov.pi.tce.publicacoes.autenticacao.AutenticadorLogin;
import br.gov.pi.tce.publicacoes.modelo.RequisicaoToken;
import br.gov.pi.tce.publicacoes.modelo.RespostaToken;
import br.gov.pi.tce.publicacoes.util.Propriedades;

@Local
@Stateless(name="SegurancaServiceClient")
public class SegurancaServiceClient{
	
	private Client client;
	private WebTarget webTarget;
	
	public SegurancaServiceClient(){
		this.client = ClientBuilder.newClient().register(new AutenticadorLogin()); 
	}
	
	private Invocation.Builder chamadaAPI(Long id) {
		Propriedades propriedades = Propriedades.getInstance();
		if (id == null) {
			this.webTarget = this.client.target(propriedades.getValorString("URI_API") + propriedades.getValorString("URI_SEGURANCA"));
		} else {
			this.webTarget = this.client.target(propriedades.getValorString("URI_API") + propriedades.getValorString("URI_SEGURANCA")).path(String.valueOf(id));
		}
//		this.webTarget.register(new Authenticator("publicacoes", "tc3p!"));
		return this.webTarget.request(propriedades.getValorString("RESPONSE_TYPE_X_WWW_FORM_URLENCODED"));
	}
	
	private void trataRetorno(Response response) throws Exception {
		if(response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
			List erros = response.readEntity(List.class);
			if(erros != null && !erros.isEmpty()) {
				Map p;
				String msg = (String)((Map)erros.get(0)).get("mensagemUsuario");
				throw new Exception(msg);
			}
			else {
				throw new Exception("Erro interno.");
			}
		}
	}
	
	public RespostaToken pegarToken(RequisicaoToken requisicaoToken) throws Exception{
//		String username="publicacoes";
//	    String password="tc3p!";
//	    String usernameAndPassword = username + ":" + password;
//	    String authorizationHeaderValue = "Basic " + java.util.Base64.getEncoder().encodeToString( usernameAndPassword.getBytes() );
	    Form form = new Form();
        form.param("client", requisicaoToken.getClient()).param("username", requisicaoToken.getUsername()).param("password", requisicaoToken.getPassword()).param("grant_type", requisicaoToken.getGrant_type());
        Entity<Form> entity = Entity.form(form);
//		Response response = chamadaAPI(null).header("Authorization", authorizationHeaderValue).post(entity);
		Response response = chamadaAPI(null).post(entity);
		trataRetorno(response);
		return response.readEntity(RespostaToken.class);
	}

}
