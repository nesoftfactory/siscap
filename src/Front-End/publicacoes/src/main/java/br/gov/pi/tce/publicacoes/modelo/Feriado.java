package br.gov.pi.tce.publicacoes.modelo;

/**
 * Classe responsável por representar a entidade Feriado.
 * 
 * @author Erick Guilherme Cavalcanti
 *
 */
public class Feriado {
	
	private Long id;
	private String nome;
	private String data;
	private Fonte fonte;
	private Boolean ativo;

	public Feriado() {
		super();
	}
	
	public Feriado(String nome, String data, Fonte fonte, Boolean ativo) {
		super();
		this.nome = nome;
		this.data = data;
		this.fonte = fonte;
		this.ativo = ativo;
	}
	
	public Feriado(Long id, String nome, String data, Fonte fonte, Boolean ativo) {
		this(nome, data, fonte, ativo);
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