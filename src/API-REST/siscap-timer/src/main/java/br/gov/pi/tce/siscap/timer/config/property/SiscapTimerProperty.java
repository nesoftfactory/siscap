package br.gov.pi.tce.siscap.timer.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

import br.gov.pi.tce.siscap.timer.enums.FONTE_COLETA_AUTOMATICA;

@ConfigurationProperties("siscap")
public class SiscapTimerProperty {

	private final String uriApiSicap = "http://localhost:7788/";
	private final Mail mail = new Mail();
	private final Fontes fontes = new Fontes();
	
	private Integer quantidadeDiasColeta = 5;

	public Integer getQuantidadeDiasColeta() {
		return quantidadeDiasColeta;
	}

	public void setQuantidadeDiasColeta(Integer quantidadeDiasColeta) {
		this.quantidadeDiasColeta = quantidadeDiasColeta;
	}
	
	public Mail getMail() {
		return mail;
	}
	
	public Fontes getFontes() {
		return fontes;
	}
	
	public String getUriApiSicap() {
		return uriApiSicap;
	}
	
	public static class Mail {
		private String host;
		private Integer port;
		private String username;
		private String password;
		
		public String getHost() {
			return host;
		}
		
		public void setHost(String host) {
			this.host = host;
		}
		
		public Integer getPort() {
			return port;
		}
		
		public void setPort(Integer port) {
			this.port = port;
		}
		
		public String getUsername() {
			return username;
		}
		
		public void setUsername(String username) {
			this.username = username;
		}
		
		public String getPassword() {
			return password;
		}
		
		public void setPassword(String password) {
			this.password = password;
		}

	}
	public static class Fontes {
		private final FonteAuto municipios = new FonteAuto(10, FONTE_COLETA_AUTOMATICA.MUNICIPIOS);
		private final FonteAuto piaui = new FonteAuto(1010, FONTE_COLETA_AUTOMATICA.PIAUI);
		private final FonteAuto teresina = new FonteAuto(12, FONTE_COLETA_AUTOMATICA.TERESINA);
		private final FonteAuto parnaiba = new FonteAuto(13, FONTE_COLETA_AUTOMATICA.PARNAIBA);

		public static class FonteAuto {
			private int id = 10;
			private final FONTE_COLETA_AUTOMATICA enumFonte;
			
			public FonteAuto(int id, FONTE_COLETA_AUTOMATICA enumFonte) {
				this.id = id;
				this.enumFonte = enumFonte;
			}

			public int getId() {
				return id;
			}
			
			public void setId(int id) {
				this.id = id;
			}
			
			public FONTE_COLETA_AUTOMATICA getEnumFonte() {
				return enumFonte;
			}
		}
		
		public FonteAuto getMunicipios() {
			return municipios;
		}
		
		public FonteAuto getPiaui() {
			return piaui;
		}
		
		public FonteAuto getTeresina() {
			return teresina;
		}
		
		public FonteAuto getParnaiba() {
			return parnaiba;
		}
		
	}
	
}
