package br.gov.pi.tce.siscap.timer.enums;

import br.gov.pi.tce.siscap.timer.collect.Coletor;
import br.gov.pi.tce.siscap.timer.collect.MunicipiosColetor;
import br.gov.pi.tce.siscap.timer.collect.ParnaibaColetor;
import br.gov.pi.tce.siscap.timer.collect.PiauiColetor;
import br.gov.pi.tce.siscap.timer.collect.TeresinaColetor;

public enum FONTE_COLETA_AUTOMATICA {
	TERESINA(new TeresinaColetor()),
	PARNAIBA(new ParnaibaColetor()),
	MUNICIPIOS(new MunicipiosColetor()),
	PIAUI(new PiauiColetor());
	
	private Coletor coletor;
	
	private FONTE_COLETA_AUTOMATICA(Coletor coletor) {
		this.coletor = coletor;
	}
	
	public Coletor getColetor() {
		return coletor;
	}
}
