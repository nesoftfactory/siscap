package br.gov.pi.tce.siscap.api.repository.publicacao;

import java.util.List;

import br.gov.pi.tce.siscap.api.model.Publicacao;
import br.gov.pi.tce.siscap.api.repository.filter.PublicacaoFilter;

public interface PublicacaoRepositoryQuery {
	
	public List<Publicacao> filtrar(PublicacaoFilter publicacaoFilter ) throws Exception;

}
