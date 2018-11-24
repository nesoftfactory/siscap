package br.gov.pi.tce.publicacoes.controller.beans.utils.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import br.gov.pi.tce.publicacoes.clients.FonteServiceClient;
import br.gov.pi.tce.publicacoes.modelo.Fonte;


@FacesConverter("fonteConverter")
public class FonteConverter implements Converter {
	
	//@Inject
	private FonteServiceClient fonteServiceClient;
	
	
	public FonteConverter() {
		super();
		if(fonteServiceClient  == null) {
			fonteServiceClient = new FonteServiceClient();
		}
	}

	public Object getAsObject(FacesContext facesContext, UIComponent component,
			String value) {
		
		if (value == null || value.trim().equals("")) {
			return "";
		}
		try {
			Fonte fonte = fonteServiceClient.consultarFontePorCodigo(Long.valueOf(value)); 
			return fonte;
		} catch (Exception e) {
			throw new ConverterException(e);
		}
	}

	public String getAsString(FacesContext facesContext, UIComponent component,
			Object value) {
		try {
			if (value instanceof Fonte) {
				Fonte fonte = (Fonte) value;
				return String.valueOf(fonte.getId());
			}
		} catch (Exception e) {
			throw new ConverterException(e);
		}

		return null;
	}

}
