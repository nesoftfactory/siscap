package br.gov.pi.tce.publicacoes.modelo;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class AnexoPublicacaoHistorico  {
	
	private Long id;
	private PublicacaoAnexo publicacaoAnexo;
	private String mensagem;
	private boolean sucesso;
	private String dataCriacao;
	private Usuario usuarioCriacao;

	public AnexoPublicacaoHistorico() {
	}

	public AnexoPublicacaoHistorico(PublicacaoAnexo publicacaoAnexo, String mensagem, boolean sucesso,Usuario usuarioLogado) {
		this.publicacaoAnexo = publicacaoAnexo;
		this.mensagem = mensagem;
		this.sucesso = sucesso;
		this.usuarioCriacao= usuarioLogado;
	}
	
	public AnexoPublicacaoHistorico(String mensagem, boolean sucesso,String dataCriacao, Usuario usuarioCriacao) {
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
	
	

	
	public PublicacaoAnexo getPublicacaoAnexo() {
		return publicacaoAnexo;
	}

	public void setPublicacaoAnexo(PublicacaoAnexo publicacaoAnexo) {
		this.publicacaoAnexo = publicacaoAnexo;
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


}

