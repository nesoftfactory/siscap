package br.gov.pi.tce.siscap.api.model.teste;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import br.gov.pi.tce.siscap.api.model.Usuario;
import br.gov.pi.tce.siscap.api.model.teste.steps.UsuarioAPISteps;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;

@RunWith(SerenityRunner.class)
public class UsuarioAPITests {
	
	private static final String LOGIN_USUARIO_TESTE = "loginTeste";
	private static final String NOME_USUARIO_TESTE = "Usuário Teste";
	private static final boolean ADMIN_USUARIO_TESTE = false;
	private static final boolean ATIVO_USUARIO_TESTE = true;
	
	private static final String LOGIN_USUARIO_TESTE2 = "loginTeste2";
	private static final String NOME_USUARIO_TESTE2 = "Usuário Teste2";
	private static final boolean ADMIN_USUARIO_TESTE2 = false;
	private static final boolean ATIVO_USUARIO_TESTE2 = true;
	
	
   @Steps
   UsuarioAPISteps usuarioAPISteps;

   
   @Test
	public void retornarUsuarioPeloId() {
	   Usuario usuario = usuarioAPISteps.criarUsuario(NOME_USUARIO_TESTE, LOGIN_USUARIO_TESTE, ADMIN_USUARIO_TESTE, ATIVO_USUARIO_TESTE);
	   usuarioAPISteps.retornarUsuarioPeloId(usuario.getId());
	   usuarioAPISteps.deletarUsuario(usuario.getId());
	}
   
   
   @Test
  	public void retornarUsuarios() {
  	   //Usuario usuario = usuarioAPISteps.criarUsuario(NOME_USUARIO_TESTE, LOGIN_USUARIO_TESTE, ADMIN_USUARIO_TESTE, ATIVO_USUARIO_TESTE);
  	   Usuario usuario2 = usuarioAPISteps.criarUsuario(NOME_USUARIO_TESTE2, LOGIN_USUARIO_TESTE2, ADMIN_USUARIO_TESTE2, ATIVO_USUARIO_TESTE2);
  	   List<Usuario> usuarios = new ArrayList<Usuario>();
  	   //usuarios.add(usuario);
  	   usuarios.add(usuario2);
  	   usuarioAPISteps.retornaUsuarios(usuarios);
  	   //usuarioAPISteps.deletarUsuario(usuario.getId());
  	   usuarioAPISteps.deletarUsuario(usuario2.getId());
  	}
   
   
   	@Test
  	public void retornarUsuarioPeloIDError() {
   		try {
   			usuarioAPISteps.retornarUsuarioPeloId(0L);
   		}
   		catch(Exception e) {
   			fail("Usuário não existe");
   		}
  	}
}