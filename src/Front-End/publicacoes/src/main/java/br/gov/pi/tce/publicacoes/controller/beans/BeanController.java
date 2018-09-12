package br.gov.pi.tce.publicacoes.controller.beans;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

public class BeanController implements Serializable {
	
	private static final long serialVersionUID = 1L;
	public static final String BUNDLE_PADRAO = "Mensagens";
	
	
	public void addMessage(Severity severityInfo, String summary, String detail) {
        FacesMessage message = new FacesMessage(severityInfo, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
	
	
	public void registrarMensagem(Severity severidade, String chave, Object... parametros) {
		FacesMessage mensagem = getFacesMessage(severidade, chave, parametros);
		FacesContext.getCurrentInstance().addMessage(null, mensagem);	
	}
	
	
	protected FacesMessage getFacesMessage(Severity severidade, String chave, Object[] parametros) {
		String mensagem = null;

		ResourceBundle resourceBundle = ResourceBundle.getBundle(BUNDLE_PADRAO);
		try {
			mensagem = getMensagem(chave);

			if(parametros != null) {
				substituirParametros(resourceBundle, parametros);
				mensagem = MessageFormat.format(mensagem, parametros);
			}
		}
		catch(MissingResourceException exception) {
			mensagem = chave;
		}

		return new FacesMessage(severidade, mensagem, mensagem);
	}
	
	
	public static String getMensagem(String chave) {
		return getMensagem(BUNDLE_PADRAO, chave);
	}

	
	public static String getMensagem(String nomeBundle, String chave) {
		ResourceBundle bundle = ResourceBundle.getBundle(nomeBundle);
		return bundle.getString(chave);
	}
	
	
	private void substituirParametros(ResourceBundle bundle, Object[] parametros) {
		try {
			for(int i = 0; i < parametros.length; i++) {
				if(parametros[i] instanceof String) {
					String mensagem = bundle.getString((String) parametros[i]);

					if(mensagem != null) {
						parametros[i] = mensagem;
					}
				}
			}
		}
		catch(Exception exception) {
			return;
		}
	}


}
