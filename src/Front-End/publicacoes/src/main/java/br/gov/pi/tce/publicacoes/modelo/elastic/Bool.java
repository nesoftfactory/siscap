package br.gov.pi.tce.publicacoes.modelo.elastic;

import java.util.List;

public class Bool {
	
	private List<Must> must;
	
	private List<Filter> filter;
	

	public List<Must> getMust() {
		return must;
	}

	public void setMust(List<Must> must) {
		this.must = must;
	}

	public List<Filter> getFilter() {
		return filter;
	}

	public void setFilter(List<Filter> filter) {
		this.filter = filter;
	}
	
	
	
	
}
