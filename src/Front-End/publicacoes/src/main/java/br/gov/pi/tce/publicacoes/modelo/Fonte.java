package br.gov.pi.tce.publicacoes.modelo;

import java.util.Date;
import java.util.UUID;

public class Fonte {
	
	private UUID id;
    private String nome;
    private String url;
    private TipoFonte tipoFonte;
    private boolean ativo;
    private Usuario usuarioCriacao;
    private Date dataCriacao;
    private Date dataAtualizacao;
    private Usuario usuarioAtualizacao; 
    
	public Fonte() {
		super();
	}

	public Fonte(String nome, String url, TipoFonte tipoFonte) {
		super();
		setId(UUID.randomUUID());
		setNome(nome);
		setUrl(url);
		setTipoFonte(tipoFonte);
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

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public TipoFonte getTipoFonte() {
		return tipoFonte;
	}

	public void setTipoFonte(TipoFonte tipoFonte) {
		this.tipoFonte = tipoFonte;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public Usuario getUsuarioCriacao() {
		return usuarioCriacao;
	}

	public void setUsuarioCriacao(Usuario usuarioCriacao) {
		this.usuarioCriacao = usuarioCriacao;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public Date getDataAtualizacao() {
		return dataAtualizacao;
	}

	public void setDataAtualizacao(Date dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

	public Usuario getUsuarioAtualizacao() {
		return usuarioAtualizacao;
	}

	public void setUsuarioAtualizacao(Usuario usuarioAtualizacao) {
		this.usuarioAtualizacao = usuarioAtualizacao;
	}

}