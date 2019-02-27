package br.gov.pi.tce.publicacoes.modelo.elastic;

public class DataPublicacaoGroupBy {

	private Terms terms;
    public AggsFonte aggs;
    
    
    
    
	public DataPublicacaoGroupBy(Terms terms, AggsFonte aggs) {
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
	public AggsFonte getAggs() {
		return aggs;
	}
	public void setAggs(AggsFonte aggs) {
		this.aggs = aggs;
	}
    
	
    
    
}
