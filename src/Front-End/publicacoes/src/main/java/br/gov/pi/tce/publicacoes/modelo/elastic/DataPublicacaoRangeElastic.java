package br.gov.pi.tce.publicacoes.modelo.elastic;

public class DataPublicacaoRangeElastic {
	
	private String gte;
	private String lte;
	private String format;
	
	
	
	
	public DataPublicacaoRangeElastic(String gte, String lte, String format) {
		super();
		this.gte = gte;
		this.lte = lte;
		this.format = format;
	}
	
	
	public String getGte() {
		return gte;
	}
	public void setGte(String gte) {
		this.gte = gte;
	}
	public String getLte() {
		return lte;
	}
	public void setLte(String lte) {
		this.lte = lte;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	
	

}
