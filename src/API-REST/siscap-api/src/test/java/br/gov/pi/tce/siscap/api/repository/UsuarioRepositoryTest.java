package br.gov.pi.tce.siscap.api.repository;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.gov.pi.tce.siscap.api.model.Usuario;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class UsuarioRepositoryTest {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	private Usuario usuario;
	private Usuario usuario2;
	
	private final Usuario usuarioAutomatico = null;
	
	@Before
	public void setUp() {
		this.usuario = criaUsuario("teste1", "usuario teste", usuarioAutomatico);
		usuarioRepository.save(this.usuario);
		this.usuario2 = criaUsuario("teste2", "usuario teste", usuarioAutomatico);
		usuarioRepository.save(this.usuario2);
	}

	@After
	public void tearDown() {
		usuarioRepository.deleteAll();
	}
	
	@Test
	public void naoDeveConsiderarProprioLoginQuandoAlterando() {
		List<Usuario> usuarios = usuarioRepository.buscarPorLoginComIdDiferenteDoInformado(usuario.getLogin(), usuario.getId());
		
		assertTrue(usuarios.isEmpty());
	}

	@Test
	public void deveEncontrarOutroLoginQuandoAlterando() {
		usuario.setLogin(usuario2.getLogin());
		List<Usuario> usuarios = usuarioRepository.buscarPorLoginComIdDiferenteDoInformado(usuario.getLogin(), usuario.getId());
		
		assertFalse(usuarios.isEmpty());
	}

	@Test
	public void deveEncontrarLoginQuandoIncluindo() {
		Optional<Usuario> usuarioBanco = usuarioRepository.findByLogin(usuario.getLogin());
		
		assertTrue(usuarioBanco.isPresent());
	}

	private Usuario criaUsuario(String login, String nome, Usuario usuarioSitema) {
		Usuario usuario = new Usuario();		
		usuario.setAdmin(true);
		usuario.setAtivo(true);
		usuario.setDataAtualizacao(LocalDateTime.now());
		usuario.setDataCriacao(LocalDateTime.now());
		usuario.setLogin(login);
		usuario.setNome(nome);
		usuario.setUsuarioAtualizacao(usuarioSitema);
		usuario.setUsuarioCriacao(usuarioSitema);
		
		return usuario;
	}
}

