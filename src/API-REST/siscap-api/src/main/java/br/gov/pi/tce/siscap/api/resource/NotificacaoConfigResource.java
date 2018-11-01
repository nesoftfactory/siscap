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
import br.gov.pi.tce.siscap.api.model.NotificacaoConfig;
import br.gov.pi.tce.siscap.api.repository.NotificacaoConfigRepository;
import br.gov.pi.tce.siscap.api.repository.filter.NotificacaoConfigFilter;
import br.gov.pi.tce.siscap.api.service.NotificacaoConfigService;
import br.gov.pi.tce.siscap.api.service.exception.FonteInexistenteOuInativaException;

@RestController
@RequestMapping("/notificacoesconfig")
public class NotificacaoConfigResource {

	@Autowired
	private NotificacaoConfigRepository notificacaoConfigRepository;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Autowired
	private NotificacaoConfigService notificacaoConfigService;
	
	@Autowired
	private MessageSource messageSource;
	
	@GetMapping
	public List<NotificacaoConfig> pesquisar(NotificacaoConfigFilter filter) {
		return notificacaoConfigRepository.filtrar(filter);
	}
	
	@PostMapping
	public ResponseEntity<NotificacaoConfig> criar(@Valid @RequestBody NotificacaoConfig notificacaoConfig, 
			HttpServletResponse response) {
		NotificacaoConfig notificacaoConfigSalvo = notificacaoConfigService.adicionar(notificacaoConfig);
		
		publisher.publishEvent(new RecursoCriadoEvent(this, response, notificacaoConfigSalvo.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(notificacaoConfigSalvo);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<NotificacaoConfig> buscarPeloId(@PathVariable Long id) {
		Optional<NotificacaoConfig> notificacaoConfigOptional = notificacaoConfigRepository.findById(id);
		
		return notificacaoConfigOptional.isPresent() ? 
				ResponseEntity.ok(notificacaoConfigOptional.get()) : ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(code=HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long id) {
		notificacaoConfigRepository.deleteById(id);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<NotificacaoConfig> atualizar(@PathVariable Long id, @Valid @RequestBody NotificacaoConfig notificacaoConfig) {
		NotificacaoConfig notificacaoConfigSalvo = notificacaoConfigService.atualizar(id, notificacaoConfig);
		
		return ResponseEntity.ok(notificacaoConfigSalvo);
	}
	
	@PutMapping("/{id}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void atualizarPropriedadeAtivo(@PathVariable Long id, @RequestBody Boolean ativo) {
		notificacaoConfigService.atualizarPropriedadeAtivo(id, ativo);
	}
	
	@ExceptionHandler(FonteInexistenteOuInativaException.class)
	public ResponseEntity<Object> handleFonteInexistenteOuInativaException(FonteInexistenteOuInativaException ex) {
		String mensagemUsuario = messageSource.getMessage("fonte.inexistente-ou-inativa", null, LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ex.toString();
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		
		return ResponseEntity.badRequest().body(erros);
	}
}
