package br.gov.pi.tce.publicacoes.autenticacao;

import java.io.Serializable;
import java.security.Key;
import java.util.List;

import javax.crypto.spec.SecretKeySpec;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import br.gov.pi.tce.publicacoes.controller.beans.SegurancaController;
import br.gov.pi.tce.publicacoes.modelo.RespostaToken;
import br.gov.pi.tce.publicacoes.util.Propriedades;
import br.gov.pi.tce.publicacoes.util.SessionUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RequestScoped
@ManagedBean
public class AutenticadorBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String login;
	private String senha;

	@Inject
	private SegurancaController segurancaController;

	private static final Logger LOGGER = Logger.getLogger(AutenticadorBean.class);

	public String autenticador() {

		Propriedades propriedades = Propriedades.getInstance();
		RespostaToken respostaToken = segurancaController.pegarToken(propriedades.getValorString("TOKEN_CLIENT"), login,
				senha, propriedades.getValorString("TOKEN_GRAND_TYPE"));
		if (respostaToken != null) {
			Object b = new Object();
			SessionUtil.setParam("token", respostaToken.getAccess_token());
			SessionUtil.setParam("login", login);
			
			byte[] apiKeySecretBytes = "nesoft".getBytes();
		    Key signingKey = new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
			
			Claims claims = Jwts.parser()         
				       .setSigningKey(signingKey)
				       .parseClaimsJws(respostaToken.getAccess_token()).getBody();
			if(claims.get("authorities")!=null) {
				List<String> lista = (List<String>) claims.get("authorities");
				if(!lista.isEmpty() && lista.get(0).equals("ROLE_ADMIN")) {
					SessionUtil.setParam("ADMINLogado", b);
				}else {
					SessionUtil.setParam("USUARIOLogado", b);
				}
			}
			return "/index.xhtml?faces-redirect=true";
		} else {
			return null;
		}
	}

	public String logout() {
		LOGGER.info("Logout realizado.");
		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();

		final HttpServletRequest r = (HttpServletRequest) ec.getRequest();
		r.getSession(false).invalidate();

		return "login.xhtml";
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
	 * @return the senha
	 */
	public String getSenha() {
		return senha;
	}

	/**
	 * @param senha the senha to set
	 */
	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getLoginLogado() {
		return (String) SessionUtil.getParam("login");
	}
}
