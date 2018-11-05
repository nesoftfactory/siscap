package br.gov.pi.tce.siscap.api.repository.notificacao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.gov.pi.tce.siscap.api.model.Fonte;
import br.gov.pi.tce.siscap.api.model.Notificacao;
import br.gov.pi.tce.siscap.api.model.Publicacao;
import br.gov.pi.tce.siscap.api.repository.filter.NotificacaoFilter;

public class NotificacaoRepositoryImpl implements NotificacaoRepositoryQuery {
	
	@PersistenceContext
	private EntityManager manager;

	@Override
	public List<Notificacao> filtrar(NotificacaoFilter filter) {
		CriteriaBuilder builder= manager.getCriteriaBuilder();
		CriteriaQuery<Notificacao> criteriaQuery = builder.createQuery(Notificacao.class);
		
		Root<Notificacao> root = criteriaQuery.from(Notificacao.class);
		
		// restrições
		Predicate[] predicates = criarRestriscoes(filter, builder, root);
		criteriaQuery.where(predicates);
		
		TypedQuery<Notificacao> query = manager.createQuery(criteriaQuery);
		
		List<Notificacao> lista = query.getResultList();
		for (Notificacao notificacao : lista) {
			notificacao.setPublicacao(null);
			notificacao.setUsuarios(null);
		}
		return lista;
	}

	private Predicate[] criarRestriscoes(NotificacaoFilter filter, CriteriaBuilder builder, Root<Notificacao> root) {
		
		List<Predicate> predicates = new ArrayList<>();
		if (filter.getTipo() != null) {
			predicates.add(builder.equal(root.get("tipo"), filter.getTipo()));
		}
				
		if (filter.getIdPublicacao() != null) {
			Publicacao publicacao = new Publicacao();
			publicacao.setId(filter.getIdPublicacao());
			
			predicates.add(builder.equal(root.get("publicacao"), publicacao));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

}
