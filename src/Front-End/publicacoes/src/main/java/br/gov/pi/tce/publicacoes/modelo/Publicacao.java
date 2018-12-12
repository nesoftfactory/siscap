package br.gov.pi.tce.publicacoes.modelo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 
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
	private String dataCriacao;
	private String codigo;
	private Long arquivo;
	private Boolean sucesso;
	private Boolean possuiAnexo;
	private Long quantidadeTentativas = 0L;
	private Boolean possuiNotificacao;
	private Usuario usuarioCriacao;
	private String dataString;

	private PublicacaoAnexo publicacaoAnexo;
	private Long quantidadeTentativasOCR = 0L;
	private Long quantidadeTentativasIndexacao = 0L;
	private String situacao;

	/**
	 * 
	 */
	public Publicacao() {
		super();
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}


	/**
	 * @param id
	 * @param fonte
	 * @param nome
	 * @param data
	 * @param codigo
	 * @param arquivo
	 * @param sucesso
	 * @param anexo
	 * @param quantidadeTentativas
	 * @param dataCriacao
	 * @param usuarioCriacao
	 */
	public Publicacao(Long id, Fonte fonte, String nome, String data, String codigo, Long arquivo, Boolean sucesso,
			Boolean anexo, Long quantidadeTentativas, String dataCriacao, Usuario usuarioCriacao) {
		super();
		setId(id);
		setFonte(fonte);
		setNome(nome);
		setData(data);
		setCodigo(codigo);
		setArquivo(arquivo);
		setSucesso(sucesso);
		setPossuiAnexo(anexo);
		setQuantidadeTentativas(quantidadeTentativas);
		setDataCriacao(dataCriacao);
		setUsuarioCriacao(usuarioCriacao);
	}
	
	
//	public Publicacao(Long id, Fonte fonte, String nome, String data, String codigo, Long arquivo, Boolean sucesso,
//			Boolean anexo, Long quantidadeTentativas, String dataCriacao, Usuario usuarioCriacao, String situacao) {
//		this(id,fonte,nome,data,codigo,arquivo,sucesso,anexo,quantidadeTentativas,dataCriacao,usuarioCriacao);
//		this.situacao = situacao;
//	}
	

	/**
	 * @param fonte
	 * @param nome
	 * @param data
	 * @param codigo
	 * @param arquivo
	 * @param sucesso
	 * @param anexo
	 * @param quantidadeTentativas
	 */
	public Publicacao(Fonte fonte, String nome, String data, String codigo, Long arquivo, Boolean sucesso,
			Boolean anexo, Long quantidadeTentativas) {
		super();
		setFonte(fonte);
		setNome(nome);
		setData(data);
		setCodigo(codigo);
		setArquivo(arquivo);
		setSucesso(sucesso);
		setPossuiAnexo(anexo);
		setQuantidadeTentativas(quantidadeTentativas);
	}

	/**
	 * @param id
	 * @param fonte
	 * @param nome
	 * @param data
	 * @param codigo
	 * @param arquivo
	 * @param usuarioCriacao
	 * @param dataAtualizacao
	 * @param usuarioAtualizacao
	 * @param sucesso
	 * @param anexo
	 * @param quantidadeTentativas
	 */
	public Publicacao(Long id, Fonte fonte, String nome, String data, String codigo, Long arquivo,
			String usuarioCriacao, Date dataAtualizacao, String usuarioAtualizacao, Boolean sucesso, Boolean anexo,
			Long quantidadeTentativas) {
		this(fonte, nome, data, codigo, arquivo, sucesso, anexo, quantidadeTentativas);
		this.id = id;
	}
	
	/**
	 * @param id
	 */
	public Publicacao(Long id) {
		super();
		setId(id);
	}
	
	

	public String getDataString() {
		return dataString;
	}

	public void setDataString(String dataString) {
		this.dataString = dataString;
	}

	public String getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(String dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public PublicacaoAnexo getPublicacaoAnexo() {
		return publicacaoAnexo;
	}

	public void setPublicacaoAnexo(PublicacaoAnexo publicacaoAnexo) {
		this.publicacaoAnexo = publicacaoAnexo;
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

	public Boolean getPossuiNotificacao() {
		return possuiNotificacao;
	}

	public void setPossuiNotificacao(Boolean possuiNotificacao) {
		this.possuiNotificacao = possuiNotificacao;
	}

	@JsonIgnore
	public String getTextoSucesso() {
		return getSucesso() ? "Sim" : "Não";
	}

	public Usuario getUsuarioCriacao() {
		return usuarioCriacao;
	}

	public void setUsuarioCriacao(Usuario usuarioCriacao) {
		this.usuarioCriacao = usuarioCriacao;
	}

	public Long getQuantidadeTentativasOCR() {
		return quantidadeTentativasOCR;
	}

	public void setQuantidadeTentativasOCR(Long quantidadeTentativasOCR) {
		this.quantidadeTentativasOCR = quantidadeTentativasOCR;
	}

	public Long getQuantidadeTentativasIndexacao() {
		return quantidadeTentativasIndexacao;
	}

	public void setQuantidadeTentativasIndexacao(Long quantidadeTentativasIndexacao) {
		this.quantidadeTentativasIndexacao = quantidadeTentativasIndexacao;
	}





	




	

}
