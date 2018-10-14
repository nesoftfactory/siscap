package br.gov.pi.tce.siscap.api.resource;

import java.time.LocalDate;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.gov.pi.tce.siscap.api.event.RecursoCriadoEvent;
import br.gov.pi.tce.siscap.api.exceptionhandler.SiscapExceptionHandler.Erro;
import br.gov.pi.tce.siscap.api.model.Feriado;
import br.gov.pi.tce.siscap.api.repository.FeriadoRepository;
import br.gov.pi.tce.siscap.api.repository.filter.FeriadoFilter;
import br.gov.pi.tce.siscap.api.service.FeriadoService;
import br.gov.pi.tce.siscap.api.service.exception.FonteInexistenteOuInativaException;

@RestController
@RequestMapping("/feriados")
public class FeriadoResource {

	@Autowired
	private FeriadoRepository feriadoRepository;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Autowired
	private FeriadoService feriadoService;
	
	@Autowired
	private MessageSource messageSource;
	
	@GetMapping
	public List<Feriado> pesquisar(FeriadoFilter feriadoFilter) {
		return feriadoRepository.filtrar(feriadoFilter);
	}
	
	@PostMapping
	public ResponseEntity<Feriado> criar(@Valid @RequestBody Feriado feriado, HttpServletResponse response) {
		Feriado feriadoSalvo = feriadoService.adicionar(feriado);
		
		publisher.publishEvent(new RecursoCriadoEvent(this, response, feriadoSalvo.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(feriadoSalvo);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Feriado> buscarPeloId(@PathVariable Long id) {
		Optional<Feriado> feriadoOptional = feriadoRepository.findById(id);
		
		return feriadoOptional.isPresent() ? 
				ResponseEntity.ok(feriadoOptional.get()) : ResponseEntity.notFound().build();
	}
	
	@GetMapping("/verifica/{data}")
	public ResponseEntity<Boolean> isFeriado(@PathVariable LocalDate data, 
			@RequestParam Long idFonte) {
		List<Long> ids = feriadoRepository.buscarPorDataEFonte(data, idFonte);
		
		return ResponseEntity.ok(!ids.isEmpty());
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(code=HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long id) {
		feriadoRepository.deleteById(id);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Feriado> atualizar(@PathVariable Long id, @Valid @RequestBody Feriado feriado) {
		Feriado feriadoSalvo = feriadoService.atualizar(id, feriado);
		
		return ResponseEntity.ok(feriadoSalvo);
	}
	
	@PutMapping("/{id}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void atualizarPropriedadeAtivo(@PathVariable Long id, @RequestBody Boolean ativo) {
		feriadoService.atualizarPropriedadeAtivo(id, ativo);
	}
	
	@ExceptionHandler(FonteInexistenteOuInativaException.class)
	public ResponseEntity<Object> handleFonteInexistenteOuInativaException(FonteInexistenteOuInativaException ex) {
		String mensagemUsuario = messageSource.getMessage("fonte.inexistente-ou-inativa", null, LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ex.toString();
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		
		return ResponseEntity.badRequest().body(erros);
	}
}
