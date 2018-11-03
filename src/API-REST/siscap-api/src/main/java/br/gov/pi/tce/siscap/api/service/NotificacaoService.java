package br.gov.pi.tce.siscap.api.service;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import br.gov.pi.tce.siscap.api.model.Notificacao;
import br.gov.pi.tce.siscap.api.model.Publicacao;
import br.gov.pi.tce.siscap.api.repository.NotificacaoRepository;
import br.gov.pi.tce.siscap.api.repository.PublicacaoRepository;
import br.gov.pi.tce.siscap.api.service.exception.PublicacaoInexistenteException;

@Service
public class NotificacaoService {

	@Autowired
	private NotificacaoRepository notificacaoRepository;
	
	@Autowired
	private PublicacaoRepository publicacaoRepository;
	
	@Autowired
	private UsuarioService usuarioService;

	public Notificacao atualizar(Long id, Notificacao notificacao) {
		Notificacao notificacaoSalvo = buscarPeloCodigo(id);
		BeanUtils.copyProperties(notificacao, notificacaoSalvo, "id", "dataCriacao", "usuarioCriacao");
		salvar(notificacaoSalvo);
		
		return notificacaoSalvo;
	}

	public Notificacao adicionar(Notificacao notificacao) {
		atualizaDadosAdicao(notificacao);
		return salvar(notificacao);
	}

	private Notificacao salvar(Notificacao notificacaoSalvo) {
		validarPublicacao(notificacaoSalvo);

		atualizarDadosEdicao(notificacaoSalvo);
		
		return notificacaoRepository.save(notificacaoSalvo);
	}
	
	private void atualizaDadosAdicao(Notificacao notificacao) {
		notificacao.setUsuarioCriacao(usuarioService.getUsuarioLogado());
	}

	private void validarPublicacao(Notificacao notificacaoSalvo) {
		if (notificacaoSalvo.getPublicacao() != null && notificacaoSalvo.getPublicacao().getId() != null) {
			Optional<Publicacao> publicacaoOptional = publicacaoRepository.findById(notificacaoSalvo.getPublicacao().getId());

			if (!publicacaoOptional.isPresent()) {
				throw new PublicacaoInexistenteException();
			}
		}
	}

	private void atualizarDadosEdicao(Notificacao notificacao) {
		notificacao.setUsuarioAtualizacao(usuarioService.getUsuarioLogado());
	}

	private Notificacao buscarPeloCodigo(Long id) {
		Notificacao notificacaoSalvo = notificacaoRepository.findById(id).
				orElseThrow(() -> new EmptyResultDataAccessException(1));
		return notificacaoSalvo;
	}
}
