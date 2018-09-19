package br.gov.pi.tce.publicacoes.modelo;

/**
 * Classe responsável por representar a entidade Historico Publicacao.
 * 
 * 
 * @author Erick Guilherme Cavalcanti
 *
 */
public class HistoricoPublicacao {

	private Long id;
	private Publicacao publicacao;
	private String mensagemErro;
	private Boolean ativo;

	/**
	 * 
	 */
	public HistoricoPublicacao() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param publicacao
	 * @param mensagemErro
	 * @param ativo
	 */
	public HistoricoPublicacao(Publicacao publicacao, String mensagemErro, Boolean ativo) {
		super();
		this.publicacao = publicacao;
		this.mensagemErro = mensagemErro;
		this.ativo = ativo;
	}

	/**
	 * @param id
	 * @param publicacao
	 * @param mensagemErro
	 * @param ativo
	 */
	public HistoricoPublicacao(Long id, Publicacao publicacao, String mensagemErro, Boolean ativo) {
		this(publicacao, mensagemErro, ativo);
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the publicacao
	 */
	public Publicacao getPublicacao() {
		return publicacao;
	}

	/**
	 * @param publicacao the publicacao to set
	 */
	public void setPublicacao(Publicacao publicacao) {
		this.publicacao = publicacao;
	}

	/**
	 * @return the mensagemErro
	 */
	public String getMensagemErro() {
		return mensagemErro;
	}

	/**
	 * @param mensagemErro the mensagemErro to set
	 */
	public void setMensagemErro(String mensagemErro) {
		this.mensagemErro = mensagemErro;
	}

	/**
	 * @return the ativo
	 */
	public Boolean getAtivo() {
		return ativo;
	}

	/**
	 * @param ativo the ativo to set
	 */
	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
}
