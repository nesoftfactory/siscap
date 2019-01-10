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
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import br.gov.pi.tce.publicacoes.autenticacao.AutenticadorToken;
import br.gov.pi.tce.publicacoes.modelo.Usuario;

@Local
@Stateless(name="UsuarioServiceClient")
public class UsuarioServiceClient{
	
	private static final String RESPONSE_TYPE = "application/json;charset=UTF-8";
	private String URI = "http://localhost:7788/usuarios/";

	
	private Client client;
	private WebTarget webTarget;
	
	public UsuarioServiceClient(){
		this.client = ClientBuilder.newClient().register(new AutenticadorToken());  
	}
	
	public List<Usuario> consultarTodos(){
		try {
			this.webTarget = this.client.target(URI);
			Invocation.Builder invocationBuilder =  this.webTarget.request(RESPONSE_TYPE);
			Response response = invocationBuilder.get();
			List<Usuario> list = response.readEntity(new GenericType<List<Usuario>>() {});
			return list;
		}
		catch (Exception e) {
			throw e;
		}
	}
	
	
	public Usuario cadastrarUsuario(Usuario usuario) throws Exception{
		this.webTarget = this.client.target(URI);
		Invocation.Builder invocationBuilder =  this.webTarget.request(RESPONSE_TYPE);
		Response response = invocationBuilder.post(Entity.entity(usuario, RESPONSE_TYPE));
		trataRetorno(response);
		return response.readEntity(Usuario.class);
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



	public Usuario alterarUsuario(Usuario usuario) throws Exception{
		this.webTarget = this.client.target(URI).path(String.valueOf(usuario.getId()));
		Invocation.Builder invocationBuilder =  this.webTarget.request(RESPONSE_TYPE);
		Response response = invocationBuilder.put(Entity.entity(usuario, RESPONSE_TYPE));
		trataRetorno(response);
		return response.readEntity(Usuario.class);

	}


	public Usuario consultarUsuarioPorCodigo(Long id){
		this.webTarget = this.client.target(URI).path(String.valueOf(id));
		Invocation.Builder invocationBuilder =  this.webTarget.request(RESPONSE_TYPE);
		Response response = invocationBuilder.get();
		if(response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
			return null;
		}	
		else {
			return  response.readEntity(Usuario.class);
		}
	}


	public String excluirUsuarioPorCodigo(Long id){
		this.webTarget = this.client.target(URI).path(String.valueOf(id));
		Invocation.Builder invocationBuilder =  this.webTarget.request(RESPONSE_TYPE);
		Response response = invocationBuilder.delete();
		if(response.getStatus() == Response.Status.NO_CONTENT.getStatusCode()) {
			return null;
		}	
		return response.readEntity(String.class);

	}

}
