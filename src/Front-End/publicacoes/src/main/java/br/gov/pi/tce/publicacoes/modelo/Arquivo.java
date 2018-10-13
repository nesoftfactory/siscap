package br.gov.pi.tce.publicacoes.modelo;

import java.io.InputStream;

/**
 * Classe responsável por representar a entidade Arquivo.
 * 
 * @author Erick Guilherme Cavalcanti
 *
 */
public class Arquivo {

	private Long id;
	private String nome;
	private Long tamanho;
	private String tipo;
	private String link;
	private byte[] conteudo;
    private InputStream inputStream;
	/**
	 * 
	 */
	public Arquivo() {
		super();
		
		// TODO Auto-generated constructor stub
		
	}

	/**
	 * @param nome
	 * @param tamanho
	 * @param tipo
	 * @param link
	 * @param conteudo
	 */
	public Arquivo(String nome, Long tamanho, String tipo, String link, byte[] conteudo) {
		super();
		setNome(nome);
		setTamanho(tamanho);
		setTipo(tipo);
		setLink(link);
		setConteudo(conteudo);
	}

	/**
	 * @param id
	 * @param nome
	 * @param tamanho
	 * @param tipo
	 * @param link
	 * @param conteudo
	 */
	public Arquivo(Long id, String nome, Long tamanho, String tipo, String link, byte[] conteudo) {
		this(nome, tamanho, tipo, link, conteudo);
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
	 * @return the tamanho
	 */
	public Long getTamanho() {
		return tamanho;
	}

	/**
	 * @param tamanho the tamanho to set
	 */
	public void setTamanho(Long tamanho) {
		this.tamanho = tamanho;
	}

	/**
	 * @return the tipo
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	/**
	 * @return the link
	 */
	public String getLink() {
		return link;
	}

	/**
	 * @param link the link to set
	 */
	public void setLink(String link) {
		this.link = link;
	}

	/**
	 * @return the conteudo
	 */
	public byte[] getConteudo() {
		return conteudo;
	}

	/**
	 * @param conteudo the conteudo to set
	 */
	public void setConteudo(byte[] conteudo) {
		this.conteudo = conteudo;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
}
