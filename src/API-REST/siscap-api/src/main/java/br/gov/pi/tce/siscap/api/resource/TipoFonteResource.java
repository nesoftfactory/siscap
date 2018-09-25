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
import br.gov.pi.tce.siscap.api.model.TipoFonte;
import br.gov.pi.tce.siscap.api.repository.TipoFonteRepository;
import br.gov.pi.tce.siscap.api.service.TipoFonteService;
import br.gov.pi.tce.siscap.api.service.exception.TipoFonteComNomeJaExistenteException;

@RestController
@RequestMapping("/tiposfonte")
public class TipoFonteResource {

	@Autowired
	private TipoFonteRepository tipoFonteRepository;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Autowired
	private TipoFonteService tipoFonteService;
	
	@Autowired
	private MessageSource messageSource;
	
	@GetMapping
	public List<TipoFonte> listar() {
		return tipoFonteRepository.findAll();
	}
	
	@PostMapping
	public ResponseEntity<TipoFonte> criar(@Valid @RequestBody TipoFonte tipoFonte, HttpServletResponse response) {
		TipoFonte tipoFonteSalva = tipoFonteService.adicionar(tipoFonte);
		
		publisher.publishEvent(new RecursoCriadoEvent(this, response, tipoFonteSalva.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(tipoFonteSalva);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<TipoFonte> buscarPeloId(@PathVariable Long id) {
		Optional<TipoFonte> tipoFonteOptional = tipoFonteRepository.findById(id);
		
		return tipoFonteOptional.isPresent() ? ResponseEntity.ok(tipoFonteOptional.get()) : ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(code=HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long id) {
		tipoFonteRepository.deleteById(id);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<TipoFonte> atualizar(@PathVariable Long id, @Valid @RequestBody TipoFonte tipoFonte) {
		TipoFonte tipoFonteSalva = tipoFonteService.atualizar(id, tipoFonte);
		
		return ResponseEntity.ok(tipoFonteSalva);
	}
	
	@PutMapping("/{id}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void atualizarPropriedadeAtivo(@PathVariable Long id, @RequestBody Boolean ativo) {
		tipoFonteService.atualizarPropriedadeAtivo(id, ativo);
	}
	
	@ExceptionHandler(TipoFonteComNomeJaExistenteException.class)
	public ResponseEntity<Object> handleFonteComNomeJaExistenteException(TipoFonteComNomeJaExistenteException ex) {
		String mensagemTipoFonte = messageSource.getMessage("tipofonte.nome-ja-existente", null, LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ex.toString();
		List<Erro> erros = Arrays.asList(new Erro(mensagemTipoFonte, mensagemDesenvolvedor));
		
		return ResponseEntity.badRequest().body(erros);
	}
}
