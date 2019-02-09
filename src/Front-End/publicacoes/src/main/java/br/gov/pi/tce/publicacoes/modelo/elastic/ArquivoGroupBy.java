package br.gov.pi.tce.publicacoes.modelo.elastic;

public class ArquivoGroupBy {
	
	private Terms terms;
    private AggsPublicacao aggs;

    
    
    
	public ArquivoGroupBy() {
		super();
	}
	public ArquivoGroupBy(Terms terms, AggsPublicacao aggs) {
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
	public AggsPublicacao getAggs() {
		return aggs;
	}
	public void setAggs(AggsPublicacao aggs) {
		this.aggs = aggs;
	}
    
    

}
