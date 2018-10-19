package br.gov.pi.tce.siscap.api.model.teste;

import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.jayway.restassured.path.json.JsonPath;

import br.gov.pi.tce.siscap.api.model.Usuario;

public class UsuarioWSTeste {
	
	
	
	
	@Test
	public void deveRetornarGetUsuarioPeloId() {
		JsonPath path = given()
				.header("Accept", "application/json")
				.get("http://localhost:7788/usuarios/9")
				.andReturn().jsonPath();
		
		Usuario usuario1 = path.getObject("", Usuario.class);
		
		Usuario usuarioEsperado1 = new Usuario(9L, "Administrador Automático", "adminAut", true, true);
		
		
		assertEquals(usuarioEsperado1, usuario1);
	}

}
