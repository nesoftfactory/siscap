package br.gov.pi.tce.siscap.api.model.teste.steps;

import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import com.jayway.restassured.path.json.JsonPath;

import br.gov.pi.tce.siscap.api.model.Usuario;
import net.thucydides.core.annotations.Step;

public class UsuarioAPISteps {
	
	
	private String path_base = "http://localhost:7788/usuarios/";

   
   @Step("Criação de novo usuário")
   public Usuario criarUsuario(String nome, String login, boolean admin, boolean ativo) {
		Usuario usuarioTeste = new Usuario(nome, login, admin, ativo);
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
		return retorno.getObject("", Usuario.class);
	}
   
   
   @Step("Consulta de usuário pelo ID") 
	public void retornarUsuarioPeloId(Long id) {
		JsonPath path = given()
				.header("Accept", "application/json")
				.expect()
               .statusCode(200)
               .when()
				.get(path_base + id)
				.andReturn().jsonPath();
		
		Usuario usuario1 = path.getObject("", Usuario.class);
		assertTrue(id == usuario1.getId());
	}
   
   
   @Step("Deletar usuário pelo ID") 
   public void deletarUsuario(Long id) {
		String retorno = given()
		 .header("Accept", "application/json")
        .expect()
        .statusCode(204)
        .when()
        .delete(path_base + id)
        .andReturn().asString();
		assertEquals("", retorno);
	}


   @Step("Retornar todos os usuários")
	public void retornaUsuarios(List<Usuario> usuarios) {
		JsonPath path = given()
				.header("Accept", "application/json")
				.expect()
                .statusCode(200)
                .when()
				.get(path_base)
				.andReturn().jsonPath();
		List<Usuario> listaUsuarios = path.getList("");
		assertTrue(!listaUsuarios.isEmpty());
		for (Usuario usuario : usuarios) {
			assertTrue(listaUsuarios.contains(usuario));
		}
	}
}