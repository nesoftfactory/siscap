package br.gov.pi.tce.publicacoes.modelo;

import java.util.List;

public class Notificacao {

	private Long id;
	private String tipo;
	private String texto;
	private String dataCriacaoString;
	private String usuarioCriacaoString;
	private List<Usuario> usuarios;
	private Publicacao publicacao;

	public Notificacao() {

	}

	public Notificacao(String tipo, String texto, String dataCriacaoString, String usuarioCriacaoString) {
		super();
		this.tipo = tipo;
		this.texto = texto;
		this.dataCriacaoString = dataCriacaoString;
		this.usuarioCriacaoString = usuarioCriacaoString;
	}

	public Notificacao(String tipo, String texto, List<Usuario> usuarios, Publicacao publicacao) {
		super();
		setTipo(tipo);
		setTexto(texto);
		setUsuarios(usuarios);
		setPublicacao(publicacao);
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

	public String getDataCriacaoString() {
		return dataCriacaoString;
	}

	public void setDataCriacaoString(String dataCriacaoString) {
		this.dataCriacaoString = dataCriacaoString;
	}

	public String getUsuarioCriacaoString() {
		return usuarioCriacaoString;
	}

	public void setUsuarioCriacaoString(String usuarioCriacaoString) {
		this.usuarioCriacaoString = usuarioCriacaoString;
	}

}
