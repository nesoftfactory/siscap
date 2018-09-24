package br.gov.pi.tce.publicacoes.modelo;

import java.util.Date;

/**
 * Classe responsável por representar a entidade Publicacao.
 * 
 * 
 * @author Erick Guilherme Cavalcanti
 *
 */
public class Publicacao {

	private Long id;
	private Fonte fonte;
	private String nome;
	private Date data;
	private String codigo;
	private String nomeArquivo;
	private String linkArquivo;
	private String arquivo;
	private Boolean sucesso;
	private Boolean anexo;
	private Long quantidadeTentativas;
	private Publicacao arquivoAnexo;

	/**
	 * 
	 */
	public Publicacao() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param fonte
	 * @param nome
	 * @param data
	 * @param codigo
	 * @param nomeArquivo
	 * @param linkArquivo
	 * @param arquivo
	 * @param sucesso
	 * @param anexo
	 * @param quantidadeTentativas
	 */
	public Publicacao(Fonte fonte, String nome, Date data, String codigo,
			String nomeArquivo, String linkArquivo, String arquivo, Boolean sucesso, Boolean anexo,
			Long quantidadeTentativas, Publicacao arquivoAnexo) {
		super();
		setFonte(fonte);
		setNome(nome);
		setData(data);
		setCodigo(codigo);
		setNomeArquivo(nomeArquivo);
		setLinkArquivo(linkArquivo);
		setArquivo(arquivo);
		setSucesso(sucesso);
		setAnexo(anexo);
		setQuantidadeTentativas(quantidadeTentativas);
		setArquivoAnexo(arquivoAnexo);
	}

	/**
	 * @param id
	 * @param fonte
	 * @param nome
	 * @param data
	 * @param codigo
	 * @param nomeArquivo
	 * @param linkArquivo
	 * @param arquivo
	 * @param sucesso
	 * @param anexo
	 * @param quantidadeTentativas
	 */
	public Publicacao(Long id, Fonte fonte, String nome, Date data,
			String codigo, String nomeArquivo, String linkArquivo,
			String arquivo, Date dataCriacao, String usuarioCriacao, Date dataAtualizacao,
			String usuarioAtualizacao, Boolean sucesso, Boolean anexo, Long quantidadeTentativas, Publicacao arquivoAnexo) {
		this(fonte, nome, data, codigo, nomeArquivo, linkArquivo, arquivo, sucesso, anexo, quantidadeTentativas, arquivoAnexo);
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
	 * @return the fonte
	 */
	public Fonte getFonte() {
		return fonte;
	}

	/**
	 * @param fonte the fonte to set
	 */
	public void setFonte(Fonte fonte) {
		this.fonte = fonte;
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
	 * @return the data
	 */
	public Date getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(Date data) {
		this.data = data;
	}

	/**
	 * @return the codigo
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the nomeArquivo
	 */
	public String getNomeArquivo() {
		return nomeArquivo;
	}

	/**
	 * @param nomeArquivo the nomeArquivo to set
	 */
	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	/**
	 * @return the linkArquivo
	 */
	public String getLinkArquivo() {
		return linkArquivo;
	}

	/**
	 * @param linkArquivo the linkArquivo to set
	 */
	public void setLinkArquivo(String linkArquivo) {
		this.linkArquivo = linkArquivo;
	}

	/**
	 * @return the arquivo
	 */
	public String getArquivo() {
		return arquivo;
	}

	/**
	 * @param arquivo the arquivo to set
	 */
	public void setArquivo(String arquivo) {
		this.arquivo = arquivo;
	}

	/**
	 * @return the sucesso
	 */
	public Boolean getSucesso() {
		return sucesso;
	}

	/**
	 * @param sucesso the sucesso to set
	 */
	public void setSucesso(Boolean sucesso) {
		this.sucesso = sucesso;
	}

	/**
	 * @return the anexo
	 */
	public Boolean getAnexo() {
		return anexo;
	}

	/**
	 * @param anexo the anexo to set
	 */
	public void setAnexo(Boolean anexo) {
		this.anexo = anexo;
	}

	/**
	 * @return the quantidadeTentativas
	 */
	public Long getQuantidadeTentativas() {
		return quantidadeTentativas;
	}

	/**
	 * @param quantidadeTentativas the quantidadeTentativas to set
	 */
	public void setQuantidadeTentativas(Long quantidadeTentativas) {
		this.quantidadeTentativas = quantidadeTentativas;
	}
	
	/**
	 * @return arquivoAnexo
	 */
	public Publicacao getArquivoAnexo() {
		return arquivoAnexo;
	}
		
	/**
	 * @param arquivoAnexo
	 */
	public void setArquivoAnexo(Publicacao arquivoAnexo) {
		this.arquivoAnexo = arquivoAnexo;
	}
}
