package br.gov.pi.tce.siscap.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.gov.pi.tce.siscap.api.model.Feriado;
import br.gov.pi.tce.siscap.api.repository.feriado.FeriadoRepositoryQuery;

public interface FeriadoRepository extends JpaRepository<Feriado, Long>, FeriadoRepositoryQuery {

}
