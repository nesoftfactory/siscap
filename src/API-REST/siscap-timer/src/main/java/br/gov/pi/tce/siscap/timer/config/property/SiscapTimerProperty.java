package br.gov.pi.tce.siscap.timer.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

import br.gov.pi.tce.siscap.timer.collect.MunicipiosColetor;
import br.gov.pi.tce.siscap.timer.collect.ParnaibaColetor;
import br.gov.pi.tce.siscap.timer.collect.PiauiColetor;
import br.gov.pi.tce.siscap.timer.collect.TeresinaColetor;

@ConfigurationProperties("siscap")
public class SiscapTimerProperty {

	private final String uriApiSicap = "http://localhost:7788/";
	private final Mail mail = new Mail();
	private final Fontes fontes = new Fontes();
	
	private Integer quantidadeDiasColeta = 5;
	private Integer quantidadeTentativasNofiticar = 1;

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
	
	public Integer getQuantidadeTentativasNofiticar() {
		return quantidadeTentativasNofiticar;
	}

	public void setQuantidadeTentativasNofiticar(Integer quantidadeTentativasNofiticar) {
		this.quantidadeTentativasNofiticar = quantidadeTentativasNofiticar;
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
		private int municipios = 10;
		private int piaui = 1010;
		private int teresina = 12;
		private int parnaiba = 13;

		public int getMunicipios() {
			return municipios;
		}
		
		public void setMunicipios(int municipios) {
			this.municipios = municipios;
		}
		
		public int getPiaui() {
			return piaui;
		}
		
		public void setPiaui(int piaui) {
			this.piaui = piaui;
		}
		
		public int getTeresina() {
			return teresina;
		}
		
		public void setTeresina(int teresina) {
			this.teresina = teresina;
		}
		
		public int getParnaiba() {
			return parnaiba;
		}
		
		public void setParnaiba(int parnaiba) {
			this.parnaiba = parnaiba;
		}

	}
	
}
