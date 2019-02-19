package br.gov.pi.tce.siscap.timer.model;

import java.time.LocalDate;
import java.util.Date;

public class Publicacao {

	private Long id;
	private String nome;
	private Fonte fonte;
	private LocalDate data;
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

	public Publicacao() {
		super();
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}


	public Publicacao(Long id, Fonte fonte, String nome, LocalDate data, String codigo, Long arquivo, Boolean sucesso,
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
	
	
	public Publicacao(Fonte fonte, String nome, LocalDate data, String codigo, Long arquivo, Boolean sucesso,
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

	public Publicacao(Long id, Fonte fonte, String nome, LocalDate data, String codigo, Long arquivo,
			String usuarioCriacao, Date dataAtualizacao, String usuarioAtualizacao, Boolean sucesso, Boolean anexo,
			Long quantidadeTentativas) {
		this(fonte, nome, data, codigo, arquivo, sucesso, anexo, quantidadeTentativas);
		this.id = id;
	}
	
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Fonte getFonte() {
		return fonte;
	}

	public void setFonte(Fonte fonte) {
		this.fonte = fonte;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Boolean getSucesso() {
		return sucesso;
	}

	public void setSucesso(Boolean sucesso) {
		this.sucesso = sucesso;
	}

	public Boolean getPossuiAnexo() {
		return possuiAnexo;
	}

	public void setPossuiAnexo(Boolean possuiAnexo) {
		this.possuiAnexo = possuiAnexo;
	}

	public Long getQuantidadeTentativas() {
		return quantidadeTentativas;
	}

	public void setQuantidadeTentativas(Long quantidadeTentativas) {
		this.quantidadeTentativas = quantidadeTentativas;
	}

	public Long getArquivo() {
		return arquivo;
	}

	public void setArquivo(Long arquivo) {
		this.arquivo = arquivo;
	}

	public Boolean getPossuiNotificacao() {
		return possuiNotificacao;
	}

	public void setPossuiNotificacao(Boolean possuiNotificacao) {
		this.possuiNotificacao = possuiNotificacao;
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

	@Override
	public String toString() {
		return (this.getFonte() != null ? fonte.getNome() + " - " : "") + this.getData();
	}
}
