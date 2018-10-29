package br.gov.pi.tce.publicacoes.util;

import java.io.IOException;
import java.util.Properties;

public class Propriedades {

	private static Propriedades propriedades = new Propriedades(); 
	private Properties properties;
	
	private Propriedades() {
		properties = new Properties();
		try {
			properties.load(getClass().getResourceAsStream("/siscap.properties"));
		} catch (IOException e) {
			return;
		}
	} 
	
	public Object getValor(String propriedade) {
		return properties.getProperty(propriedade);
	}
	
	public String getValorString(String propriedade) {
		return properties.getProperty(propriedade).toString();
	}
	
	public Long getValorLong(String propriedade) {
		return Long.valueOf(properties.getProperty(propriedade).toString());
	}
	
	public int getValorInt(String propriedade) {
		return Integer.valueOf(properties.getProperty(propriedade).toString()).intValue();
	}

	public static Propriedades getInstance() {
		return propriedades; 
	}


}
