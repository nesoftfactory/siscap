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

	private Long idPublicacao;
	private Fonte fonte;
	private String nomePublicacao;
	private Date dataPublicacao;
	private String codigoPublicacao;
	private String nomeArquivoPublicacao;
	private String linkArquivoPublicacao;
	private String arquivoPublicacao;
	private Boolean ativo;
	private Boolean anexo;
	private Long quantidadeTentativas;

	/**
	 * 
	 */
	public Publicacao() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param fonte
	 * @param nomePublicacao
	 * @param dataPublicacao
	 * @param codigoPublicacao
	 * @param nomeArquivoPublicacao
	 * @param linkArquivoPublicacao
	 * @param arquivoPublicacao
	 * @param ativo
	 * @param anexo
	 * @param quantidadeTentativas
	 */
	public Publicacao(Fonte fonte, String nomePublicacao, Date dataPublicacao, String codigoPublicacao,
			String nomeArquivoPublicacao, String linkArquivoPublicacao, String arquivoPublicacao, Boolean ativo, Boolean anexo,
			Long quantidadeTentativas) {
		super();
		this.fonte = fonte;
		this.nomePublicacao = nomePublicacao;
		this.dataPublicacao = dataPublicacao;
		this.codigoPublicacao = codigoPublicacao;
		this.nomeArquivoPublicacao = nomeArquivoPublicacao;
		this.linkArquivoPublicacao = linkArquivoPublicacao;
		this.arquivoPublicacao = arquivoPublicacao;
		this.ativo = ativo;
		this.anexo = anexo;
		this.quantidadeTentativas = quantidadeTentativas;
	}

	/**
	 * @param idPublicacao
	 * @param idFonte
	 * @param nomePublicacao
	 * @param dataPublicacao
	 * @param codigoPublicacao
	 * @param nomeArquivoPublicacao
	 * @param linkArquivoPublicacao
	 * @param arquivoPublicacao
	 * @param ativo
	 * @param anexo
	 * @param quantidadeTentativas
	 */
	public Publicacao(Long idPublicacao, Fonte fonte, String nomePublicacao, Date dataPublicacao,
			String codigoPublicacao, String nomeArquivoPublicacao, String linkArquivoPublicacao,
			String arquivoPublicacao, Date dataCriacao, String usuarioCriacao, Date dataAtualizacao,
			String usuarioAtualizacao, Boolean ativo, Boolean anexo, Long quantidadeTentativas) {
		this(fonte, nomePublicacao, dataPublicacao, codigoPublicacao, nomeArquivoPublicacao, linkArquivoPublicacao,
				arquivoPublicacao, ativo, anexo, quantidadeTentativas);
		this.idPublicacao = idPublicacao;
	}

	/**
	 * @return the idPublicacao
	 */
	public Long getIdPublicacao() {
		return idPublicacao;
	}

	/**
	 * @param idPublicacao the idPublicacao to set
	 */
	public void setIdPublicacao(Long idPublicacao) {
		this.idPublicacao = idPublicacao;
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
	 * @return the nomePublicacao
	 */
	public String getNomePublicacao() {
		return nomePublicacao;
	}

	/**
	 * @param nomePublicacao the nomePublicacao to set
	 */
	public void setNomePublicacao(String nomePublicacao) {
		this.nomePublicacao = nomePublicacao;
	}

	/**
	 * @return the dataPublicacao
	 */
	public Date getDataPublicacao() {
		return dataPublicacao;
	}

	/**
	 * @param dataPublicacao the dataPublicacao to set
	 */
	public void setDataPublicacao(Date dataPublicacao) {
		this.dataPublicacao = dataPublicacao;
	}

	/**
	 * @return the codigoPublicacao
	 */
	public String getCodigoPublicacao() {
		return codigoPublicacao;
	}

	/**
	 * @param codigoPublicacao the codigoPublicacao to set
	 */
	public void setCodigoPublicacao(String codigoPublicacao) {
		this.codigoPublicacao = codigoPublicacao;
	}

	/**
	 * @return the nomeArquivoPublicacao
	 */
	public String getNomeArquivoPublicacao() {
		return nomeArquivoPublicacao;
	}

	/**
	 * @param nomeArquivoPublicacao the nomeArquivoPublicacao to set
	 */
	public void setNomeArquivoPublicacao(String nomeArquivoPublicacao) {
		this.nomeArquivoPublicacao = nomeArquivoPublicacao;
	}

	/**
	 * @return the linkArquivoPublicacao
	 */
	public String getLinkArquivoPublicacao() {
		return linkArquivoPublicacao;
	}

	/**
	 * @param linkArquivoPublicacao the linkArquivoPublicacao to set
	 */
	public void setLinkArquivoPublicacao(String linkArquivoPublicacao) {
		this.linkArquivoPublicacao = linkArquivoPublicacao;
	}

	/**
	 * @return the arquivoPublicacao
	 */
	public String getArquivoPublicacao() {
		return arquivoPublicacao;
	}

	/**
	 * @param arquivoPublicacao the arquivoPublicacao to set
	 */
	public void setArquivoPublicacao(String arquivoPublicacao) {
		this.arquivoPublicacao = arquivoPublicacao;
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
}
