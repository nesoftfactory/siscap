package br.gov.pi.tce.siscap.api.repository;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
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
import br.gov.pi.tce.siscap.api.repository.UsuarioRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class UsuarioRepositoryTest {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	private static final String CPF = "02444657039";
	private static final String CPF2 = "91410800091";

	private Usuario usuario;
	private Usuario usuario2;
	
	@Before
	public void setUp() {
		this.usuario = criaUsuario(CPF, "teste", "usuario teste", "sistema");
		usuarioRepository.save(this.usuario);
		this.usuario2 = criaUsuario(CPF2, "teste", "usuario teste", "sistema");
		usuarioRepository.save(this.usuario2);
	}

	@After
	public void tearDown() {
		usuarioRepository.deleteAll();
	}
	
	@Test
	public void naoDeveConsiderarProprioCpfQuandoAlterando() {
		List<Usuario> usuarios = usuarioRepository.buscarPorCpfComIdDiferenteDoInformado(usuario.getCpf(), usuario.getId());
		
		assertTrue(usuarios.isEmpty());
	}

	@Test
	public void deveEncontrarOutroCpfQuandoAlterando() {
		usuario.setCpf(usuario2.getCpf());
		List<Usuario> usuarios = usuarioRepository.buscarPorCpfComIdDiferenteDoInformado(usuario.getCpf(), usuario.getId());
		
		assertFalse(usuarios.isEmpty());
	}

	@Test
	public void deveEncontrarCpfQuandoIncluindo() {
		Optional<Usuario> usuarioBanco = usuarioRepository.findByCpf(usuario.getCpf());
		
		assertTrue(usuarioBanco.isPresent());
	}

	private Usuario criaUsuario(String cpf, String login, String nome, String usuarioSitema) {
		usuario = new Usuario();		
		usuario.setCpf(cpf);
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

