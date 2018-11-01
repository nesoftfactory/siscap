package br.gov.pi.tce.siscap.api.repository.filter;

import br.gov.pi.tce.siscap.api.model.enums.NotificacaoTipo;

public class NotificacaoConfigFilter {

	private NotificacaoTipo tipo;
	private Boolean ativo;
	
	public NotificacaoTipo getTipo() {
		return tipo;
	}
	
	public void setTipo(NotificacaoTipo tipo) {
		this.tipo = tipo;
	}
	
	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
}
