package br.gov.pi.tce.siscap.api.repository.notificacao.config;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.gov.pi.tce.siscap.api.model.NotificacaoConfig;
import br.gov.pi.tce.siscap.api.repository.filter.NotificacaoConfigFilter;

public class NotificacaoConfigRepositoryImpl implements NotificacaoConfigRepositoryQuery {
	
	@PersistenceContext
	private EntityManager manager;

	@Override
	public List<NotificacaoConfig> filtrar(NotificacaoConfigFilter filter) {
		CriteriaBuilder builder= manager.getCriteriaBuilder();
		CriteriaQuery<NotificacaoConfig> criteriaQuery = builder.createQuery(NotificacaoConfig.class);
		
		Root<NotificacaoConfig> root = criteriaQuery.from(NotificacaoConfig.class);
		
		// restrições
		Predicate[] predicates = criarRestriscoes(filter, builder, root);
		criteriaQuery.where(predicates);
		
		TypedQuery<NotificacaoConfig> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}

	private Predicate[] criarRestriscoes(NotificacaoConfigFilter filter, CriteriaBuilder builder, Root<NotificacaoConfig> root) {
		
		List<Predicate> predicates = new ArrayList<>();
		if (filter.getTipo() != null) {
			predicates.add(builder.equal(root.get("tipo"), filter.getTipo()));
		}
				
		if (filter.getAtivo() != null) {
			predicates.add(builder.equal(root.get("ativo"), filter.getAtivo()));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

}
