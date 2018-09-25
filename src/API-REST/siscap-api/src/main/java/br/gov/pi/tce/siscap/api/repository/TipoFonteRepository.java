package br.gov.pi.tce.siscap.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.gov.pi.tce.siscap.api.model.TipoFonte;

public interface TipoFonteRepository extends JpaRepository<TipoFonte, Long> {
	
	Optional<TipoFonte> findByNome(String nome);
	
	@Query("SELECT tf FROM TipoFonte tf WHERE tf.nome = :nome AND tf.id != :idTipoFonte")
	List<TipoFonte> buscarPorNomeComIdDiferenteDoInformado(@Param("nome") String nome, @Param("idTipoFonte") Long idTipoFonte);

}
