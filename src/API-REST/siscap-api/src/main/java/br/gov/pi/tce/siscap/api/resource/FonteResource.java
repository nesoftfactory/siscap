package br.gov.pi.tce.siscap.api.resource;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.gov.pi.tce.siscap.api.event.RecursoCriadoEvent;
import br.gov.pi.tce.siscap.api.exceptionhandler.SiscapExceptionHandler.Erro;
import br.gov.pi.tce.siscap.api.model.Fonte;
import br.gov.pi.tce.siscap.api.repository.FonteRepository;
import br.gov.pi.tce.siscap.api.service.FonteService;
import br.gov.pi.tce.siscap.api.service.exception.FonteComNomeJaExistenteException;
import br.gov.pi.tce.siscap.api.service.exception.TipoFonteInexistenteOuInativaException;

@RestController
@RequestMapping("/fontes")
public class FonteResource {

	@Autowired
	private FonteRepository fonteRepository;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Autowired
	private FonteService fonteService;
	
	@Autowired
	private MessageSource messageSource;
	
	@GetMapping
	public List<Fonte> listar() {
		return fonteRepository.findAll();
	}
	
	@PostMapping
	public ResponseEntity<Fonte> criar(@Valid @RequestBody Fonte fonte, HttpServletResponse response) {
		Fonte fonteSalva = fonteService.adicionar(fonte);
		
		publisher.publishEvent(new RecursoCriadoEvent(this, response, fonteSalva.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(fonteSalva);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Fonte> buscarPeloId(@PathVariable Long id) {
		Optional<Fonte> fonteOptional = fonteRepository.findById(id);
		
		return fonteOptional.isPresent() ? ResponseEntity.ok(fonteOptional.get()) : ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(code=HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long id) {
		fonteRepository.deleteById(id);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Fonte> atualizar(@PathVariable Long id, @Valid @RequestBody Fonte fonte) {
		Fonte fonteSalva = fonteService.atualizar(id, fonte);
		
		return ResponseEntity.ok(fonteSalva);
	}
	
	@PutMapping("/{id}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void atualizarPropriedadeAtivo(@PathVariable Long id, @RequestBody Boolean ativo) {
		fonteService.atualizarPropriedadeAtivo(id, ativo);
	}
	
	@ExceptionHandler(TipoFonteInexistenteOuInativaException.class)
	public ResponseEntity<Object> handleTipoFonteInexistenteOuInativaException(TipoFonteInexistenteOuInativaException ex) {
		String mensagemUsuario = messageSource.getMessage("tipofonte.inexistente-ou-inativa", null, LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ex.toString();
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		
		return ResponseEntity.badRequest().body(erros);
	}
	
	@ExceptionHandler(FonteComNomeJaExistenteException.class)
	public ResponseEntity<Object> handleFonteComNomeJaExistenteException(FonteComNomeJaExistenteException ex) {
		String mensagemFonte = messageSource.getMessage("fonte.nome-ja-existente", null, LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ex.toString();
		List<Erro> erros = Arrays.asList(new Erro(mensagemFonte, mensagemDesenvolvedor));
		
		return ResponseEntity.badRequest().body(erros);
	}
}
