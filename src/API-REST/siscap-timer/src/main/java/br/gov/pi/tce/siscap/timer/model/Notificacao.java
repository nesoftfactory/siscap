package br.gov.pi.tce.siscap.timer.model;

import java.util.List;

import br.gov.pi.tce.siscap.timer.model.enums.NotificacaoTipo;

public class Notificacao {

	private Long id;
	private NotificacaoTipo tipo;
	private List<Usuario> usuarios;
	private Publicacao publicacao;
	private String texto;

	public Notificacao(NotificacaoTipo tipo, List<Usuario> usuarios, Publicacao publicacao, String texto) {
		super();
		setTipo(tipo);
		setUsuarios(usuarios);
		setPublicacao(publicacao);
		setTexto(texto);
	}

	public Notificacao(Long id, NotificacaoTipo tipo, List<Usuario> usuarios, Publicacao publicacao, String texto) {
		this.id = id;
		setTipo(tipo);
		setUsuarios(usuarios);
		setPublicacao(publicacao);
		setTexto(texto);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public NotificacaoTipo getTipo() {
		return tipo;
	}

	public void setTipo(NotificacaoTipo tipo) {
		this.tipo = tipo;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public Publicacao getPublicacao() {
		return publicacao;
	}

	public void setPublicacao(Publicacao publicacao) {
		this.publicacao = publicacao;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

}
