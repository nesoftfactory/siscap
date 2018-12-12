package br.gov.pi.tce.siscap.api.service;

import java.io.IOException;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.gov.pi.tce.siscap.api.model.Arquivo;
import br.gov.pi.tce.siscap.api.model.Publicacao;
import br.gov.pi.tce.siscap.api.model.PublicacaoAnexo;
import br.gov.pi.tce.siscap.api.model.PublicacaoAnexoHistorico;
import br.gov.pi.tce.siscap.api.model.enums.SituacaoPublicacao;
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
	
	private static final String PUBLICACAO_ANEXO_MENSAGEM_INCLUSAO = "Anexo de Publicaçao incluído";
	
	public PublicacaoAnexo adicionar(PublicacaoAnexo publicacaoAnexo, MultipartFile partFile, String link) throws IOException {
		atualizaDadosAdicao(publicacaoAnexo);
		PublicacaoAnexo publicacaoAnexoSalvo = salvar(publicacaoAnexo, partFile, link);
		return publicacaoAnexoSalvo;
	}

	private PublicacaoAnexoHistorico atualizarHistoricoAdicao(PublicacaoAnexo publicacaoAnexo) {
		PublicacaoAnexoHistorico historico = new PublicacaoAnexoHistorico(publicacaoAnexo, 
				PUBLICACAO_ANEXO_MENSAGEM_INCLUSAO, true, usuarioService.getUsuarioLogado());
		
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
		
		PublicacaoAnexoHistorico historico = atualizarHistoricoAdicao(publicacaoAnexoSalvo);
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

	public PublicacaoAnexo realizarOCRAnexo(Long id) {
		// TODO Auto-generated method stub
		//1 - Implementar OCR de cada página de um arquivo de um anexo publicação
		//2 - Se der certo pra todos log que deu tudo certo e:
		//	2.1 - Atualizar a coluna situação para  SituacaoPublicacao.OCR_REALIZADO
		//	2.2 - Atualizar historico
		//3 - Se der errado, log informando o erro e:
		//	3.1 - Gravar notificação 
		//	3.2 - Disparar notificação
		//	3.3 - Retornar Anexo de Publicação sem a situação alterada e alguma mensagem para ser registrada no log do WEB
		Optional<PublicacaoAnexo> publicacaoAnexoOptional = publicacaoAnexoRepository.findById(id);
		PublicacaoAnexo pa = publicacaoAnexoOptional.isPresent() ? publicacaoAnexoOptional.get() : null;
		if(pa != null) {
			pa.setSituacao(SituacaoPublicacao.OCR_REALIZADO.getDescricao());
		}
		return pa;
	}
}
