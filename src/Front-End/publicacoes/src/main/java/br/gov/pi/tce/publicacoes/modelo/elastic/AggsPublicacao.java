package br.gov.pi.tce.publicacoes.modelo.elastic;

public class AggsPublicacao {

	private PublicacaoGroupBy publicacao;
	
	

	public AggsPublicacao(PublicacaoGroupBy publicacao) {
		super();
		this.publicacao = publicacao;
	}

	public PublicacaoGroupBy getPublicacao() {
		return publicacao;
	}

	public void setPublicacao(PublicacaoGroupBy publicacao) {
		this.publicacao = publicacao;
	}

		
}
