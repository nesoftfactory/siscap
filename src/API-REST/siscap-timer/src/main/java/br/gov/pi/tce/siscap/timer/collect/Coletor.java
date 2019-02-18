package br.gov.pi.tce.siscap.timer.collect;

import java.util.Date;

import br.gov.pi.tce.siscap.timer.model.Fonte;
import br.gov.pi.tce.siscap.timer.service.ColetorService;

public interface Coletor {

	void coletar(ColetorService coletorService, Fonte fonte, Date dataInicial, Date dataFinal);
}
