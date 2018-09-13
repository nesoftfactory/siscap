package br.gov.pi.tce.publicacoes.modelo;

import java.util.Date;
import java.util.UUID;

public class TipoFonte 
{
	
	private UUID id;
    private String nome;
    private boolean ativo;
    private Usuario usuarioCriacao;
    private Date dataCriacao;
    private Date dataAtualizacao;
    private Usuario usuarioAtualizacao; 
    
	public TipoFonte() {
		super();
	}

	public TipoFonte(String nome) {
		super();
		setId(UUID.randomUUID());
		setNome(nome);
		setAtivo(true);
		setUsuarioCriacao(new Usuario());
		setDataCriacao(new Date());
		setUsuarioAtualizacao(null);
		setDataAtualizacao(null);
	}

	public UUID getId() {
		return id;
	}
	
	private void setId(UUID id) {
		this.id = id;
	}
	
	public String getNome() {
		return nome;
	}
	
	public boolean isAtivo() {
		return ativo;
	}
	
	public Usuario getUsuarioCriacao() {
		return usuarioCriacao;
	}
	
	public Date getDataCriacao() {
		return dataCriacao;
	}
	
	public Date getDataAtualizacao() {
		return dataAtualizacao;
	}
	
	public Usuario getUsuarioAtualizacao() {
		return usuarioAtualizacao;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}
	
	public void setUsuarioCriacao(Usuario usuarioCriacao) {
		this.usuarioCriacao = usuarioCriacao;
	}
	
	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}
	
	public void setDataAtualizacao(Date dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}
	
	public void setUsuarioAtualizacao(Usuario usuarioAtualizacao) {
		this.usuarioAtualizacao = usuarioAtualizacao;
	}
    
}
