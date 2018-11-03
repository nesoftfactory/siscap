package br.gov.pi.tce.siscap.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.gov.pi.tce.siscap.api.model.Notificacao;
import br.gov.pi.tce.siscap.api.repository.notificacao.NotificacaoRepositoryQuery;

public interface NotificacaoRepository extends JpaRepository<Notificacao, Long>, NotificacaoRepositoryQuery {

}
