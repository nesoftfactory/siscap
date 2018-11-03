package br.gov.pi.tce.siscap.api.resource;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.gov.pi.tce.siscap.api.event.RecursoCriadoEvent;
import br.gov.pi.tce.siscap.api.model.Notificacao;
import br.gov.pi.tce.siscap.api.repository.NotificacaoRepository;
import br.gov.pi.tce.siscap.api.repository.filter.NotificacaoFilter;
import br.gov.pi.tce.siscap.api.service.NotificacaoService;

@RestController
@RequestMapping("/notificacoes")
public class NotificacaoResource {

	@Autowired
	private NotificacaoRepository notificacaoRepository;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Autowired
	private NotificacaoService notificacaoService;
	
	@GetMapping
	public List<Notificacao> pesquisar(NotificacaoFilter filter) {
		return notificacaoRepository.filtrar(filter);
	}
	
	@PostMapping
	public ResponseEntity<Notificacao> criar(@Valid @RequestBody Notificacao notificacao, 
			HttpServletResponse response) {
		Notificacao notificacaoSalvo = notificacaoService.adicionar(notificacao);
		
		publisher.publishEvent(new RecursoCriadoEvent(this, response, notificacaoSalvo.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(notificacaoSalvo);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Notificacao> buscarPeloId(@PathVariable Long id) {
		Optional<Notificacao> notificacaoOptional = notificacaoRepository.findById(id);
		
		return notificacaoOptional.isPresent() ? 
				ResponseEntity.ok(notificacaoOptional.get()) : ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(code=HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long id) {
		notificacaoRepository.deleteById(id);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Notificacao> atualizar(@PathVariable Long id, @Valid @RequestBody Notificacao notificacao) {
		Notificacao notificacaoSalvo = notificacaoService.atualizar(id, notificacao);
		
		return ResponseEntity.ok(notificacaoSalvo);
	}
	
}
