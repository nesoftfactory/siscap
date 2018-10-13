package br.gov.pi.tce.siscap.api.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import br.gov.pi.tce.siscap.api.model.Usuario;
import br.gov.pi.tce.siscap.api.repository.UsuarioRepository;
import br.gov.pi.tce.siscap.api.service.exception.UsuarioComLoginJaExistenteException;

@Service
public class UsuarioService {

	
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	private Usuario usuarioLogado;
	
	public Usuario atualizar(Long id, Usuario usuario) {
		Usuario usuarioSalvo = buscarUsuarioPeloCodigo(id);
		BeanUtils.copyProperties(usuario, usuarioSalvo, "id", "dataCriacao", "usuarioCriacao");

		salvar(usuarioSalvo);
		
		return usuarioSalvo;
	}

	public Usuario adicionar(Usuario usuario) {
		atualizaDadosAdicao(usuario);
		Usuario usuarioSalvo = salvar(usuario);
		return usuarioSalvo;
	}

	public void atualizarPropriedadeAtivo(Long id, Boolean ativo){
		Usuario usuario = buscarUsuarioPeloCodigo(id);
		usuario.setAtivo(ativo);
		
		salvar(usuario);
	}
	
	public void atualizarPropriedadeAdmin(Long id, Boolean admin){
		Usuario usuario = buscarUsuarioPeloCodigo(id);
		usuario.setAdmin(admin);;
		salvar(usuario);
	}
	
	private Usuario salvar(Usuario usuario) {
		validarUsuario(usuario);
		atualizarDadosEdicao(usuario);
		Usuario usuarioSalvo = usuarioRepository.save(usuario);
		return usuarioSalvo;
	}
	
	private void validarUsuario(Usuario usuario) {
		//validarCpfDuplicado(usuario);
		validarLoginDuplicado(usuario);
	}

//	private void validarCpfDuplicado(Usuario usuario) {
//		if (usuario.isAlterando()) {
//			List<Usuario> usuarios = usuarioRepository
//					.buscarPorCpfComIdDiferenteDoInformado(usuario.getCpf(), usuario.getId());
//			if (!usuarios.isEmpty()) {
//				throw new UsuarioComCpfJaExistenteException();
//			}
//		} else {
//			if(usuarioRepository.findByCpf(usuario.getCpf()).isPresent()) {
//				throw new UsuarioComCpfJaExistenteException();
//			}
//		}
//	}

	private void validarLoginDuplicado(Usuario usuario) {
		if (usuario.isAlterando()) {
			List<Usuario> usuarios = usuarioRepository
					.buscarPorLoginComIdDiferenteDoInformado(usuario.getLogin(), usuario.getId());
			if (!usuarios.isEmpty()) {
				throw new UsuarioComLoginJaExistenteException();
			}
		} else {
			if(usuarioRepository.findByLogin(usuario.getLogin()).isPresent()) {
				throw new UsuarioComLoginJaExistenteException();
			}
		}
	}
	
	private void atualizaDadosAdicao(Usuario usuario) {
		usuario.setUsuarioCriacao(getUsuarioLogado());
		
	}

	private void atualizarDadosEdicao(Usuario usuario) {
		usuario.setUsuarioAtualizacao(getUsuarioLogado());
	}
	
	private Usuario buscarUsuarioPeloCodigo(Long id) {
		Usuario usuarioSalvo = usuarioRepository.findById(id).
				orElseThrow(() -> new EmptyResultDataAccessException(1));
		return usuarioSalvo;
	}

	
	public Usuario getUsuarioLogado() {
		if (this.usuarioLogado == null) {
			//TODO Depois verificar como ajusta para pegar o usuário da sessão
			//return buscarUsuarioPeloCodigo(2L);
			this.usuarioLogado = usuarioRepository.findAll().get(0);
		}
		return this.usuarioLogado;
	}
	
}
