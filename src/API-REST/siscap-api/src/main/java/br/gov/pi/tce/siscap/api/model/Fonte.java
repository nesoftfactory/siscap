package br.gov.pi.tce.siscap.api.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="fonte")
public class Fonte extends BaseEntity {
	
	private String nome;
	private String url;
	private TipoFonte tipoFonte;
	private Boolean ativo;

	@NotNull
	@Size(min=3, max=50)
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@NotNull
	@ManyToOne
	@JoinColumn(name="id_tipo_fonte")
	public TipoFonte getTipoFonte() {
		return tipoFonte;
	}

	public void setTipoFonte(TipoFonte tipoFonte) {
		this.tipoFonte = tipoFonte;
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
}
