package br.gov.pi.tce.siscap.api.repository.paginaArquivoOcr;

import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PostPersist;

import org.springframework.beans.factory.annotation.Autowired;

import br.gov.pi.tce.siscap.api.model.PaginaOCRArquivo;
import br.gov.pi.tce.siscap.api.repository.PaginaArquivoOCRRepository;

public class PaginaArquivoOCRRepositoryImpl implements PaginaArquivoOCRRepositoryQuery {
	
	@PersistenceContext
	private EntityManager manager;
	
	
	@Autowired
	private PaginaArquivoOCRRepository paginaArquivoOCRRepository;
	
	@Override
	@PostPersist
	public void gravarPaginasArquivo(List<PaginaOCRArquivo> paginasArquivo) {
		for (Iterator iterator = paginasArquivo.iterator(); iterator.hasNext();) {
			PaginaOCRArquivo paginaOCRArquivo = (PaginaOCRArquivo) iterator.next();
			if(paginaArquivoOCRRepository.buscaPorPaginaDoArquivo(paginaOCRArquivo.getArquivo().getId(), paginaOCRArquivo.getPagina()) == null) {
				paginaArquivoOCRRepository.save(paginaOCRArquivo);
			}
		}
	}


}
