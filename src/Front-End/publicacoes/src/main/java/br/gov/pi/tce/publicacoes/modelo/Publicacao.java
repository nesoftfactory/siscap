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
	private String nome;
	private Fonte fonte;
	private String data;
	private String codigo;
	private Long idArquivo;
	private Boolean sucesso;
	private Boolean possuiAnexo;
	private Long quantidadeTentativas;
	private Publicacao arquivoAnexo;//remover

	/**
	 * 
	 */
	public Publicacao() {
		super();
	}

	/**
	 * @param fonte
	 * @param nome
	 * @param data
	 * @param codigo
	 * @param idArquivo
	 * @param sucesso
	 * @param anexo
	 * @param quantidadeTentativas
	 */
	public Publicacao(Fonte fonte, String nome, String data, String codigo,
			Long idArquivo, Boolean sucesso, Boolean anexo,
			Long quantidadeTentativas, Publicacao arquivoAnexo) {
		super();
		setFonte(fonte);
		setNome(nome);
		setData(data);
		setCodigo(codigo);
		setIdArquivo(idArquivo);
		setSucesso(sucesso);
		setPossuiAnexo(anexo);
		setQuantidadeTentativas(quantidadeTentativas);
		setArquivoAnexo(arquivoAnexo);
	}

	/**
	 * @param id
	 * @param fonte
	 * @param nome
	 * @param data
	 * @param codigo
	 * @param idArquivo
	 * @param sucesso
	 * @param anexo
	 * @param quantidadeTentativas
	 */
	public Publicacao(Long id, Fonte fonte, String nome, String data,
			String codigo, Long idArquivo, Date dataCriacao, String usuarioCriacao, Date dataAtualizacao,
			String usuarioAtualizacao, Boolean sucesso, Boolean anexo, Long quantidadeTentativas, Publicacao arquivoAnexo) {
		this(fonte, nome, data, codigo, idArquivo, sucesso, anexo, quantidadeTentativas, arquivoAnexo);
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
	public String getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(String data) {
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
	 * @return the possuiAnexo
	 */
	public Boolean getPossuiAnexo() {
		return possuiAnexo;
	}

	/**
	 * @param possuiAnexo the possuiAnexo to set
	 */
	public void setPossuiAnexo(Boolean possuiAnexo) {
		this.possuiAnexo = possuiAnexo;
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

	/**
	 * @return the idArquivo
	 */
	public Long getIdArquivo() {
		return idArquivo;
	}

	/**
	 * @param idArquivo the idArquivo to set
	 */
	public void setIdArquivo(Long idArquivo) {
		this.idArquivo = idArquivo;
	}
}
