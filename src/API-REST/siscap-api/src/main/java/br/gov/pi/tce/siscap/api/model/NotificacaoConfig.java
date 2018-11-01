package br.gov.pi.tce.siscap.api.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.gov.pi.tce.siscap.api.model.enums.NotificacaoTipo;

@Entity
@Table(name="notificacao_config")
public class NotificacaoConfig extends BaseEntity {
	
	private NotificacaoTipo tipo;
	private List<Usuario> usuarios;
	private Boolean ativo;
	private int quantidadeTentativas;

	@NotNull
	@Enumerated(EnumType.STRING)
	public NotificacaoTipo getTipo() {
		return tipo;
	}
	
	public void setTipo(NotificacaoTipo tipo) {
		this.tipo = tipo;
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
	@JoinTable(name="notificacao_config_usuarios"
				, joinColumns=@JoinColumn(name="id_notificacao_config")
				, inverseJoinColumns=@JoinColumn(name="id_usuario"))
	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	@Column(name="qt_tentativas")
	public int getQuantidadeTentativas() {
		return quantidadeTentativas;
	}

	public void setQuantidadeTentativas(int quantidadeTentativas) {
		this.quantidadeTentativas = quantidadeTentativas;
	}
	
}
