package br.gov.pi.tce.siscap.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import br.gov.pi.tce.siscap.api.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	@Transactional(readOnly=true)
	Optional<Usuario> findByCpf(String cpf);
	
	@Transactional(readOnly=true)
	@Query("SELECT u FROM Usuario u WHERE u.cpf = :cpf AND u.id != :idUsuario")
	List<Usuario> buscarPorCpfComIdDiferenteDoInformado(@Param("cpf") String cpf, @Param("idUsuario") Long idUsuario);
}
