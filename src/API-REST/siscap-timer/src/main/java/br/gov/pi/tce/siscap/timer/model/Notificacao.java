package br.gov.pi.tce.siscap.timer.model;

import java.time.LocalDateTime;
import java.util.List;

import br.gov.pi.tce.siscap.timer.model.enums.NotificacaoTipo;

public class Notificacao {

	private Long id;
	private NotificacaoTipo tipo;
	private List<Usuario> usuarios;
	private Publicacao publicacao;
	private String texto;
	private LocalDateTime dataCriacao;
	private LocalDateTime dataAtualizacao;

	public Notificacao() {
		// TODO Auto-generated constructor stub
	}

	public Notificacao(NotificacaoTipo tipo, List<Usuario> usuarios, Publicacao publicacao, String texto) {
		super();
		setTipo(tipo);
		setUsuarios(usuarios);
		setPublicacao(publicacao);
		setTexto(texto);
		setDataCriacao(LocalDateTime.now());
		setDataAtualizacao(LocalDateTime.now());
	}

	public Notificacao(Long id, NotificacaoTipo tipo, List<Usuario> usuarios, Publicacao publicacao, String texto,
			String usuarioCriacaoString, String dataCriacaoString) {
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

	public LocalDateTime getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(LocalDateTime dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public LocalDateTime getDataAtualizacao() {
		return dataAtualizacao;
	}

	public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

}