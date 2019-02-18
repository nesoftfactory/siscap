package br.gov.pi.tce.siscap.timer.model;

public class Fonte {
	
	// Nomes e URL das Fontes Padrão
	public static final String FONTE_NOME_DO_ESTADO     = "Diário Oficial do Estado";
	public static final String FONTE_NOME_DO_MUNICIPIOS = "Diário Oficial dos Municípios";
	public static final String FONTE_NOME_DO_TERESINA   = "Diário Oficial de Teresina";
	public static final String FONTE_NOME_DO_PARNAIBA   = "Diário Oficial de Parnaíba";
	
	public static final String FONTE_URL_DO_ESTADO      = "http://www.diariooficial.pi.gov.br/";
	public static final String FONTE_URL_DO_MUNICIPIOS  = "http://www.diarioficialdosmunicipios.org/";
	public static final String FONTE_URL_DO_TERESINA    = "http://www.dom.teresina.pi.gov.br/";
	public static final String FONTE_URL_DO_PARNAIBA    = "http://dom.parnaiba.pi.gov.br/";
	
	private Long id;
    private String nome;
    private String url;
    private TipoFonte tipoFonte;
    private Boolean ativo;
    private Usuario usuarioCriacao;
    private Usuario usuarioAtualizacao; 
    
	public Fonte() {
		super();
	}

	public Fonte(String nome, String url, TipoFonte tipoFonte) {
		super();
		setNome(nome);
		setUrl(url);
		setTipoFonte(tipoFonte);
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

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public Usuario getUsuarioCriacao() {
		return usuarioCriacao;
	}

	public void setUsuarioCriacao(Usuario usuarioCriacao) {
		this.usuarioCriacao = usuarioCriacao;
	}

	public Usuario getUsuarioAtualizacao() {
		return usuarioAtualizacao;
	}

	public void setUsuarioAtualizacao(Usuario usuarioAtualizacao) {
		this.usuarioAtualizacao = usuarioAtualizacao;
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
		Fonte other = (Fonte) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}