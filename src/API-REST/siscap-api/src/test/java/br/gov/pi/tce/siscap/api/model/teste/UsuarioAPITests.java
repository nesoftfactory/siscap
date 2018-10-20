package br.gov.pi.tce.siscap.api.model.teste;

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
	
	private static final String NOME_USUARIO_TESTE2 = "Usuário Teste2";
	
	
   @Steps
   UsuarioAPISteps usuarioAPISteps;
   
   
   
   @Test
	public void retornarUsuarioPeloId() {
	   Usuario usuario = usuarioAPISteps.criarUsuarioComSucesso(NOME_USUARIO_TESTE, LOGIN_USUARIO_TESTE, ADMIN_USUARIO_TESTE, ATIVO_USUARIO_TESTE);
	   usuarioAPISteps.retornarUsuarioPeloId(usuario.getId());
	   usuarioAPISteps.deletarUsuario(usuario.getId());
	}
   
   
    @Test
  	public void retornarTodosUsuariosComSucesso() {
  	   List<Usuario> usuarios = new ArrayList<Usuario>();
  	   usuarioAPISteps.retornaUsuarios(usuarios);
  	}
   
   
   	@Test
  	public void retornarUsuarioPeloIDError() {
   		usuarioAPISteps.retornarUsuarioPeloId(0L);
  	}
   	
   	@Test
  	public void criarUsuarioComSucesso() {
  		Usuario usuario = usuarioAPISteps.criarUsuarioComSucesso(NOME_USUARIO_TESTE, LOGIN_USUARIO_TESTE, ADMIN_USUARIO_TESTE, ATIVO_USUARIO_TESTE);
  		usuarioAPISteps.deletarUsuario(usuario.getId());
  	}
   	
   	
   	@Test
   	public void criarUsuarioComLoginJaExistente() {
   		Usuario usuario = usuarioAPISteps.criarUsuarioComSucesso(NOME_USUARIO_TESTE, LOGIN_USUARIO_TESTE, ADMIN_USUARIO_TESTE, ATIVO_USUARIO_TESTE);
   		usuarioAPISteps.criarUsuarioComLoginExistente(NOME_USUARIO_TESTE2, LOGIN_USUARIO_TESTE, ADMIN_USUARIO_TESTE, ATIVO_USUARIO_TESTE);
   		usuarioAPISteps.deletarUsuario(usuario.getId());
   	}
}