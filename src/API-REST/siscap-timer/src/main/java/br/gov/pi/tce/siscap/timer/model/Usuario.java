package br.gov.pi.tce.siscap.timer.model;

import java.io.Serializable;

public class Usuario implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Long id;
	private String nome;
	private String login;
	private Boolean admin = false;
	private Boolean ativo = false;
	private Usuario usuarioCriacao;
	private Usuario usuarioAtualizacao;
	private String email;

	public Usuario() {
		super();
	}

	public Usuario(String nome, String login, boolean ativo, boolean admin, String email) {
		super();
		this.nome = nome;
		this.login = login;
		this.ativo = ativo;
		this.admin = admin;
	}

	public Usuario(Long id, String nome, String login, boolean ativo, boolean admin, String email) {
		this(nome, login, ativo, admin, email);
		this.id = id;
	}

	public Boolean getAdmin() {
		return admin;
	}

	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public Usuario getUsuarioCriacao() {
		return usuarioCriacao;
	}

	public void setUsuarioCriacao(Usuario usuarioCriacao) {
		this.usuarioCriacao = usuarioCriacao;
	}

	public Usuario getUsuarioAtualizacao() {
		return usuarioAtualizacao;
	}

	public void setUsuarioAtualizacao(Usuario usuarioAtualizacao) {
		this.usuarioAtualizacao = usuarioAtualizacao;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
