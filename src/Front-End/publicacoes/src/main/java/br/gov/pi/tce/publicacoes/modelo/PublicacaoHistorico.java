package br.gov.pi.tce.publicacoes.modelo;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class PublicacaoHistorico  {
	
	private Long id;
	private Publicacao publicacao;
	private String mensagem;
	private boolean sucesso;
	private String dataCriacao;
	private Usuario usuarioCriacao;
	private String dataCriacaoString;

	public PublicacaoHistorico() {
	}

	public PublicacaoHistorico(Publicacao publicacao, String mensagem, boolean sucesso,Usuario usuarioLogado) {
		this.publicacao = publicacao;
		this.mensagem = mensagem;
		this.sucesso = sucesso;
		this.usuarioCriacao= usuarioLogado;
	}
	
	public PublicacaoHistorico(String mensagem, boolean sucesso,String dataCriacao, Usuario usuarioCriacao) {
		this.mensagem = mensagem;
		this.sucesso = sucesso;
		this.dataCriacao = dataCriacao;
		this.usuarioCriacao= usuarioCriacao;
	}


	
	
	public String getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(String dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
		
	public String getMensagem() {
		return mensagem;
	}
	
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	
	public boolean getSucesso() {
		return sucesso;
	}

	public void setSucesso(boolean sucesso) {
		this.sucesso = sucesso;
	}
	
	public Publicacao getPublicacao() {
		return publicacao;
	}

	public void setPublicacao(Publicacao publicacao) {
		this.publicacao = publicacao;
	}

	
	public Usuario getUsuarioCriacao() {
		return usuarioCriacao;
	}
	
	public void setUsuarioCriacao(Usuario usuarioCriacao) {
		this.usuarioCriacao = usuarioCriacao;
	}
	
	@JsonIgnore
	public String getTextoSucesso() {
		return getSucesso() ? "Sim" : "Não";
	}

	public String getDataCriacaoString() {
		return dataCriacaoString;
	}

	public void setDataCriacaoString(String dataCriacaoString) {
		this.dataCriacaoString = dataCriacaoString;
	}
	
	


}

