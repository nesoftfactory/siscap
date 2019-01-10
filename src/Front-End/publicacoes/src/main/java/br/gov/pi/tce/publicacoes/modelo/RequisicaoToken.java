package br.gov.pi.tce.publicacoes.modelo;

/**
 * Classe responsável por representar a entidade RequisicaoToken.
 * 
 * @author Erick Guilherme Cavalcanti
 *
 */
public class RequisicaoToken {

	private String client;
	private String username;
	private String password;
	private String grant_type;

	public RequisicaoToken() {
		super();
	}

	public RequisicaoToken(String client, String username, String password, String grant_type) {
		super();
		setClient(client);
		setUsername(username);
		setPassword(password);
		setGrant_type(grant_type);
	}

	/**
	 * @return the client
	 */
	public String getClient() {
		return client;
	}

	/**
	 * @param client the client to set
	 */
	public void setClient(String client) {
		this.client = client;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the grant_type
	 */
	public String getGrant_type() {
		return grant_type;
	}

	/**
	 * @param grant_type the grant_type to set
	 */
	public void setGrant_type(String grant_type) {
		this.grant_type = grant_type;
	}
}