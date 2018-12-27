package br.gov.pi.tce.siscap.api.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.gov.pi.tce.siscap.api.model.Arquivo;
import br.gov.pi.tce.siscap.api.model.Fonte;
import br.gov.pi.tce.siscap.api.model.Notificacao;
import br.gov.pi.tce.siscap.api.model.PaginaOCRArquivo;
import br.gov.pi.tce.siscap.api.model.Publicacao;
import br.gov.pi.tce.siscap.api.model.PublicacaoHistorico;
import br.gov.pi.tce.siscap.api.model.enums.SituacaoPublicacao;
import br.gov.pi.tce.siscap.api.repository.ArquivoRepository;
import br.gov.pi.tce.siscap.api.repository.FonteRepository;
import br.gov.pi.tce.siscap.api.repository.PaginaArquivoOCRRepository;
import br.gov.pi.tce.siscap.api.repository.PublicacaoHistoricoRepository;
import br.gov.pi.tce.siscap.api.repository.PublicacaoRepository;
import br.gov.pi.tce.siscap.api.service.exception.FonteInexistenteOuInativaException;
import br.gov.pi.tce.siscap.api.service.exception.OCRException;

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
	
	@Autowired
	private PaginaArquivoOCRRepository paginaArquivoOCRRepository;
	
	@Autowired
	private ArquivoRepository arquivoRepository;
	
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
		if(publicacaoSalva != null && publicacaoSalva.getSucesso()) {
			publicacaoSalva.setSituacao(SituacaoPublicacao.COLETA_REALIZADA.getDescricao());
		}
		atualizarHistorico(publicacaoSalva, mensagemLog, true);
		
		return publicacaoSalva;
	}

	private PublicacaoHistorico atualizarHistorico(Publicacao publicacao, String mensagemLog, boolean isSucesso) {
		PublicacaoHistorico historico = new PublicacaoHistorico(publicacao, 
				mensagemLog, isSucesso, usuarioService.getUsuarioLogado());
		
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

	public Publicacao realizarOCRPublicacao(Long idPublicacao) throws Exception {
		
		Publicacao p = buscarPublicacaoPeloCodigo(idPublicacao);
		if(p == null || p.getSituacao() == null || !p.getSituacao().equals(SituacaoPublicacao.COLETA_REALIZADA.getDescricao())) {
			throw new Exception("Esta publicação não existe, ou não está na situação ideal para ser feito o OCR");
		}
		Arquivo a = p.getArquivo();
		if(a == null) {
			throw new Exception("Não foi encontrado arquivo para essa publicação");
		}
		
		Map<Integer, PaginaOCRArquivo> mapaPaginasArquivo = getOCRPaginasArquivo(a.getId()); 
		Publicacao publicacaoAtualizada = gravarPaginasArquivoPublicacao(idPublicacao, a.getId(), mapaPaginasArquivo);
		return publicacaoAtualizada;
	}

	private Publicacao gravarPaginasArquivoPublicacao(Long idPublicacao, Long idArquivo, Map<Integer, PaginaOCRArquivo> mapaPaginasArquivo) throws OCRException{
		List<PaginaOCRArquivo> paginasArquivo = new ArrayList<PaginaOCRArquivo>();
		Notificacao notificacao;
		
		for (Iterator iterator = mapaPaginasArquivo.keySet().iterator(); iterator.hasNext();) {
			Integer pagina = (Integer) iterator.next();
			PaginaOCRArquivo paginaOCRArquivo = mapaPaginasArquivo.get(pagina);
			paginasArquivo.add(paginaOCRArquivo);
		}
		Publicacao publicacao = buscarPublicacaoPeloCodigo(idPublicacao);
		try {
			paginaArquivoOCRRepository.gravarPaginasArquivo(paginasArquivo);
			
			atualizarHistorico(publicacao, "OCR realizado com sucesso", true);
		}
		catch (Exception e) {
			//TODO Helton
			//notificacao = criaNotificacaoErro(idPublicacao);
			
			//TODO Helton
			//disparaNotificacao();			
			atualizarHistorico(publicacao, "Erro ao tentar realizar OCR", false);
			throw new OCRException("Erro ao realizar OCR do arquivo: " + idArquivo);
		}
		
		Optional<Publicacao> publicacaoOptional = publicacaoRepository.findById(idPublicacao);
		Publicacao p = publicacaoOptional.isPresent() ? publicacaoOptional.get() : null;
		p.setQuantidadeTentativasOCR(p.getQuantidadeTentativasOCR() != null ? (p.getQuantidadeTentativasOCR() + 1) : 1);
		if(p != null) {
			p.setSituacao(SituacaoPublicacao.OCR_REALIZADO.getDescricao());
		}
		Publicacao salva = publicacaoRepository.save(p);
		return salva;
	}


// TODO Helton
//	private Notificacao criaNotificacaoErro(Long idPublicacao) {
//		Notificacao notificacao = new Notificacao();
//		notificacao.setTipo(NotificacaoTipo.OCR);
//		notificacao.setPublicacao(publicacao);
//		notificacao.setUsuarios(usuarios);
//		notificacao.setDataCriacao(dataCriacao);
//		notificacao.setDataAtualizacao(dataAtualizacao);
//		notificacao.setUsuarioAtualizacao(usuarioAtualizacao);
//		notificacao.setUsuarioCriacao();
//		
//	}

	protected Map<Integer, PaginaOCRArquivo> getOCRPaginasArquivo(Long idArquivo) {
		//TODO Antônio Moreira
		Map mapaPaginasArquivo = new HashMap<>();
		Optional<Arquivo> arquivoOptional = arquivoRepository.findById(idArquivo);
		Arquivo arquivo = arquivoOptional.isPresent() ? arquivoOptional.get() : null;
		if(arquivo == null) {
			throw new OCRException("Erro ao realizar OCR do arquivo: " + idArquivo + ". O arquivo não foi encontrado");
		}
		
		
		//TODO Implementação mock
		mapaPaginasArquivo.put(1, new PaginaOCRArquivo(1, "Pagina 1", arquivo));
		mapaPaginasArquivo.put(2, new PaginaOCRArquivo(2, "Pagina 2", arquivo));
		mapaPaginasArquivo.put(3, new PaginaOCRArquivo(3, "Pagina 3", arquivo));
		mapaPaginasArquivo.put(4, new PaginaOCRArquivo(4, "Pagina 4", arquivo));
		mapaPaginasArquivo.put(5, new PaginaOCRArquivo(5, "Pagina 5", arquivo));
		return mapaPaginasArquivo;
	}

}
