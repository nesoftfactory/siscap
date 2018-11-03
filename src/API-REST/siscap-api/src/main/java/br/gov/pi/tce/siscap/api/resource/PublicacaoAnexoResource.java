package br.gov.pi.tce.siscap.api.resource;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.gov.pi.tce.siscap.api.event.RecursoCriadoEvent;
import br.gov.pi.tce.siscap.api.model.PublicacaoAnexo;
import br.gov.pi.tce.siscap.api.repository.PublicacaoAnexoRepository;
import br.gov.pi.tce.siscap.api.service.PublicacaoAnexoService;

@RestController
@RequestMapping("/publicacoes_anexos")
public class PublicacaoAnexoResource {

	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Autowired
	private PublicacaoAnexoService publicacaoAnexoService;
	
	@Autowired
	private PublicacaoAnexoRepository publicacaoAnexoRepository;
	
	@GetMapping
	public List<PublicacaoAnexo> listar() {
		return publicacaoAnexoRepository.findAll();
	}
	
	@PostMapping
	public ResponseEntity<PublicacaoAnexo> criar(@Valid PublicacaoAnexo publicacaoAnexo, @RequestParam MultipartFile partFile,
			@RequestParam String link, HttpServletResponse response) throws IOException {
		
		PublicacaoAnexo publicacaoAnexoSalva = publicacaoAnexoService.adicionar(publicacaoAnexo, partFile, link);
		
		publisher.publishEvent(new RecursoCriadoEvent(this, response, publicacaoAnexoSalva.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(publicacaoAnexoSalva);
	}

	@GetMapping("/{id}")
	public ResponseEntity<PublicacaoAnexo> buscarPeloId(@PathVariable Long id) {
		Optional<PublicacaoAnexo> publicacaoAnexoOptional = publicacaoAnexoRepository.findById(id);
		
		return publicacaoAnexoOptional.isPresent() ? 
				ResponseEntity.ok(publicacaoAnexoOptional.get()) : ResponseEntity.notFound().build();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<PublicacaoAnexo> atualizar(@PathVariable Long id, @Valid @RequestPart PublicacaoAnexo publicacaoAnexo, 
			@RequestParam(required=false) MultipartFile partFile,
			@RequestParam String link) throws IOException {
		PublicacaoAnexo publicacaoAnexoSalva = publicacaoAnexoService.atualizar(id, publicacaoAnexo, partFile, link);
		
		return ResponseEntity.ok(publicacaoAnexoSalva);
	}
	
	
	@GetMapping("/{id}/publicacao")
	public ResponseEntity<PublicacaoAnexo> buscarPeloIdPublicacao(@PathVariable Long id) {
		List<PublicacaoAnexo> listaPublicacaoAnexoOptional = publicacaoAnexoRepository.buscarPorIdPublicacao(id);
		if(listaPublicacaoAnexoOptional != null && !listaPublicacaoAnexoOptional.isEmpty()) {
			PublicacaoAnexo pa = listaPublicacaoAnexoOptional.get(0);
			return ResponseEntity.status(HttpStatus.OK).body(pa);
			
		}
		else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(code=HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long id) {
		publicacaoAnexoRepository.deleteById(id);
	}
	
}
