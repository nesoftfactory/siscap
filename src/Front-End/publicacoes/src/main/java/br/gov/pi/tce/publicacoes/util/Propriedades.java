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

	public static Propriedades getInstance() {
		return propriedades; 
	}


}
