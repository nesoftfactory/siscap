package br.gov.pi.tce.siscap.api.repository.paginaArquivoOcr;

import java.util.List;

import br.gov.pi.tce.siscap.api.model.PaginaOCRArquivo;

public interface PaginaArquivoOCRRepositoryQuery {
	
	public void gravarPaginasArquivo(List<PaginaOCRArquivo> paginasArquivo);
	

}
