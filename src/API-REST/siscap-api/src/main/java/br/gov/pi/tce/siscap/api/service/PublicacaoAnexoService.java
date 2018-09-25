package br.gov.pi.tce.siscap.api.service;

import java.io.IOException;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.gov.pi.tce.siscap.api.model.Arquivo;
import br.gov.pi.tce.siscap.api.model.Publicacao;
import br.gov.pi.tce.siscap.api.model.PublicacaoAnexo;
import br.gov.pi.tce.siscap.api.model.PublicacaoAnexoHistorico;
import br.gov.pi.tce.siscap.api.model.Usuario;
import br.gov.pi.tce.siscap.api.repository.PublicacaoAnexoHistoricoRepository;
import br.gov.pi.tce.siscap.api.repository.PublicacaoAnexoRepository;
import br.gov.pi.tce.siscap.api.repository.PublicacaoRepository;
import br.gov.pi.tce.siscap.api.service.exception.PublicacaoInexistenteException;

@Service
public class PublicacaoAnexoService {

	@Autowired
	private PublicacaoAnexoRepository publicacaoAnexoRepository;
	
	@Autowired
	private PublicacaoAnexoHistoricoRepository publicacaoAnexoHistoricoRepository;
	
	@Autowired
	private PublicacaoRepository publicacaoRepository;
	
	@Autowired
	private UsuarioService usuarioService;
	
	private Usuario usuarioLogado;
	
	@PostConstruct
	public void setarUsuarioLogado() {
		usuarioLogado = usuarioService.getUsuarioLogado();
	}

	private static final String PUBLICACAO_ANEXO_MENSAGEM_INCLUSAO = "Anexo de Publicaçao incluído";
	
	public PublicacaoAnexo adicionar(PublicacaoAnexo publicacaoAnexo, MultipartFile partFile, String link) throws IOException {
		atualizarArquivo(publicacaoAnexo, partFile, link);
		atualizaDadosAdicao(publicacaoAnexo);
		
		PublicacaoAnexo publicacaoAnexoSalvo = salvar(publicacaoAnexo);
		
		PublicacaoAnexoHistorico historico = atualizarHistoricoAdicao(publicacaoAnexoSalvo);
		publicacaoAnexoHistoricoRepository.save(historico);
		
		return publicacaoAnexoSalvo;
	}

	private PublicacaoAnexoHistorico atualizarHistoricoAdicao(PublicacaoAnexo publicacaoAnexo) {
		PublicacaoAnexoHistorico historico = new PublicacaoAnexoHistorico(publicacaoAnexo, 
				PUBLICACAO_ANEXO_MENSAGEM_INCLUSAO, "Encontrado", usuarioLogado);
		
		return historico;
	}

	private void atualizarArquivo(PublicacaoAnexo publicacaoAnexo, MultipartFile partFile, String link) throws IOException {
		Arquivo arquivo = null;
		if (partFile != null) {
			arquivo = new Arquivo(partFile, link, usuarioLogado);
		}
		publicacaoAnexo.setArquivo(arquivo);
	}

	private PublicacaoAnexo salvar(PublicacaoAnexo publicacaoAnexoSalvo) {
		validarPublicacao(publicacaoAnexoSalvo);
		atualizarDadosEdicao(publicacaoAnexoSalvo);
		
		return publicacaoAnexoRepository.save(publicacaoAnexoSalvo);
	}

	private void atualizarDadosEdicao(PublicacaoAnexo publicacaoAnexoSalvo) {
		publicacaoAnexoSalvo.setUsuarioAtualizacao(usuarioLogado);
	}

	private void validarPublicacao(PublicacaoAnexo publicacaoAnexoSalvo) {
		if (publicacaoAnexoSalvo.getPublicacao() != null && publicacaoAnexoSalvo.getPublicacao().getId() != null) {
			Optional<Publicacao> publicacaoOptional = publicacaoRepository.findById(publicacaoAnexoSalvo.getPublicacao().getId());

			if (!publicacaoOptional.isPresent()) {
				throw new PublicacaoInexistenteException();
			}
		}
	}

	private void atualizaDadosAdicao(PublicacaoAnexo publicacaoAnexo) {
		publicacaoAnexo.setUsuarioCriacao(usuarioLogado);
	}
}
