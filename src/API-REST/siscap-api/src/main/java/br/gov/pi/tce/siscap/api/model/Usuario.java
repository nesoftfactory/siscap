package br.gov.pi.tce.siscap.api.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="usuario")
public class Usuario extends BaseEntity {
	
	private String nome;
	private String login;
	private Boolean admin = false;
	private Boolean ativo = false;
	
	
	
	public Usuario() {
		super();
	}
	
	
	public Usuario(String nome, String login, Boolean admin, Boolean ativo) {
		this.nome = nome;
		this.login = login;
		this.admin = admin;
		this.ativo = ativo;
	}
	
	
	
	public Usuario(Long id, String nome, String login, Boolean admin, Boolean ativo) {
		super(id);
		this.nome = nome;
		this.login = login;
		this.admin = admin;
		this.ativo = ativo;
	}




	@NotNull
	@Size(min=3, max=50)
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}

	@NotNull
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public Boolean getAdmin() {
		return admin;
	}

	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}

	@NotNull
	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
}
