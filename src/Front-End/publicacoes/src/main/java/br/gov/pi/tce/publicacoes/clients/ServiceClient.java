package br.gov.pi.tce.publicacoes.clients;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import modelo.Usuario;

public class ServiceClient {

	
	private Client client;

	/**ACESSA UM RECURSO IDENTIFICADO PELO URI(Uniform Resource Identifier/Identificador Uniforme de Recursos)*/
	private WebTarget webTarget;

	/**URL DO SERVIÇO REST QUE VAMOS ACESSAR */
	private final String URL_SERVICE = "http://localhost:8081/WebServiceRest/rest/service/";

	public ServiceClient(){

		this.client = ClientBuilder.newClient();  
	}

	public String CadastrarUsuario(Usuario Usuario){

		this.webTarget = this.client.target(URL_SERVICE).path("cadastrar");

		Invocation.Builder invocationBuilder =  this.webTarget.request("application/json;charset=UTF-8");

		Response response = invocationBuilder.post(Entity.entity(Usuario, "application/json;charset=UTF-8"));

		return response.readEntity(String.class);

	}

	public String AlterarUsuario(Usuario Usuario){

		this.webTarget = this.client.target(URL_SERVICE).path("alterar");

		Invocation.Builder invocationBuilder =  this.webTarget.request("application/json;charset=UTF-8");

		Response response = invocationBuilder.put(Entity.entity(Usuario, "application/json;charset=UTF-8"));

		return response.readEntity(String.class);

	}

	public List<Usuario> ConsultarTodasUsuarios(){

		this.webTarget = this.client.target(URL_SERVICE).path("todasUsuarios");

		Invocation.Builder invocationBuilder =  this.webTarget.request("application/json;charset=UTF-8");

		Response response = invocationBuilder.get();
		
		List<Usuario> list = response.readEntity(new GenericType<List<Usuario>>() {});
		
		return list;


	}

	public Usuario ConsultarUsuarioPorCodigo(int codigo){

		this.webTarget = this.client.target(URL_SERVICE).path("getUsuario").path(String.valueOf(codigo));

		Invocation.Builder invocationBuilder =  this.webTarget.request("application/json;charset=UTF-8");

		Response response = invocationBuilder.get();

		return response.readEntity(Usuario.class);

	}


	public String ExcluirUsuarioPorCodigo(int codigo){

		this.webTarget = this.client.target(URL_SERVICE).path("excluir").path(String.valueOf(codigo));

		Invocation.Builder invocationBuilder =  this.webTarget.request("application/json;charset=UTF-8");

		Response response = invocationBuilder.delete();

		return response.readEntity(String.class);

	}
}
