package br.gov.pi.tce.publicacoes.controller.beans.utils.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import br.gov.pi.tce.publicacoes.clients.FonteServiceClient;
import br.gov.pi.tce.publicacoes.modelo.TipoFonte;


@FacesConverter("tipoFonteConverter")
public class TipoFonteConverter implements Converter {
	
	//@Inject
	private FonteServiceClient fonteServiceClient;
	
	
	public TipoFonteConverter() {
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
			TipoFonte tf = fonteServiceClient.consultarTipoFontePorCodigo(Long.valueOf(value)); 
			return tf;
		//}// catch (NumberFormatException ne) {
			// Ocorre quando vem um texto (Ex. Selecione...)
			// Nesse caso nao deve fazer nada aqui.
		} catch (Exception e) {
			throw new ConverterException(e);
		}
		//return null;
	}

	public String getAsString(FacesContext facesContext, UIComponent component,
			Object value) {
		try {
			if (value instanceof TipoFonte) {
				TipoFonte tipo = (TipoFonte) value;
				return String.valueOf(tipo.getId());
			}
		} catch (Exception e) {
			throw new ConverterException(e);
		}

		return null;
	}

}
