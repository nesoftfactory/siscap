package modelo;

public class Usuario {
	
	private Long id;
	private String nome;
	private String email;
	private String login;
	
	
	
	
	public Usuario() {
		super();
	}

	public Usuario(String nome, String email, String login) {
		super();
		this.nome = nome;
		this.email = email;
		this.login = login;
	}



	public Usuario(Long id, String nome, String email, String login) {
		this(nome,email,login);
		this.id = id;
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
