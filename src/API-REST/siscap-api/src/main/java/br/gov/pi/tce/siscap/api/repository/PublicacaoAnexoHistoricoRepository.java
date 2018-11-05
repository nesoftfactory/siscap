package br.gov.pi.tce.siscap.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.gov.pi.tce.siscap.api.model.PublicacaoAnexoHistorico;

public interface PublicacaoAnexoHistoricoRepository extends JpaRepository<PublicacaoAnexoHistorico, Long> {
	
	@Query("SELECT new br.gov.pi.tce.siscap.api.model.PublicacaoAnexoHistorico(pah.mensagem, pah.sucesso, pah.dataCriacao, pah.usuarioCriacao)  FROM PublicacaoAnexoHistorico pah WHERE pah.publicacaoAnexo.id = :idPublicacaoAnexo")
	List<PublicacaoAnexoHistorico> buscarPeloIdAnexoPublicacao(@Param("idPublicacaoAnexo") Long idPublicacaoAnexo);

}
