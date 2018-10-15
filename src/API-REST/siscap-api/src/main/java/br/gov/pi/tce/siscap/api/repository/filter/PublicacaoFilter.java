package br.gov.pi.tce.siscap.api.repository.filter;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

public class PublicacaoFilter {

	private Long idFonte;
	private String nome;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private LocalDate data;
	
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private LocalDate dataInicio;
	
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private LocalDate dataFim;
	
	private Boolean sucesso;
	
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

	public LocalDate getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(LocalDate dataInicio) {
		this.dataInicio = dataInicio;
	}

	public LocalDate getDataFim() {
		return dataFim;
	}

	public void setDataFim(LocalDate dataFim) {
		this.dataFim = dataFim;
	}

	public Boolean getSucesso() {
		return sucesso;
	}

	public void setSucesso(Boolean sucesso) {
		this.sucesso = sucesso;
	}
	
	
}
