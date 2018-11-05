package br.gov.pi.tce.siscap.api.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import br.gov.pi.tce.siscap.api.model.enums.NotificacaoTipo;

@Entity
@Table(name="notificacao")
public class Notificacao extends BaseEntity {
	
	private NotificacaoTipo tipo;
	private List<Usuario> usuarios;
	private Publicacao publicacao;
	private String texto;

	@NotNull
	@Enumerated(EnumType.STRING)
	public NotificacaoTipo getTipo() {
		return tipo;
	}
	
	public void setTipo(NotificacaoTipo tipo) {
		this.tipo = tipo;
	}
	
	@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
	@JsonIdentityReference(alwaysAsId=true)
	@NotNull
	@ManyToOne
	@JoinColumn(name="id_publicacao")
	public Publicacao getPublicacao() {
		return publicacao;
	}
	
	public void setPublicacao(Publicacao publicacao) {
		this.publicacao = publicacao;
	}
	
	@NotNull
	@Size(min=3)
	public String getTexto() {
		return texto;
	}
	
	public void setTexto(String texto) {
		this.texto = texto;
	}
	@ManyToMany
	@JoinTable(name="notificacao_usuarios"
				, joinColumns=@JoinColumn(name="id_notificacao")
				, inverseJoinColumns=@JoinColumn(name="id_usuario"))
	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}


}
