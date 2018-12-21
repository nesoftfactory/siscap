package br.gov.pi.tce.siscap.api.resource;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.gov.pi.tce.siscap.api.exceptionhandler.SiscapExceptionHandler.Erro;
import br.gov.pi.tce.siscap.api.model.Publicacao;
import br.gov.pi.tce.siscap.api.model.PublicacaoAnexo;
import br.gov.pi.tce.siscap.api.model.enums.SituacaoPublicacao;
import br.gov.pi.tce.siscap.api.repository.PublicacaoAnexoRepository;
import br.gov.pi.tce.siscap.api.repository.PublicacaoRepository;
import br.gov.pi.tce.siscap.api.service.PublicacaoAnexoService;
import br.gov.pi.tce.siscap.api.service.PublicacaoService;
import br.gov.pi.tce.siscap.api.service.exception.OCRException;

@RestController
@RequestMapping("/ocr_publicacoes")
public class PublicacaoOCRResource {

	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private PublicacaoService publicacaoService;
	
	@Autowired
	private PublicacaoAnexoService publicacaoAnexoService;
	
	@Autowired
	private PublicacaoRepository publicacaoRepository;
	
	@Autowired
	private PublicacaoAnexoRepository publicacaoAnexoRepository;
	
	
	@GetMapping("/{idFonte}/publicacoes_aptas")
	public List<Publicacao> buscarPublicacoesAptasParaOCR(@PathVariable Long idFonte) throws Exception {
		List<Publicacao> lista = publicacaoRepository.buscarPublicacoesAptasParaOCR(idFonte, SituacaoPublicacao.COLETA_REALIZADA.getDescricao()); 
		return lista;
	}
	
	
	@GetMapping("/{idFonte}/anexos_aptos")
	public List<PublicacaoAnexo> buscarAnexosPublicacoesAptosParaOCR(@PathVariable Long idFonte) throws Exception {
		List<PublicacaoAnexo> lista = publicacaoAnexoRepository.buscarAnexosPublicacoesAptosParaOCR(idFonte, SituacaoPublicacao.COLETA_REALIZADA.getDescricao()); 
		return lista;
	}
	
	
	
	@GetMapping("/{idPublicacao}/publicacao")
	public ResponseEntity<Publicacao> realizarOCRDaPublicacao(@PathVariable Long idPublicacao) throws Exception{
		Publicacao publicacaoSalva = publicacaoService.realizarOCRPublicacao(idPublicacao);
		return ResponseEntity.ok(publicacaoSalva);
	}

	
	@GetMapping("/{idPublicacaoAnexo}/anexo")
	public ResponseEntity<PublicacaoAnexo> realizarOCRDOAnexoPublicacao(@PathVariable Long idPublicacaoAnexo) throws IOException {
		PublicacaoAnexo publicacaoAnexoSalva = publicacaoAnexoService.realizarOCRAnexo(idPublicacaoAnexo);
		return ResponseEntity.ok(publicacaoAnexoSalva);
	}
	
	
	@ExceptionHandler(OCRException.class)
	public ResponseEntity<Object> handleOCRException(OCRException ex) {
		String mensagemUsuario = messageSource.getMessage("fonte.inexistente-ou-inativa", null, LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ex.toString();
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		
		return ResponseEntity.badRequest().body(erros);
	}

}
