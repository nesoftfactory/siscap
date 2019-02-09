package br.gov.pi.tce.publicacoes.modelo.elastic;

import java.util.ArrayList;
import java.util.List;

public class PublicacaoElasticTO {
	
	private Long idArquivo;
	private String nomePublicacao;
	private String dataPublicacao;
	private List<String> paginas = new ArrayList<>();
	private String fonte;
	
	
	
	public List<String> getPaginas() {
		return paginas;
	}
	public void setPaginas(List<String> paginas) {
		this.paginas = paginas;
	}
	public String getFonte() {
		return fonte;
	}
	public void setFonte(String fonte) {
		this.fonte = fonte;
	}
	public Long getIdArquivo() {
		return idArquivo;
	}
	public void setIdArquivo(Long idArquivo) {
		this.idArquivo = idArquivo;
	}
	public String getNomePublicacao() {
		return nomePublicacao;
	}
	public void setNomePublicacao(String nomePublicacao) {
		this.nomePublicacao = nomePublicacao;
	}
	public String getDataPublicacao() {
		return dataPublicacao;
	}
	public void setDataPublicacao(String dataPublicacao) {
		this.dataPublicacao = dataPublicacao;
	}

	

}
