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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TipoFonte other = (TipoFonte) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
    
}
