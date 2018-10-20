package br.gov.pi.tce.siscap.api.model.teste;

import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;

import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.jayway.restassured.path.json.JsonPath;

import br.gov.pi.tce.siscap.api.model.Usuario;
import br.gov.pi.tce.siscap.api.model.teste.steps.CountriesSearchSteps;

//@RunWith(SerenityRunner.class)
public class CountriesSearchTests {
	
	private static final String LOGIN_USUARIO_TESTE = "loginTeste";
	private static final String NOME_USUARIO_TESTE = "UsuÃ¡rio Teste";
	private static final boolean ADMIN_USUARIO_TESTE = false;
	private static final boolean ATIVO_USUARIO_TESTE = true;
	private String path_base = "http://localhost:7788/usuarios/";
	
	
  // @Steps
   CountriesSearchSteps countriesSearchSteps;

  // @Test
   public void verifyThatWeCanFindUnitedStatesOfAmericaCountryUsingTheCodeUS() {
       countriesSearchSteps.searchCountryByCode("US");
       countriesSearchSteps.searchIsExecutedSuccesfully();
       countriesSearchSteps.iShouldFindCountry("United States of America");
   }

  // @Test
   public void verifyThatWeCanFindIndiaCountryUsingTheCodeIN(){
       countriesSearchSteps.searchCountryByCode("IN");
       countriesSearchSteps.searchIsExecutedSuccesfully();
       countriesSearchSteps.iShouldFindCountry("India");
   }

  // @Test
   public void verifyThatWeCanFindBrazilCountryUsingTheCodeBR(){
       countriesSearchSteps.searchCountryByCode("BR");
       countriesSearchSteps.searchIsExecutedSuccesfully();
       countriesSearchSteps.iShouldFindCountry("Brazil");
   }
   
 //  @Test
	public void retornarUsuarioPeloId() {
		//Usuario novo = criarUsuario();
		
		JsonPath path = given()
				.header("Accept", "application/json")
				.expect()
               .statusCode(200)
               .when()
				.get(path_base + "21")
				.andReturn().jsonPath();
		
		Usuario usuario1 = path.getObject("", Usuario.class);
		assertTrue(21L == usuario1.getId());
	}
   
  // @Test
  	public void retornarTeste() {
  		assertTrue(21L == 20L);
  	}
}