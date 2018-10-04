package br.gov.pi.tce.siscap.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.gov.pi.tce.siscap.api.model.Publicacao;
import br.gov.pi.tce.siscap.api.repository.publicacao.PublicacaoRepositoryQuery;

public interface PublicacaoRepository extends JpaRepository<Publicacao, Long>, PublicacaoRepositoryQuery {

}
