package br.gov.pi.tce.publicacoes.autenticacao;

import java.io.Serializable;

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
			SessionUtil.setParam("USUARIOLogado", b);
			SessionUtil.setParam("token", respostaToken.getAccess_token());
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

		return "/publicacoes/login2.xhtml?faces-redirect=true";
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

}
