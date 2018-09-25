package br.gov.pi.tce.siscap.api.model;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="publicacao")
public class Publicacao extends BaseEntity {
	
	private String nome;
	private Fonte fonte;
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private LocalDate data;
	private String codigo;
	private Arquivo arquivo;
	private Boolean sucesso;
	private Boolean possuiAnexo;
	private int quantidadeTentativas;

	@NotNull
	@Size(min=3, max=50)
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="id_fonte")
	public Fonte getFonte() {
		return fonte;
	}

	public void setFonte(Fonte fonte) {
		this.fonte = fonte;
	}

	@NotNull
	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	@ManyToOne(optional=true, cascade=CascadeType.ALL)
	@JoinColumn(name="id_arquivo")
	public Arquivo getArquivo() {
		return arquivo;
	}

	public void setArquivo(Arquivo arquivo) {
		this.arquivo = arquivo;
	}

	public Boolean isSucesso() {
		return sucesso;
	}

	public void setSucesso(Boolean sucesso) {
		this.sucesso = sucesso;
	}

	@Column(name="possui_anexo")
	public Boolean isPossuiAnexo() {
		return possuiAnexo;
	}

	public void setPossuiAnexo(Boolean possuiAnexo) {
		this.possuiAnexo = possuiAnexo;
	}

	@Column(name="quatidade_tentativas")
	public int getQuantidadeTentativas() {
		return quantidadeTentativas;
	}

	public void setQuantidadeTentativas(int quantidadeTentativas) {
		this.quantidadeTentativas = quantidadeTentativas;
	}

}
