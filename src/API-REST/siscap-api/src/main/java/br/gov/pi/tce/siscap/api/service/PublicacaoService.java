package br.gov.pi.tce.siscap.api.service;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.gov.pi.tce.siscap.api.model.Arquivo;
import br.gov.pi.tce.siscap.api.model.Fonte;
import br.gov.pi.tce.siscap.api.model.Publicacao;
import br.gov.pi.tce.siscap.api.model.PublicacaoHistorico;
import br.gov.pi.tce.siscap.api.repository.FonteRepository;
import br.gov.pi.tce.siscap.api.repository.PublicacaoHistoricoRepository;
import br.gov.pi.tce.siscap.api.repository.PublicacaoRepository;
import br.gov.pi.tce.siscap.api.service.exception.FonteInexistenteOuInativaException;

@Service
public class PublicacaoService {

	private static final String PUBLICACAO_MENSAGEM_INCLUSAO = "Publicação incluída";
	private static final String PUBLICACAO_MENSAGEM_ALTERACAO = "Publicação alterada";
	
	@Autowired
	private PublicacaoRepository publicacaoRepository;
	
	@Autowired
	private PublicacaoHistoricoRepository publicacaoHistoricoRepository;
	
	@Autowired
	private FonteRepository fonteRepository;
	
	@Autowired
	private UsuarioService usuarioService;
	
	public Publicacao adicionar(Publicacao publicacao, MultipartFile partFile, String link) throws IOException {
		atualizaDadosAdicao(publicacao);
		Publicacao publicacaoSalva = salvar(publicacao, partFile, link);

		return publicacaoSalva;
	}

	public Publicacao atualizar(Long id, Publicacao publicacao, MultipartFile partFile, String link) throws IOException {
		Publicacao publicacaoSalva = buscarPublicacaoPeloCodigo(id);
		BeanUtils.copyProperties(publicacao, publicacaoSalva, "id", "arquivo", "dataCriacao", "usuarioCriacao");

		publicacaoSalva = salvar(publicacaoSalva, partFile, link);
		
		return publicacaoSalva;
	}

	private Publicacao salvar(Publicacao publicacaoSalva, MultipartFile partFile, String link) throws IOException {
		validarFonte(publicacaoSalva);

		atualizarArquivo(publicacaoSalva, partFile, link);
		atualizarDadosEdicao(publicacaoSalva);
		
		String mensagemLog = (publicacaoSalva.isAlterando() ? PUBLICACAO_MENSAGEM_ALTERACAO : 
			PUBLICACAO_MENSAGEM_INCLUSAO);
		
		publicacaoSalva = publicacaoRepository.save(publicacaoSalva);
		atualizarHistorico(publicacaoSalva, mensagemLog);
		
		return publicacaoSalva;
	}

	private PublicacaoHistorico atualizarHistorico(Publicacao publicacao, String mensagemLog) {
		PublicacaoHistorico historico = new PublicacaoHistorico(publicacao, 
				mensagemLog, true, usuarioService.getUsuarioLogado());
		
		publicacaoHistoricoRepository.save(historico);
		
		return historico;
	}
	
	private void atualizarArquivo(Publicacao publicacao, MultipartFile partFile, String link) throws IOException {
		Arquivo arquivo = null;
		if (partFile != null) {
			arquivo = new Arquivo(partFile, link, usuarioService.getUsuarioLogado());
		}
		publicacao.setArquivo(arquivo);
	}
	
	private void atualizarDadosEdicao(Publicacao publicacaoSalva) {
		publicacaoSalva.setUsuarioAtualizacao(usuarioService.getUsuarioLogado());
	}

	private void validarFonte(Publicacao publicacaoSalva) {
		if (publicacaoSalva.getFonte() != null && publicacaoSalva.getFonte().getId() != null) {
			Optional<Fonte> fonteOptional = fonteRepository.findById(publicacaoSalva.getFonte().getId());

			if (!fonteOptional.isPresent() || fonteOptional.get().isInativa()) {
				throw new FonteInexistenteOuInativaException();
			}
		}
	}

	private void atualizaDadosAdicao(Publicacao publicacao) {
		publicacao.setUsuarioCriacao(usuarioService.getUsuarioLogado());
	}

	private Publicacao buscarPublicacaoPeloCodigo(Long id) {
		Publicacao publicacaoSalva = publicacaoRepository.findById(id).
				orElseThrow(() -> new EmptyResultDataAccessException(1));
		return publicacaoSalva;
	}

}
