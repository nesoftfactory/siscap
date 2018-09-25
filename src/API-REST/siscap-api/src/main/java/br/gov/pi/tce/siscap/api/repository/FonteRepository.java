package br.gov.pi.tce.siscap.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.gov.pi.tce.siscap.api.model.Fonte;

public interface FonteRepository extends JpaRepository<Fonte, Long> {
	
	Optional<Fonte> findByNome(String nome);
	
	@Query("SELECT f FROM Fonte f WHERE f.nome = :nome AND f.id != :idFonte")
	List<Fonte> buscarPorNomeComIdDiferenteDoInformado(@Param("nome") String nome, @Param("idFonte") Long idFonte);

}
