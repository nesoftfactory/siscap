package br.gov.pi.tce.siscap.api.model.teste;

import org.junit.Test;

import com.jayway.restassured.path.json.JsonPath;

import br.gov.pi.tce.siscap.api.model.Usuario;

import static com.jayway.restassured.RestAssured.*;

public class UsuarioWSTeste {
	
	@Test
	public void deveRetornarListaDeUsuarios() {
		JsonPath path = given().header("Accept", "application/json").get("/usuario").andReturn().jsonPath();
		
		Usuario usuario1 = path.getObject("list.usuario[0]", Usuario.class);
		
		
		get("usuarios").andReturn().andReturn();
	}
	
	
	
	@Test
	public void deveRetornarGetUsuarioPeloId() {
		JsonPath path = given().header("Accept", "application/json").get("/usuario").andReturn().jsonPath();
		
		Usuario usuario1 = path.getObject("list.usuario[0]", Usuario.class);
		
		
		get("usuarios").andReturn().andReturn();
	}

}
