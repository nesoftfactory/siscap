package br.gov.pi.tce.siscap.api.repository.publicacao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import br.gov.pi.tce.siscap.api.model.Fonte;
import br.gov.pi.tce.siscap.api.model.Publicacao;
import br.gov.pi.tce.siscap.api.repository.filter.PublicacaoFilter;
import br.gov.pi.tce.siscap.api.service.exception.FiltroPublicacaoDataInvalidaException;

public class PublicacaoRepositoryImpl implements PublicacaoRepositoryQuery {
	
	@PersistenceContext
	private EntityManager manager;

	@Override
	public List<Publicacao> filtrar(PublicacaoFilter publicacaoFilter) {
		CriteriaBuilder builder= manager.getCriteriaBuilder();
		CriteriaQuery<Publicacao> criteriaQuery = builder.createQuery(Publicacao.class);
		
		Root<Publicacao> root = criteriaQuery.from(Publicacao.class);
		
		// restrições
		Predicate[] predicates = criarRestriscoes(publicacaoFilter, builder, root);
		criteriaQuery.where(predicates);
		
		TypedQuery<Publicacao> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}

	private Predicate[] criarRestriscoes(PublicacaoFilter publicacaoFilter, CriteriaBuilder builder, Root<Publicacao> root) {
		
		List<Predicate> predicates = new ArrayList<>();
		if (publicacaoFilter.getIdFonte() != null) {
			Fonte fonte = new Fonte();
			fonte.setId(publicacaoFilter.getIdFonte());
			predicates.add(builder.equal(root.get("fonte"), fonte));
		}
		
		
		if (StringUtils.isNotEmpty(publicacaoFilter.getNome())) {
			predicates.add(builder.equal(root.get("nome"), publicacaoFilter.getNome()));
		}
		
		if (publicacaoFilter.getSucesso() != null && publicacaoFilter.getSucesso().booleanValue()) {
			predicates.add(builder.equal(root.get("sucesso"), publicacaoFilter.getSucesso()));
		}
		
		if (publicacaoFilter.getData() != null) {
			predicates.add(builder.equal(root.get("data"), publicacaoFilter.getData()));
		}
		
		else if (publicacaoFilter.getDataInicio() != null && publicacaoFilter.getDataFim() != null) {
			if(publicacaoFilter.getDataInicio().isAfter(publicacaoFilter.getDataFim())) {
				throw new FiltroPublicacaoDataInvalidaException();
			}
			predicates.add(builder.between(root.get("data"), publicacaoFilter.getDataInicio(), publicacaoFilter.getDataFim()));
		}
		

		return predicates.toArray(new Predicate[predicates.size()]);
	}

}
