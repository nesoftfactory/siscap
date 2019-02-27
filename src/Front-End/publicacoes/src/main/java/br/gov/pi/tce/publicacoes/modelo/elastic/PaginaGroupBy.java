package br.gov.pi.tce.publicacoes.modelo.elastic;

public class PaginaGroupBy {
	private Terms terms;
	

	public PaginaGroupBy(Terms terms) {
		super();
		this.terms = terms;
	}

	
	public Terms getTerms() {
		return terms;
	}

	public void setTerms(Terms terms) {
		this.terms = terms;
	}
	
	
}
