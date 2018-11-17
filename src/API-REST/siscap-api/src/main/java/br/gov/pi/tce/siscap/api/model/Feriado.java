package br.gov.pi.tce.siscap.api.model;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="feriado")
public class Feriado extends BaseEntity {
	
	private String nome;
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private LocalDate data;
	private List<Fonte> fontes;
	private Boolean ativo;
	private Boolean fixo;
	private Boolean todasFontes;

	@NotNull
	@Size(min=3, max=50)
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@NotNull
	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	@NotNull
	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	@JsonIgnore
	@Transient
	public boolean isInativa( ) {
		return !this.ativo;
	}

	@ManyToMany
	@JoinTable(name="feriado_fonte"
				, joinColumns=@JoinColumn(name="id_feriado")
				, inverseJoinColumns=@JoinColumn(name="id_fonte"))
	public List<Fonte> getFontes() {
		return fontes;
	}

	public void setFontes(List<Fonte> fontes) {
		this.fontes = fontes;
	}

	public Boolean getFixo() {
		return fixo;
	}

	public void setFixo(Boolean fixo) {
		this.fixo = fixo;
	}

	@Column(name="todas_fontes")
	public Boolean getTodasFontes() {
		return todasFontes;
	}

	public void setTodasFontes(Boolean todasFontes) {
		this.todasFontes = todasFontes;
	}
	
}
