package br.gov.pi.tce.siscap.api.resource;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.gov.pi.tce.siscap.api.event.RecursoCriadoEvent;
import br.gov.pi.tce.siscap.api.exceptionhandler.SiscapExceptionHandler.Erro;
import br.gov.pi.tce.siscap.api.model.Publicacao;
import br.gov.pi.tce.siscap.api.repository.PublicacaoRepository;
import br.gov.pi.tce.siscap.api.repository.filter.PublicacaoFilter;
import br.gov.pi.tce.siscap.api.service.PublicacaoService;
import br.gov.pi.tce.siscap.api.service.exception.FiltroPublicacaoDataInvalidaException;
import br.gov.pi.tce.siscap.api.service.exception.FonteInexistenteOuInativaException;

@RestController
@RequestMapping("/publicacoes")
public class PublicacaoResource {

	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Autowired
	private PublicacaoService publicacaoService;
	
	@Autowired
	private PublicacaoRepository publicacaoRepository;
	
	@Autowired
	private MessageSource messageSource;
	
	@GetMapping
	public List<Publicacao> pesquisar(PublicacaoFilter publicacaoFilter) throws Exception {
		return publicacaoRepository.filtrar(publicacaoFilter);
	}
	
	@PostMapping
	public ResponseEntity<Publicacao> criar(@Valid Publicacao publicacao, 
			@RequestParam(required=false) MultipartFile partFile,
			@RequestParam String link, HttpServletResponse response) throws IOException {
		
		Publicacao publicacaoSalva = publicacaoService.adicionar(publicacao, partFile, link);
		
		publisher.publishEvent(new RecursoCriadoEvent(this, response, publicacaoSalva.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(publicacaoSalva);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Publicacao> buscarPeloId(@PathVariable Long id) {
		Optional<Publicacao> publicacaoOptional = publicacaoRepository.findById(id);
		
		return publicacaoOptional.isPresent() ? 
				ResponseEntity.ok(publicacaoOptional.get()) : ResponseEntity.notFound().build();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Publicacao> atualizar(@PathVariable Long id, 
			@RequestParam(required=false) MultipartFile partFile,
			@RequestParam(required=false) String link,
			@Valid Publicacao publicacao
			) throws IOException {
		
		Publicacao publicacaoSalva = publicacaoService.atualizar(id, publicacao, partFile, link);
		
		return ResponseEntity.ok(publicacaoSalva);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(code=HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long id) {
		publicacaoRepository.deleteById(id);
	}
	
	@ExceptionHandler(FonteInexistenteOuInativaException.class)
	public ResponseEntity<Object> handleFonteInexistenteOuInativaException(FonteInexistenteOuInativaException ex) {
		String mensagemUsuario = messageSource.getMessage("fonte.inexistente-ou-inativa", null, LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ex.toString();
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		
		return ResponseEntity.badRequest().body(erros);
	}
	
	
	@ExceptionHandler(FiltroPublicacaoDataInvalidaException.class)
	public ResponseEntity<Object> handleFiltroPublicacaoDataInvalidoException(FiltroPublicacaoDataInvalidaException ex) {
		String mensagemUsuario = messageSource.getMessage("publicacao.filtro.data.invalido", null, LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ex.toString();
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		
		return ResponseEntity.badRequest().body(erros);
	}

}
