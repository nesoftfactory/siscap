package br.gov.pi.tce.siscap.api.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.gov.pi.tce.siscap.api.model.PublicacaoAnexoHistorico;
import br.gov.pi.tce.siscap.api.repository.PublicacaoAnexoHistoricoRepository;

@RestController
@RequestMapping("/historico_anexo_publicacao")
public class PublicacaoAnexoHistoricoResource {

	
	@Autowired
	private PublicacaoAnexoHistoricoRepository publicacaoAnexoHistoricoRepository;
	
	@GetMapping("/{id}")
	public List<PublicacaoAnexoHistorico> buscarPeloIdPublicacao(@PathVariable Long id) {
		List<PublicacaoAnexoHistorico> lista = publicacaoAnexoHistoricoRepository.buscarPeloIdAnexoPublicacao(id);
		return lista;
	}
}
