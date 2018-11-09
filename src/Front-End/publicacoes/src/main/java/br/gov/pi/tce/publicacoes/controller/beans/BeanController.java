package br.gov.pi.tce.publicacoes.controller.beans;

import java.io.Serializable;
import java.text.MessageFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.commons.beanutils.BeanUtils;



public class BeanController implements Serializable {
	
	private static final long serialVersionUID = 1L;
	public static final String BUNDLE_PADRAO = "Mensagens";
	
	
	public void addMessage(Severity severityInfo, String summary, String detail) {
        FacesMessage message = new FacesMessage(severityInfo, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
	
	
	public void addMessage(Severity severidade, String chave, Object... parametros) {
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
	
	
	public List<SelectItem> getSelectItens(Collection beans, String... campos) {
		List<SelectItem> selectItems = Collections.EMPTY_LIST;

		try {
			if(beans != null) {
				selectItems = new ArrayList<SelectItem>(beans.size());

				for(Object objeto : beans) {
					StringBuilder label = new StringBuilder("");
					if(campos != null && campos.length > 0) {
						for(String campo : campos) {
							label.append(BeanUtils.getProperty(objeto, campo) + " - ");
						}
						
							if(label.length() > 0){	
								label.delete(label.length() - 3, label.length());
							}
						
					}
					else {
						label.append(objeto.toString());
					}
					selectItems.add(new SelectItem(objeto, label.toString()));
				}
			}
		}
		catch(Exception exception) {
			return Collections.EMPTY_LIST;
		}

		return selectItems;
	}
	
	
	/**
	 * Converte um Date em LocalDate.
	 * 
	 * @param date
	 * @return
	 */
	public LocalDate asLocalDate(Date date) {
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
	}


}
