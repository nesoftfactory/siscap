package br.gov.pi.tce.publicacoes.modelo;

/**
 * Classe responsável por representar a entidade RespostaToken.
 * 
 * @author Erick Guilherme Cavalcanti
 *
 */
public class RespostaToken {

	private String access_token;
	private String token_type;
	private int expires_in;
	private String scope;
	private String jti;

	public RespostaToken() {
		super();
	}

	public RespostaToken(String access_token, String token_type, int expires_in, String scope, String jti) {
		super();
		setAccess_token(access_token);
		setToken_type(token_type);
		setExpires_in(expires_in);
		setScope(scope);
		setJti(jti);
	}

	/**
	 * @return the access_token
	 */
	public String getAccess_token() {
		return access_token;
	}

	/**
	 * @param access_token the access_token to set
	 */
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	/**
	 * @return the token_type
	 */
	public String getToken_type() {
		return token_type;
	}

	/**
	 * @param token_type the token_type to set
	 */
	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}

	/**
	 * @return the expires_in
	 */
	public int getExpires_in() {
		return expires_in;
	}

	/**
	 * @param expires_in the expires_in to set
	 */
	public void setExpires_in(int expires_in) {
		this.expires_in = expires_in;
	}

	/**
	 * @return the scope
	 */
	public String getScope() {
		return scope;
	}

	/**
	 * @param scope the scope to set
	 */
	public void setScope(String scope) {
		this.scope = scope;
	}

	/**
	 * @return the jti
	 */
	public String getJti() {
		return jti;
	}

	/**
	 * @param jti the jti to set
	 */
	public void setJti(String jti) {
		this.jti = jti;
	}
}