package br.gov.pi.tce.publicacoes.controller.beans;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

public class BeanController implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	
	public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
	
	public void addMessage(Severity severityInfo, String summary, String detail) {
        FacesMessage message = new FacesMessage(severityInfo, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }


}
