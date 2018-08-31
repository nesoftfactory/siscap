package br.com.nesoftware.rest.publicacao.representation;

public class UsuarioRepresentation {
	
	private Long id;
	private String nome;
	private String email;
	private String login;
	
	
	
	
	
	public UsuarioRepresentation(Long id, String nome, String email, String login) {
		super();
		this.id = id;
		this.nome = nome;
		this.email = email;
		this.login = login;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((nome == null) ? 0 : nome.hashCode());
		result = prime * result
				+ ((email == null) ? 0 : email.hashCode());
		result = prime * result
				+ ((login == null) ? 0 : login.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UsuarioRepresentation other = (UsuarioRepresentation) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!login.equals(other.login))
			return false;
		return true;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	
	
	

}
