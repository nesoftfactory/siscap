package br.gov.pi.tce.siscap.api.service;

import java.io.IOException;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.gov.pi.tce.siscap.api.model.Arquivo;
import br.gov.pi.tce.siscap.api.model.Fonte;
import br.gov.pi.tce.siscap.api.model.Publicacao;
import br.gov.pi.tce.siscap.api.model.PublicacaoHistorico;
import br.gov.pi.tce.siscap.api.model.Usuario;
import br.gov.pi.tce.siscap.api.repository.FonteRepository;
import br.gov.pi.tce.siscap.api.repository.PublicacaoHistoricoRepository;
import br.gov.pi.tce.siscap.api.repository.PublicacaoRepository;
import br.gov.pi.tce.siscap.api.service.exception.FonteInexistenteOuInativaException;

@Service
public class PublicacaoService {

	@Autowired
	private PublicacaoRepository publicacaoRepository;
	
	@Autowired
	private PublicacaoHistoricoRepository publicacaoHistoricoRepository;
	
	@Autowired
	private FonteRepository fonteRepository;
	
	@Autowired
	private UsuarioService usuarioService;
	
	private Usuario usuarioLogado;
	
	@PostConstruct
	public void setarUsuarioLogado() {
		usuarioLogado = usuarioService.getUsuarioLogado();
	}

	private static final String PUBLICACAO_MENSAGEM_INCLUSAO = "Publicaçao incluída";
	
	public Publicacao adicionar(Publicacao publicacao, MultipartFile partFile, String link) throws IOException {
		atualizarArquivo(publicacao, partFile, link);
		atualizaDadosAdicao(publicacao);

		PublicacaoHistorico historico = atualizarHistorico(publicacao);
		
		Publicacao publicacaoSalva = salvar(publicacao);
		publicacaoHistoricoRepository.save(historico);
		
		return publicacaoSalva;
	}

	private PublicacaoHistorico atualizarHistorico(Publicacao publicacao) {
		PublicacaoHistorico historico = new PublicacaoHistorico(publicacao, 
				PUBLICACAO_MENSAGEM_INCLUSAO, true, usuarioLogado);
		
		return historico;
	}

	private void atualizarArquivo(Publicacao publicacao, MultipartFile partFile, String link) throws IOException {
		Arquivo arquivo = null;
		if (partFile != null) {
			arquivo = new Arquivo(partFile, link, usuarioLogado);
		}
		publicacao.setArquivo(arquivo);
	}

	private Publicacao salvar(Publicacao publicacaoSalva) {
		validarFonte(publicacaoSalva);
		atualizarDadosEdicao(publicacaoSalva);
		
		return publicacaoRepository.save(publicacaoSalva);
	}

	private void atualizarDadosEdicao(Publicacao publicacaoSalva) {
		publicacaoSalva.setUsuarioAtualizacao(usuarioLogado);
	}

	private void validarFonte(Publicacao publicacaoSalva) {
		if (publicacaoSalva.getFonte() != null && publicacaoSalva.getFonte().getId() != null) {
			Optional<Fonte> fonteOptional = fonteRepository.findById(publicacaoSalva.getFonte().getId());

			if (!fonteOptional.isPresent() || fonteOptional.get().isInativo()) {
				throw new FonteInexistenteOuInativaException();
			}
		}
	}

	private void atualizaDadosAdicao(Publicacao publicacao) {
		publicacao.setUsuarioCriacao(usuarioLogado);
	}
}
