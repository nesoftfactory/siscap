package br.gov.pi.tce.siscap.api.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import br.gov.pi.tce.siscap.api.model.NotificacaoConfig;
import br.gov.pi.tce.siscap.api.repository.NotificacaoConfigRepository;

@Service
public class NotificacaoConfigService {

	@Autowired
	private NotificacaoConfigRepository notificacaoConfigRepository;
	
	@Autowired
	private UsuarioService usuarioService;

	public NotificacaoConfig atualizar(Long id, NotificacaoConfig notificacaoConfig) {
		NotificacaoConfig notificacaoConfigSalvo = buscarPeloCodigo(id);
		BeanUtils.copyProperties(notificacaoConfig, notificacaoConfigSalvo, "id", "dataCriacao", "usuarioCriacao");
		salvar(notificacaoConfigSalvo);
		
		return notificacaoConfigSalvo;
	}

	public NotificacaoConfig adicionar(NotificacaoConfig notificacaoConfig) {
		atualizaDadosAdicao(notificacaoConfig);
		return salvar(notificacaoConfig);
	}

	public void atualizarPropriedadeAtivo(Long id, Boolean ativo) {
		NotificacaoConfig notificacaoConfig = buscarPeloCodigo(id);
		notificacaoConfig.setAtivo(ativo);
		salvar(notificacaoConfig);
	}
	
	private NotificacaoConfig salvar(NotificacaoConfig notificacaoConfigSalvo) {
		atualizarDadosEdicao(notificacaoConfigSalvo);
		
		return notificacaoConfigRepository.save(notificacaoConfigSalvo);
	}
	
	private void atualizaDadosAdicao(NotificacaoConfig notificacaoConfig) {
		notificacaoConfig.setUsuarioCriacao(usuarioService.getUsuarioLogado());
	}

	private void atualizarDadosEdicao(NotificacaoConfig notificacaoConfig) {
		notificacaoConfig.setUsuarioAtualizacao(usuarioService.getUsuarioLogado());
	}

	private NotificacaoConfig buscarPeloCodigo(Long id) {
		NotificacaoConfig notificacaoConfigSalvo = notificacaoConfigRepository.findById(id).
				orElseThrow(() -> new EmptyResultDataAccessException(1));
		return notificacaoConfigSalvo;
	}
}
