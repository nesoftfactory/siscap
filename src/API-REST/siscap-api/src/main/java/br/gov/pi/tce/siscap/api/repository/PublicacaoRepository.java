package br.gov.pi.tce.siscap.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.gov.pi.tce.siscap.api.model.Publicacao;
import br.gov.pi.tce.siscap.api.repository.publicacao.PublicacaoRepositoryQuery;

public interface PublicacaoRepository extends JpaRepository<Publicacao, Long>, PublicacaoRepositoryQuery {


	@Query("SELECT new br.gov.pi.tce.siscap.api.model.Publicacao(p.id, p.situacao, p.fonte.id, p.fonte.nome, p.data)  FROM Publicacao p WHERE p.situacao = :situacao AND p.fonte.id = :idFonte")
	public List<Publicacao> buscarPublicacoesAptasParaOCR(Long idFonte, String situacao);
}
