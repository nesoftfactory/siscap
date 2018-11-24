package br.gov.pi.tce.publicacoes.controller.beans.utils;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;


@Named
@SessionScoped
public class UtilsBean implements Serializable {

	public Date getHoje() {
		return Calendar.getInstance(TimeZone.getTimeZone("GMT-3"), new Locale("pt", "BR")).getTime();
	}

	public Boolean getTemMensagens() {
		return FacesContext.getCurrentInstance().getMessages().hasNext();
	}
	
	public String getStyleMessages(){
		if(getTemMensagens()){
			if(FacesContext.getCurrentInstance().getMessages().next().getSeverity().equals(FacesMessage.SEVERITY_WARN)){
				return "warn";
			}
			if(FacesContext.getCurrentInstance().getMessages().next().getSeverity().equals(FacesMessage.SEVERITY_INFO)){
				return "sucesso";
			}
		}
		return "erro";
	}
}
