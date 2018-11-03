package br.gov.pi.tce.siscap.api.repository.notificacao;

import java.util.List;

import br.gov.pi.tce.siscap.api.model.Notificacao;
import br.gov.pi.tce.siscap.api.repository.filter.NotificacaoFilter;

public interface NotificacaoRepositoryQuery {
	
	public List<Notificacao> filtrar(NotificacaoFilter filter);

}
