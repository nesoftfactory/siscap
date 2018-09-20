package br.gov.pi.tce.publicacoes.modelo;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class TipoFonte 
{
	
	private Long id;
    private String nome;
    private Boolean ativo = false;
    private Usuario usuarioCriacao;
    private Usuario usuarioAtualizacao; 
    
	public TipoFonte() {
		super();
	}

	public TipoFonte(String nome) {
		super();
		setNome(nome);
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getNome() {
		return nome;
	}
	
	public Usuario getUsuarioCriacao() {
		return usuarioCriacao;
	}
	
	public Usuario getUsuarioAtualizacao() {
		return usuarioAtualizacao;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public void setUsuarioCriacao(Usuario usuarioCriacao) {
		this.usuarioCriacao = usuarioCriacao;
	}
	
	public void setUsuarioAtualizacao(Usuario usuarioAtualizacao) {
		this.usuarioAtualizacao = usuarioAtualizacao;
	}
	
	
	
	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	@JsonIgnore
	public String getTextoAtivo() {
		return getAtivo() ? "Sim" : "Não";
	}
	
	public String toString() {
		return nome;
	}
    
}
