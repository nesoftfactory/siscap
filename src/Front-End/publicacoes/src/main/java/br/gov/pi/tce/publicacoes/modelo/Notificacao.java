package br.gov.pi.tce.publicacoes.modelo;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="notificacao")
public class Notificacao{
	
	private Long id;
	private String tipo;
	private String texto;
	private String dataCriacao;
	private Usuario usuarioCriacao;
	private List<Usuario> usuarios;
	private Publicacao publicacao;
	
	
	public Notificacao() {
		
	}
	
	
	
	
	public Notificacao(String tipo, String texto, String dataCriacao, Usuario usuarioCriacao) {
		super();
		this.tipo = tipo;
		this.texto = texto;
		this.dataCriacao = dataCriacao;
		this.usuarioCriacao = usuarioCriacao;
	}
	
	
	
	
	public Publicacao getPublicacao() {
		return publicacao;
	}




	public void setPublicacao(Publicacao publicacao) {
		this.publicacao = publicacao;
	}




	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	public String getDataCriacao() {
		return dataCriacao;
	}
	public void setDataCriacao(String dataCriacao) {
		this.dataCriacao = dataCriacao;
	}
	public Usuario getUsuarioCriacao() {
		return usuarioCriacao;
	}
	public void setUsuarioCriacao(Usuario usuarioCriacao) {
		this.usuarioCriacao = usuarioCriacao;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public List<Usuario> getUsuarios() {
		return usuarios;
	}
	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}
	
	
	
	

	
}
