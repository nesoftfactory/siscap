package br.gov.pi.tce.publicacoes.modelo.elastic;

public class Range {

	private DataPublicacaoRangeElastic data_publicacao;
	
	

	public Range(DataPublicacaoRangeElastic data_publicacao) {
		super();
		this.data_publicacao = data_publicacao;
	}

	public DataPublicacaoRangeElastic getData_publicacao() {
		return data_publicacao;
	}

	public void setData_publicacao(DataPublicacaoRangeElastic data_publicacao) {
		this.data_publicacao = data_publicacao;
	}
	
	
}
