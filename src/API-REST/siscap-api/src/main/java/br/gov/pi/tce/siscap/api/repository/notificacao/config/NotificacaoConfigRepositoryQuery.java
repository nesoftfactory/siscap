package br.gov.pi.tce.siscap.api.repository.notificacao.config;

import java.util.List;

import br.gov.pi.tce.siscap.api.model.NotificacaoConfig;
import br.gov.pi.tce.siscap.api.repository.filter.NotificacaoConfigFilter;

public interface NotificacaoConfigRepositoryQuery {
	
	public List<NotificacaoConfig> filtrar(NotificacaoConfigFilter filter);

}
