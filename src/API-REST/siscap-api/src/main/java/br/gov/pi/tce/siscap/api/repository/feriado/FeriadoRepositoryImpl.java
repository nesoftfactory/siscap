package br.gov.pi.tce.siscap.api.repository.feriado;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.gov.pi.tce.siscap.api.model.Feriado;
import br.gov.pi.tce.siscap.api.model.Fonte;
import br.gov.pi.tce.siscap.api.repository.filter.FeriadoFilter;

public class FeriadoRepositoryImpl implements FeriadoRepositoryQuery {
	
	@PersistenceContext
	private EntityManager manager;

	@Override
	public List<Feriado> filtrar(FeriadoFilter feriadoFilter) {
		CriteriaBuilder builder= manager.getCriteriaBuilder();
		CriteriaQuery<Feriado> criteriaQuery = builder.createQuery(Feriado.class);
		
		Root<Feriado> root = criteriaQuery.from(Feriado.class);
		
		// restrições
		Predicate[] predicates = criarRestriscoes(feriadoFilter, builder, root);
		criteriaQuery.where(predicates);
		
		TypedQuery<Feriado> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}

	private Predicate[] criarRestriscoes(FeriadoFilter feriadoFilter, CriteriaBuilder builder, Root<Feriado> root) {
		
		List<Predicate> predicates = new ArrayList<>();
		if (feriadoFilter.getIdFonte() != null) {
			Fonte fonte = new Fonte();
			fonte.setId(feriadoFilter.getIdFonte());
			
			Predicate fonteInformada = builder.isMember(fonte, root.get("fontes"));
			Predicate todasAsFontes = builder.equal(root.get("todasFontes"), true);
			
			predicates.add(builder.or(fonteInformada, todasAsFontes));
		}
		
		if (feriadoFilter.getPeriodoDe() != null) {
			predicates.add(builder.greaterThanOrEqualTo(root.get("data"), feriadoFilter.getPeriodoDe()));
		}
		
		if (feriadoFilter.getPeriodoAte() != null) {
			predicates.add(builder.lessThanOrEqualTo(root.get("data"), feriadoFilter.getPeriodoAte()));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

}
