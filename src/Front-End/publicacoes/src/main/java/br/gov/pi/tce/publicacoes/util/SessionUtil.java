package br.gov.pi.tce.publicacoes.util;

import java.io.Serializable;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

public class SessionUtil implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static HttpSession getSession() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		HttpSession sessao = (HttpSession) ctx.getExternalContext().getSession(false);
		return sessao;
	}

	public static void setParam(String key, Object value) {
		getSession().setAttribute(key, value);
	}

	public static Object getParam(String key) {
		HttpSession session = getSession();
		if (session == null) {
			return null;
		} else {
			return session.getAttribute(key);
		}
	}

	public static void remove(String key) {
		getSession().removeAttribute(key);
	}

	public static void invalidate() {
		getSession().invalidate();
	}
}
