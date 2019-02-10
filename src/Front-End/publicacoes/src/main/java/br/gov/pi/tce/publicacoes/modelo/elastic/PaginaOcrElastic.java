package br.gov.pi.tce.publicacoes.modelo.elastic;

public class PaginaOcrElastic {
	
	private String textoOcr;
	private Long numPagina;
	private Long idArquivo;
	
	
	public String getTextoOcr() {
		return textoOcr;
	}
	public void setTextoOcr(String textoOcr) {
		this.textoOcr = textoOcr;
	}
	public Long getNumPagina() {
		return numPagina;
	}
	public void setNumPagina(Long numPagina) {
		this.numPagina = numPagina;
	}
	public Long getIdArquivo() {
		return idArquivo;
	}
	public void setIdArquivo(Long idArquivo) {
		this.idArquivo = idArquivo;
	}

	
	
}
