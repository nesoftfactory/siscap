package br.gov.pi.tce.siscap.api.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.gov.pi.tce.siscap.api.model.Arquivo;
import br.gov.pi.tce.siscap.api.model.Notificacao;
import br.gov.pi.tce.siscap.api.model.PaginaOCRArquivo;
import br.gov.pi.tce.siscap.api.model.Publicacao;
import br.gov.pi.tce.siscap.api.model.PublicacaoAnexo;
import br.gov.pi.tce.siscap.api.model.PublicacaoAnexoHistorico;
import br.gov.pi.tce.siscap.api.model.enums.SituacaoPublicacao;
import br.gov.pi.tce.siscap.api.repository.PaginaArquivoOCRRepository;
import br.gov.pi.tce.siscap.api.repository.PublicacaoAnexoHistoricoRepository;
import br.gov.pi.tce.siscap.api.repository.PublicacaoAnexoRepository;
import br.gov.pi.tce.siscap.api.repository.PublicacaoRepository;
import br.gov.pi.tce.siscap.api.service.exception.OCRException;
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
	
	@Autowired
	private OCRService ocrService;
	
	@Autowired
	private PaginaArquivoOCRRepository paginaArquivoOCRRepository;
	
	
	private static final String PUBLICACAO_ANEXO_MENSAGEM_INCLUSAO = "Anexo de Publicaçao incluído";
	
	public PublicacaoAnexo adicionar(PublicacaoAnexo publicacaoAnexo, MultipartFile partFile, String link) throws IOException {
		atualizaDadosAdicao(publicacaoAnexo);
		PublicacaoAnexo publicacaoAnexoSalvo = salvar(publicacaoAnexo, partFile, link);
		return publicacaoAnexoSalvo;
	}

	private PublicacaoAnexoHistorico atualizarHistorico(PublicacaoAnexo publicacaoAnexo, String msg, boolean isSucesso) {
		PublicacaoAnexoHistorico historico = new PublicacaoAnexoHistorico(publicacaoAnexo, 
				msg, isSucesso, usuarioService.getUsuarioLogado());
		publicacaoAnexoHistoricoRepository.save(historico);
		return historico;
	}

	private void atualizarArquivo(PublicacaoAnexo publicacaoAnexo, MultipartFile partFile, String link) throws IOException {
		Arquivo arquivo = null;
		if (partFile != null) {
			arquivo = new Arquivo(partFile, link, usuarioService.getUsuarioLogado());
		}
		publicacaoAnexo.setArquivo(arquivo);
	}

	private PublicacaoAnexo salvar(PublicacaoAnexo publicacaoAnexo, MultipartFile partFile, String link) throws IOException {
		atualizarArquivo(publicacaoAnexo, partFile, link);
		
		validarPublicacao(publicacaoAnexo);
		atualizarDadosEdicao(publicacaoAnexo);
		PublicacaoAnexo publicacaoAnexoSalvo = publicacaoAnexoRepository.save(publicacaoAnexo);
		if(publicacaoAnexoSalvo != null && publicacaoAnexoSalvo.getSucesso()) {
			publicacaoAnexoSalvo.setSituacao(SituacaoPublicacao.COLETA_REALIZADA.getDescricao());
		}
		
		PublicacaoAnexoHistorico historico = atualizarHistorico(publicacaoAnexoSalvo, PUBLICACAO_ANEXO_MENSAGEM_INCLUSAO, true);
		publicacaoAnexoHistoricoRepository.save(historico);
		
		Optional<Publicacao> publicacaoOptional = publicacaoRepository.findById(publicacaoAnexoSalvo.getPublicacao().getId());
		Publicacao publicacao = publicacaoOptional.get();
		publicacao.setPossuiAnexo(true);
		publicacaoRepository.save(publicacao);
		return publicacaoAnexoSalvo;
	}

	private void atualizarDadosEdicao(PublicacaoAnexo publicacaoAnexoSalvo) {
		publicacaoAnexoSalvo.setUsuarioAtualizacao(usuarioService.getUsuarioLogado());
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
		publicacaoAnexo.setUsuarioCriacao(usuarioService.getUsuarioLogado());
	}

	public PublicacaoAnexo atualizar(Long id, @Valid PublicacaoAnexo publicacaoAnexo, MultipartFile partFile,
			String link) throws IOException {
		PublicacaoAnexo publicacaoAnexoSalva = buscarPublicacaoAnexoPeloCodigo(id);
		BeanUtils.copyProperties(publicacaoAnexo, publicacaoAnexoSalva, "id", "arquivo", "dataCriacao", "usuarioCriacao");

		publicacaoAnexoSalva = salvar(publicacaoAnexo, partFile, link);
		
		return publicacaoAnexoSalva;
	}
	
	
	private PublicacaoAnexo buscarPublicacaoAnexoPeloCodigo(Long id) {
		PublicacaoAnexo publicacaoAnexoSalva = publicacaoAnexoRepository.findById(id).
				orElseThrow(() -> new EmptyResultDataAccessException(1));
		return publicacaoAnexoSalva;
	}

	public PublicacaoAnexo realizarOCRAnexo(Long idAnexoPublicacao) throws Exception {
		PublicacaoAnexo pa = buscarPublicacaoAnexoPeloCodigo(idAnexoPublicacao);
		if(pa == null || pa.getSituacao() == null || !pa.getSituacao().equals(SituacaoPublicacao.COLETA_REALIZADA.getDescricao())) {
			throw new Exception("Este anexo de publicação não existe, ou não está na situação ideal para ser feito o OCR");
		}
		Arquivo a = pa.getArquivo();
		if(a == null) {
			throw new Exception("Não foi encontrado arquivo para esse anexo de publicação");
		}
		
		Map<Integer, PaginaOCRArquivo> mapaPaginasArquivo = ocrService.getOCRPaginasArquivo(a.getId()); 
		PublicacaoAnexo publicacaoAnexoAtualizada = gravarPaginasArquivoAnexoPublicacao(idAnexoPublicacao, a.getId(), mapaPaginasArquivo);
		return publicacaoAnexoAtualizada;
	}
	
	
	
	private PublicacaoAnexo gravarPaginasArquivoAnexoPublicacao(Long idAnexoPublicacao, Long idArquivo, Map<Integer, PaginaOCRArquivo> mapaPaginasArquivo) throws OCRException{
		List<PaginaOCRArquivo> paginasArquivo = new ArrayList<PaginaOCRArquivo>();
		Notificacao notificacao;
		
		for (Iterator iterator = mapaPaginasArquivo.keySet().iterator(); iterator.hasNext();) {
			Integer pagina = (Integer) iterator.next();
			PaginaOCRArquivo paginaOCRArquivo = mapaPaginasArquivo.get(pagina);
			paginasArquivo.add(paginaOCRArquivo);
		}
		PublicacaoAnexo anexoPublicacao = buscarPublicacaoAnexoPeloCodigo(idAnexoPublicacao);
		try {
			paginaArquivoOCRRepository.gravarPaginasArquivo(paginasArquivo);
			
			atualizarHistorico(anexoPublicacao, "OCR realizado com sucesso", true);
		}
		catch (Exception e) {
			//TODO Helton
			//notificacao = criaNotificacaoErro(idPublicacao);
			
			//TODO Helton
			//disparaNotificacao();			
			atualizarHistorico(anexoPublicacao, "Erro ao tentar realizar OCR", false);
			throw new OCRException("Erro ao realizar OCR do arquivo: " + idArquivo);
		}
		
		Optional<PublicacaoAnexo> publicacaoAnexoOptional = publicacaoAnexoRepository.findById(idAnexoPublicacao);
		PublicacaoAnexo pa = publicacaoAnexoOptional.isPresent() ? publicacaoAnexoOptional.get() : null;
		if(pa != null) {
			pa.setSituacao(SituacaoPublicacao.OCR_REALIZADO.getDescricao());
		}
		PublicacaoAnexo salva = publicacaoAnexoRepository.save(pa);
		return salva;
	}
}
