package br.gov.pi.tce.siscap.api.resource;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.gov.pi.tce.siscap.api.model.Arquivo;
import br.gov.pi.tce.siscap.api.repository.ArquivoRepository;

@RestController
@RequestMapping("/arquivos")
public class ArquivoResource {

	
	@Autowired
	private ArquivoRepository arquivoRepository;
	
	@GetMapping("/{id}")
	public ResponseEntity<Arquivo> buscarPeloId(@PathVariable Long id) {
		Optional<Arquivo> arquivoOptional = arquivoRepository.findById(id);
		return arquivoOptional.isPresent() ? 
				ResponseEntity.ok(arquivoOptional.get()) : ResponseEntity.notFound().build();
	}
	
}
