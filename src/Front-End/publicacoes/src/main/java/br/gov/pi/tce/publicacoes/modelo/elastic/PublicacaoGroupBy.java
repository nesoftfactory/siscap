package br.gov.pi.tce.publicacoes.modelo.elastic;

public class PublicacaoGroupBy {

	private Terms terms;
    public AggsDataPublicacao aggs;
    
    
    
    
	public PublicacaoGroupBy(Terms terms, AggsDataPublicacao aggs) {
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


	public AggsDataPublicacao getAggs() {
		return aggs;
	}


	public void setAggs(AggsDataPublicacao aggs) {
		this.aggs = aggs;
	}
	
	
    
    
}
