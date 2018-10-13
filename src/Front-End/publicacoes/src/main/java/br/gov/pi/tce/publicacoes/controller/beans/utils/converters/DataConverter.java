package br.gov.pi.tce.publicacoes.controller.beans.utils.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

@FacesConverter("dataConverter")
public class DataConverter implements Converter {
	
	public DataConverter() {
		super();
	}

	public Object getAsObject(FacesContext facesContext, UIComponent component,
			String value) {
		
		if (value == null || value.trim().equals("")) {
			return "";
		}
		try {
			// Tratamento para data
			String[] dataSplit = value.split("/");
			if (dataSplit[2].length() == 2) {
				return ("20"+dataSplit[2]+"-"+dataSplit[1]+"-"+dataSplit[0]);
			} else {
				return (dataSplit[2]+"-"+dataSplit[1]+"-"+dataSplit[0]);
			}
		} catch (Exception e) {
			throw new ConverterException(e);
		}
	
	}

	public String getAsString(FacesContext facesContext, UIComponent component,
			Object value) {
		try {
			if (value instanceof String) {
				return (String) value;
			}
		} catch (Exception e) {
			throw new ConverterException(e);
		}

		return null;
	}

}
