package br.gov.pi.tce.publicacoes.modelo;

/**
 * 
 * Classe responsável por representar a entidade Usuario.
 * 
 * 
 * @author Erick Guilherme Cavalcanti
 *
 */
public class UsuarioN {

	private Long id;
	private String nome;
	private String login;
	private Boolean admin = false;
	private Boolean ativo = false;

	/**
	 * 
	 */
	public UsuarioN() {
		super();
	}

	public UsuarioN(String nome, String login, Boolean admin, Boolean ativo) {
		super();
		setNome(nome);
		setLogin(login);
		setAdmin(admin);
		setAtivo(ativo);
	}

	public UsuarioN(Long id, String nome, String login, Boolean admin, Boolean ativo) {
		this.id = id;
		setNome(nome);
		setLogin(login);
		setAdmin(admin);
		setAtivo(ativo);
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @param nome the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * @param login the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * @return the admin
	 */
	public Boolean getAdmin() {
		return admin;
	}

	/**
	 * @param admin the admin to set
	 */
	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}

	/**
	 * @return the ativo
	 */
	public Boolean getAtivo() {
		return ativo;
	}

	/**
	 * @param ativo the ativo to set
	 */
	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
}
