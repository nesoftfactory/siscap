package br.gov.pi.tce.publicacoes.modelo.elastic;

public class TermsArquivo extends Terms{
	
	
	private Order order;

	public TermsArquivo() {
		super();
	}
	
	

	public TermsArquivo(String field, Order order ) {
		super(field);
		this.order = order;
	}



	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
	
	

	
	
	
	
}
