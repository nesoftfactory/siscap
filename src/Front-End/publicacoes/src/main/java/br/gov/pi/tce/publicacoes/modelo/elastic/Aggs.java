package br.gov.pi.tce.publicacoes.modelo.elastic;

public class Aggs {
	
	private ArquivoGroupBy arquivo;
	
	

	public Aggs(ArquivoGroupBy arquivo) {
		super();
		this.arquivo = arquivo;
	}

	public ArquivoGroupBy getArquivo() {
		return arquivo;
	}

	public void setArquivo(ArquivoGroupBy arquivo) {
		this.arquivo = arquivo;
	}

	
	

}
