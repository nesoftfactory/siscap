package br.gov.pi.tce.siscap.api.repository.filter;

import br.gov.pi.tce.siscap.api.model.enums.NotificacaoTipo;

public class NotificacaoFilter {

	private NotificacaoTipo tipo;
	private Long idPublicacao;
	
	public NotificacaoTipo getTipo() {
		return tipo;
	}
	
	public void setTipo(NotificacaoTipo tipo) {
		this.tipo = tipo;
	}

	public Long getIdPublicacao() {
		return idPublicacao;
	}

	public void setIdPublicacao(Long idPublicacao) {
		this.idPublicacao = idPublicacao;
	}
	
}
