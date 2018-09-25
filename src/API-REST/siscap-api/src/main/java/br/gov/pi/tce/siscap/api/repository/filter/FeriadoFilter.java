package br.gov.pi.tce.siscap.api.repository.filter;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

public class FeriadoFilter {

	private Long idFonte;

	@DateTimeFormat(pattern="yyyy-MM-dd")
	private LocalDate periodoDe;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private LocalDate periodoAte;
	
	public Long getIdFonte() {
		return idFonte;
	}
	
	public void setIdFonte(Long idFonte) {
		this.idFonte = idFonte;
	}
	
	public LocalDate getPeriodoDe() {
		return periodoDe;
	}
	
	public void setPeriodoDe(LocalDate periodoDe) {
		this.periodoDe = periodoDe;
	}
	
	public LocalDate getPeriodoAte() {
		return periodoAte;
	}
	
	public void setPeriodoAte(LocalDate periodoAte) {
		this.periodoAte = periodoAte;
	}
}
