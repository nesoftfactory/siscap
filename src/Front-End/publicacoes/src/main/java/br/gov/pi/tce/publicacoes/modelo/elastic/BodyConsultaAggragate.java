package br.gov.pi.tce.publicacoes.modelo.elastic;

public class BodyConsultaAggragate {
	
	private Aggs aggs;
	private Query query;
	
	

	public BodyConsultaAggragate(Aggs aggs) {
		super();
		this.aggs = aggs;
	}
	
	

	public Query getQuery() {
		return query;
	}



	public void setQuery(Query query) {
		this.query = query;
	}



	public Aggs getAggs() {
		return aggs;
	}

	public void setAggs(Aggs aggs) {
		this.aggs = aggs;
	}

	
	
	
	
	
	 
	 

}
