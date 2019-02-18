package br.gov.pi.tce.publicacoes.modelo.elastic;

public class Filter {
	
	private Range range;
	
	

	public Filter(Range range) {
		super();
		this.range = range;
	}

	public Range getRange() {
		return range;
	}

	public void setRange(Range range) {
		this.range = range;
	}
	
	

}
