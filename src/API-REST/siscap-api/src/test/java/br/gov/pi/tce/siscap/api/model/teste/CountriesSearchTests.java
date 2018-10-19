package br.gov.pi.tce.siscap.api.model.teste;

import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import org.junit.Test;
import org.junit.runner.RunWith;
import br.gov.pi.tce.siscap.api.model.teste.steps.CountriesSearchSteps;

@RunWith(SerenityRunner.class)
public class CountriesSearchTests {
   @Steps
   CountriesSearchSteps countriesSearchSteps;

   @Test
   public void verifyThatWeCanFindUnitedStatesOfAmericaCountryUsingTheCodeUS() {
       countriesSearchSteps.searchCountryByCode("US");
       countriesSearchSteps.searchIsExecutedSuccesfully();
       countriesSearchSteps.iShouldFindCountry("United States of America");
   }

   @Test
   public void verifyThatWeCanFindIndiaCountryUsingTheCodeIN(){
       countriesSearchSteps.searchCountryByCode("IN");
       countriesSearchSteps.searchIsExecutedSuccesfully();
       countriesSearchSteps.iShouldFindCountry("India");
   }

   @Test
   public void verifyThatWeCanFindBrazilCountryUsingTheCodeBR(){
       countriesSearchSteps.searchCountryByCode("BR");
       countriesSearchSteps.searchIsExecutedSuccesfully();
       countriesSearchSteps.iShouldFindCountry("Brazil");
   }
}