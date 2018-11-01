package br.gov.pi.tce.siscap.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.gov.pi.tce.siscap.api.model.NotificacaoConfig;
import br.gov.pi.tce.siscap.api.repository.notificacao.config.NotificacaoConfigRepositoryQuery;

public interface NotificacaoConfigRepository extends JpaRepository<NotificacaoConfig, Long>, NotificacaoConfigRepositoryQuery {

}
