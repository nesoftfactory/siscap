package br.gov.pi.tce.siscap.api.model.teste;

import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.jayway.restassured.path.json.JsonPath;

import br.gov.pi.tce.siscap.api.model.Usuario;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UsuarioWSTeste {
	
	private static final String LOGIN_USUARIO_TESTE = "loginTeste";
	private static final String NOME_USUARIO_TESTE = "UsuÃ¡rio Teste";
	private static final boolean ADMIN_USUARIO_TESTE = false;
	private static final boolean ATIVO_USUARIO_TESTE = true;
	private String path_base = "http://localhost:7788/usuarios/";
	
	
	
	@Test
	public void criarUsuarioNovo() {
		Usuario usuarioTeste = new Usuario(NOME_USUARIO_TESTE, LOGIN_USUARIO_TESTE, ADMIN_USUARIO_TESTE, ATIVO_USUARIO_TESTE);
		JsonPath retorno =
            given()
            	.header("Accept", "application/json")
            	.contentType("application/json;charset=UTF-8")
                .body(usuarioTeste)
                .expect()
                .statusCode(201)
                .when()
                .post(path_base)
                .andReturn()
                .jsonPath();
		Usuario usuarioNovo = retorno.getObject("", Usuario.class);
        assertEquals(usuarioTeste, usuarioNovo);
        deletarUsuario(usuarioNovo.getId());
	}
	
	
	@Test
	public void retornarUsuarioPeloId() {
		Usuario novo = criarUsuario();
		
		JsonPath path = given()
				.header("Accept", "application/json")
				.expect()
                .statusCode(200)
                .when()
				.get(path_base + novo.getId())
				.andReturn().jsonPath();
		
		Usuario usuario1 = path.getObject("", Usuario.class);
		assertEquals(novo, usuario1);
	}
	
	
	@Test
	public void deletarUsuario() {
		Usuario novo = criarUsuario();
		String retorno = given()
		 .header("Accept", "application/json")
         .expect()
         .statusCode(204)
         .when()
         .delete(path_base + novo.getId())
         .andReturn().asString();
		assertEquals("", retorno);
	}
	
	
	//@Test
	public void criarUsuarioQueJaExiste() {
		
	}
	
	//@Test
	public void alterarUsuario() {
		
	}
	
	
	private void deletarUsuario(Long id) {
		String retorno = given()
		 .header("Accept", "application/json")
         .delete(path_base + id)
         .andReturn().asString();
	}
	
	private Usuario criarUsuario() {
		Usuario usuarioTeste = new Usuario(NOME_USUARIO_TESTE, LOGIN_USUARIO_TESTE, ADMIN_USUARIO_TESTE, ATIVO_USUARIO_TESTE);
		JsonPath retorno =
            given()
            	.header("Accept", "application/json")
            	.contentType("application/json;charset=UTF-8")
                .body(usuarioTeste)
                .post(path_base)
                .andReturn()
                .jsonPath();
		return retorno.getObject("", Usuario.class);
	}
	

}
