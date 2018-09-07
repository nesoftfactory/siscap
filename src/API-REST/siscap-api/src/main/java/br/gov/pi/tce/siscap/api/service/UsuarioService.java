package br.gov.pi.tce.siscap.api.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import br.gov.pi.tce.siscap.api.model.Usuario;
import br.gov.pi.tce.siscap.api.repository.UsuarioRepository;

@Service
public class UsuarioService {

	private final String USUARIO = "automatizar";
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	public Usuario atualizar(Long id, Usuario usuario) {
		Usuario usuarioSalvo = buscarUsuarioPeloCodigo(id);
		BeanUtils.copyProperties(usuario, usuarioSalvo, "id", "dataCriacao", "usuarioCriacao");
		atualizarDadosEdicao(usuarioSalvo);
		usuarioRepository.save(usuarioSalvo);
		
		return usuarioSalvo;
	}

	public Usuario adicionar(Usuario usuario) {
		atualizaDadosAdicao(usuario);
		Usuario usuarioSalvo = usuarioRepository.save(usuario);
		
		return usuarioSalvo;
	}

	public void atualizarPropriedadeAtivo(Long id, Boolean ativo) {
		Usuario usuario = buscarUsuarioPeloCodigo(id);
		usuario.setAtivo(ativo);
		atualizarDadosEdicao(usuario);
		usuarioRepository.save(usuario);
	}
	
	public void atualizarPropriedadeAdmin(Long id, Boolean admin) {
		Usuario usuario = buscarUsuarioPeloCodigo(id);
		usuario.setAdmin(admin);;
		atualizarDadosEdicao(usuario);
		usuarioRepository.save(usuario);
	}
	
	private void atualizaDadosAdicao(Usuario usuario) {
		usuario.setUsuarioCriacao(USUARIO);
		atualizarDadosEdicao(usuario);
	}

	private void atualizarDadosEdicao(Usuario usuario) {
		usuario.setUsuarioAtualizacao(USUARIO);
	}

	private Usuario buscarUsuarioPeloCodigo(Long id) {
		Usuario usuarioSalvo = usuarioRepository.findById(id).
				orElseThrow(() -> new EmptyResultDataAccessException(1));
		return usuarioSalvo;
	}
}
