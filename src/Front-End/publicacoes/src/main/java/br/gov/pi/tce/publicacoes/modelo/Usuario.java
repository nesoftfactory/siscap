package br.gov.pi.tce.publicacoes.modelo;

import java.io.Serializable;

import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;


public class Usuario implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String nome;
	private String login;
	private Boolean admin = false;
	private Boolean ativo = false;
	private String cpf;
	
	
	
	public Usuario() {
		super();
	}

	public Usuario(String nome, String login,  boolean ativo, boolean admin ) {
		super();
		this.nome = nome;
		this.login = login;
		this.ativo = ativo;
		this.admin = admin;
	}



	public Usuario(Long id, String nome, String login,  boolean ativo, boolean admin) {
		this(nome,login,ativo,admin);
		this.id = id;
	}
	
	
	

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
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
	
	@JsonIgnore
	public String getTextoAdmin() {
		return getAdmin() ? "Sim" : "Não";
	}
	
	@JsonIgnore
	public String getTextoAtivo() {
		return getAtivo() ? "Sim" : "Não";
	}
	
	
	

}
