package br.gov.pi.tce.siscap.timer.model;

import java.time.LocalDate;
import java.util.List;

public class Feriado {

	private Long id;
	private String nome;
	private LocalDate data;
	private List<Fonte> fontes;
	private Boolean ativo;
	private Boolean todasFontes;
	private Boolean fixo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public List<Fonte> getFontes() {
		return fontes;
	}

	public void setFontes(List<Fonte> fontes) {
		this.fontes = fontes;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public Boolean getTodasFontes() {
		return todasFontes;
	}

	public void setTodasFontes(Boolean todasFontes) {
		this.todasFontes = todasFontes;
	}

	public Boolean getFixo() {
		return fixo;
	}

	public void setFixo(Boolean fixo) {
		this.fixo = fixo;
	}

}