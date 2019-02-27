package br.gov.pi.tce.publicacoes.modelo.elastic;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class PublicacaoElasticTO {
	
	private Long idArquivo;
	private String nomePublicacao;
	private String dataPublicacao;
	private List<String> paginas = new ArrayList<>();
	private String fonte;
	
	
	
	@JsonIgnore
	private String conteudoPrimeiraPagina;
	
	
	
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
	public String getConteudoPrimeiraPagina() {
		return conteudoPrimeiraPagina;
	}
	public void setConteudoPrimeiraPagina(String conteudoPrimeiraPagina) {
		this.conteudoPrimeiraPagina = conteudoPrimeiraPagina;
	}
	
	@JsonIgnore
	/*
	 * A data ta vindo do elastic no formato 2018-11-07T00:00:00.000Z
	 */
	public String getDataPublicacaoFormatada() {
		String data = (dataPublicacao.substring(0,10)).replaceAll("-", "/");
		String[] s = data.split("/");
		String novaData = s[2]+"/"+s[1]+"/"+s[0];
		return novaData;
	}
	
	

	

}
