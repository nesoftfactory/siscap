package br.gov.pi.tce.siscap.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.gov.pi.tce.siscap.api.model.PaginaOCRArquivo;
import br.gov.pi.tce.siscap.api.model.Publicacao;
import br.gov.pi.tce.siscap.api.repository.paginaArquivoOcr.PaginaArquivoOCRRepositoryQuery;

public interface PaginaArquivoOCRRepository extends JpaRepository<PaginaOCRArquivo, Long>, PaginaArquivoOCRRepositoryQuery {


	@Query("SELECT new br.gov.pi.tce.siscap.api.model.Publicacao(p.id, p.situacao)  FROM Publicacao p WHERE p.situacao = :situacao AND p.fonte.id = :idFonte")
	public List<Publicacao> buscarPublicacoesAptasParaOCR(Long idFonte, String situacao);

	@Query("SELECT new br.gov.pi.tce.siscap.api.model.PaginaOCRArquivo(p.id)  FROM PaginaOCRArquivo p WHERE p.arquivo.id = :idArquivo AND p.pagina = :pagina")
	public PaginaOCRArquivo buscaPorPaginaDoArquivo(Long idArquivo, Integer pagina);
	
}
