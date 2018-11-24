package br.gov.pi.tce.publicacoes.controller.beans.utils;

public class Erro {
	
	private String mensagemUsuario;
	private String mensagemDesenvolvedor;
	
	public Erro(String mensagemUsuario, String mensagemDesenvolvedor) {
		this.mensagemUsuario = mensagemUsuario;
		this.mensagemDesenvolvedor = mensagemDesenvolvedor;
	}

	public String getMensagemUsuario() {
		return mensagemUsuario;
	}

	public String getMensagemDesenvolvedor() {
		return mensagemDesenvolvedor;
	}

}
