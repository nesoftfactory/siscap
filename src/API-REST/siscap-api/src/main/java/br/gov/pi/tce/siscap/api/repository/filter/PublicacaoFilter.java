package br.gov.pi.tce.siscap.api.repository.filter;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

public class PublicacaoFilter {

	private Long idFonte;
	private String nome;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private LocalDate data;
	
	public Long getIdFonte() {
		return idFonte;
	}
	
	public void setIdFonte(Long idFonte) {
		this.idFonte = idFonte;
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public LocalDate getData() {
		return data;
	}
	
	public void setData(LocalDate data) {
		this.data = data;
	}
}
