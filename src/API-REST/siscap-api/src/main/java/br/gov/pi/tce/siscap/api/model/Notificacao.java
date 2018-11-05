package br.gov.pi.tce.siscap.api.model;

import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
	
	
	@Transient
	public String getUsuarioCriacaoString() {
		return getUsuarioCriacao() != null ? getUsuarioCriacao().getNome() : "";
	}
	
	@Transient
	public String getDataCriacaoString() {
		if(getDataCriacao() != null) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	        String dataCriacaoFormatada = getDataCriacao().format(formatter);
			return dataCriacaoFormatada;
		}
		else {
			return "";
		}
		
	}



}
