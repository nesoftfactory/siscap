package br.gov.pi.tce.siscap.api.repository.feriado;

import java.util.List;

import br.gov.pi.tce.siscap.api.model.Feriado;
import br.gov.pi.tce.siscap.api.repository.filter.FeriadoFilter;

public interface FeriadoRepositoryQuery {
	
	public List<Feriado> filtrar(FeriadoFilter feriadoFilter);

}
