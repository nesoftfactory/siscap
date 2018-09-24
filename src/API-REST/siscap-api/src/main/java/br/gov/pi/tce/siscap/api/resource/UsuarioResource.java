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
import br.gov.pi.tce.siscap.api.model.Usuario;
import br.gov.pi.tce.siscap.api.repository.UsuarioRepository;
import br.gov.pi.tce.siscap.api.service.UsuarioService;
import br.gov.pi.tce.siscap.api.service.exception.UsuarioComLoginJaExistenteException;

@RestController
@RequestMapping("/usuarios")
public class UsuarioResource {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private MessageSource messageSource;
	
	@GetMapping
	public List<Usuario> listar() {
		return usuarioRepository.findAll();
	}
	
	@PostMapping
	public ResponseEntity<Usuario> criar(@Valid @RequestBody Usuario usuario, HttpServletResponse response) {
		Usuario usuarioSalvo = usuarioService.adicionar(usuario);
		
		publisher.publishEvent(new RecursoCriadoEvent(this, response, usuarioSalvo.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(usuarioSalvo);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Usuario> buscarPeloId(@PathVariable Long id) {
		Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
		
		return usuarioOptional.isPresent() ? ResponseEntity.ok(usuarioOptional.get()) : ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(code=HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long id) {
		usuarioRepository.deleteById(id);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Usuario> atualizar(@PathVariable Long id, @Valid @RequestBody Usuario usuario) {
		Usuario usuarioSalvo = usuarioService.atualizar(id, usuario);
		
		return ResponseEntity.ok(usuarioSalvo);
	}
	
	@PutMapping("/{id}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void atualizarPropriedadeAtivo(@PathVariable Long id, @RequestBody Boolean ativo) {
		usuarioService.atualizarPropriedadeAtivo(id, ativo);
	}
	
	@PutMapping("/{id}/admin")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void atualizarPropriedadeAdmin(@PathVariable Long id, @RequestBody Boolean admin) {
		usuarioService.atualizarPropriedadeAdmin(id, admin);
	}
	
//	@ExceptionHandler(UsuarioComCpfJaExistenteException.class)
//	public ResponseEntity<Object> handleUsuarioComCpfJaExistenteException(UsuarioComCpfJaExistenteException ex) {
//		String mensagemUsuario = messageSource.getMessage("usuario.cpf-ja-existente", null, LocaleContextHolder.getLocale());
//		String mensagemDesenvolvedor = ex.toString();
//		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
//		
//		return ResponseEntity.badRequest().body(erros);
//	}

	@ExceptionHandler(UsuarioComLoginJaExistenteException.class)
	public ResponseEntity<Object> handleUsuarioComLoginJaExistenteException(UsuarioComLoginJaExistenteException ex) {
		String mensagemUsuario = messageSource.getMessage("usuario.login-ja-existente", null, LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ex.toString();
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		
		return ResponseEntity.badRequest().body(erros);
	}
}
