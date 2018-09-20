package br.gov.pi.tce.siscap.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import br.gov.pi.tce.siscap.api.model.Usuario;

@Transactional(readOnly=true)
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	//Optional<Usuario> findByCpf(String cpf);
	
	//@Query("SELECT u FROM Usuario u WHERE u.cpf = :cpf AND u.id != :idUsuario")
	//List<Usuario> buscarPorCpfComIdDiferenteDoInformado(@Param("cpf") String cpf, @Param("idUsuario") Long idUsuario);

	Optional<Usuario> findByLogin(String login);
	
	@Query("SELECT u FROM Usuario u WHERE u.login = :login AND u.id != :idUsuario")
	List<Usuario> buscarPorLoginComIdDiferenteDoInformado(@Param("login") String login, @Param("idUsuario") Long idUsuario);
}
