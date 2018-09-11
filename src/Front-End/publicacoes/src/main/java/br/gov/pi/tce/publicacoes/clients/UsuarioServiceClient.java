package br.gov.pi.tce.publicacoes.clients;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import br.gov.pi.tce.publicacoes.modelo.Usuario;

@Local
@Stateless(name="UsuarioServiceClient")
public class UsuarioServiceClient{
	
	private static final String PATH_EXCLUIR = "excluir";
	private static final String PATH_GET_USUARIO = "getUsuario";
	private static final String PATH_ALTERAR = "alterar";
	private static final String PATH_CADASTRAR = "usuarios/novo";
	private static final String RESPONSE_TYPE = "application/json;charset=UTF-8";
	private final String URL_SERVICE = "http://localhost:7788/";
	private final String PATH = "usuarios";
	private String URI = "http://localhost:7788/usuarios";

	
	private Client client;
	private WebTarget webTarget;


	
	public UsuarioServiceClient(){
		this.client = ClientBuilder.newClient();  
	}
	
	
	
	public List<Usuario> consultarTodos(){
		try {
			this.webTarget = this.client.target(URL_SERVICE).path(PATH);
			Invocation.Builder invocationBuilder =  this.webTarget.request(RESPONSE_TYPE);
			Response response = invocationBuilder.get();
			List<Usuario> list = response.readEntity(new GenericType<List<Usuario>>() {});
			return list;
		}
		catch (Exception e) {
			throw e;
		}
	}
	
	
	public Usuario cadastrarUsuario(Usuario usuario){
			
//		RestTemplate restTemplate = new RestTemplate();
//		restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
//		restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
//		Usuario result = restTemplate.postForObject( URI, usuario, Usuario.class);
//		return result;
		
		//this.webTarget = this.client.target(URL_SERVICE).path(PATH);
		this.webTarget = this.client.target(URL_SERVICE);
		Invocation.Builder invocationBuilder =  this.webTarget.request(RESPONSE_TYPE);
		Response response = invocationBuilder.post(Entity.entity(usuario, RESPONSE_TYPE));
		return response.readEntity(Usuario.class);
	}

	public Usuario alterarUsuario(Usuario usuario){
		this.webTarget = this.client.target(URL_SERVICE).path(PATH_ALTERAR);
		Invocation.Builder invocationBuilder =  this.webTarget.request(RESPONSE_TYPE);
		Response response = invocationBuilder.put(Entity.entity(usuario, RESPONSE_TYPE));
		return response.readEntity(Usuario.class);

	}


	public Usuario consultarUsuarioPorCodigo(Long id){
		this.webTarget = this.client.target(URL_SERVICE).path(PATH_GET_USUARIO).path(String.valueOf(id));
		Invocation.Builder invocationBuilder =  this.webTarget.request(RESPONSE_TYPE);
		Response response = invocationBuilder.get();
		return response.readEntity(Usuario.class);
	}


	public String excluirUsuarioPorCodigo(Long id){
		this.webTarget = this.client.target(URL_SERVICE).path(PATH_EXCLUIR).path(String.valueOf(id));
		Invocation.Builder invocationBuilder =  this.webTarget.request(RESPONSE_TYPE);
		Response response = invocationBuilder.delete();
		return response.readEntity(String.class);

	}

}
