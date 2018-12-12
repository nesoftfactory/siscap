package br.gov.pi.tce.publicacoes.modelo;

/**
 * Classe responsável por representar a entidade PublicacaoAnexo.
 * 
 * @author Erick Guilherme Cavalcanti
 *
 */
public class PublicacaoAnexo {

	private Long id;
	private Publicacao publicacao;
	private String nome;
	private Long arquivo;
	private boolean sucesso;
	private String situacao;
	

	/**
	 * 
	 */
	public PublicacaoAnexo() {
		super();
	}

	/**
	 * @param publicacao
	 * @param nome
	 * @param arquivo
	 * @param sucesso
	 */
	public PublicacaoAnexo(Publicacao publicacao, String nome, Long arquivo, boolean sucesso) {
		super();
		setPublicacao(publicacao);
		setNome(nome);
		setArquivo(arquivo);
		setSucesso(sucesso);
	}

	/**
	 * @param id
	 * @param publicacao
	 * @param nome
	 * @param arquivo
	 * @param sucesso
	 */
	public PublicacaoAnexo(Long id, Publicacao publicacao, String nome, Long arquivo, boolean sucesso) {
		this(publicacao, nome, arquivo, sucesso);
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
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @param nome the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
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
	 * @return the arquivo
	 */
	public Long getArquivo() {
		return arquivo;
	}

	/**
	 * @param arquivo the arquivo to set
	 */
	public void setArquivo(Long arquivo) {
		this.arquivo = arquivo;
	}

	/**
	 * @return the sucesso
	 */
	public boolean isSucesso() {
		return sucesso;
	}

	/**
	 * @param sucesso the sucesso to set
	 */
	public void setSucesso(boolean sucesso) {
		this.sucesso = sucesso;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

}
