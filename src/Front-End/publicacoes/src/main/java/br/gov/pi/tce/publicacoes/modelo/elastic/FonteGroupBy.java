package br.gov.pi.tce.publicacoes.modelo.elastic;

public class FonteGroupBy {

	 private Terms terms;
	 private AggsPaginas aggs;
	 
	 
	 
	 
	public FonteGroupBy(Terms terms, AggsPaginas aggs) {
		super();
		this.terms = terms;
		this.aggs = aggs;
	}
	public Terms getTerms() {
		return terms;
	}
	public void setTerms(Terms terms) {
		this.terms = terms;
	}
	public AggsPaginas getAggs() {
		return aggs;
	}
	public void setAggs(AggsPaginas aggs) {
		this.aggs = aggs;
	}
	 
	 
	 
	 
	 
}
