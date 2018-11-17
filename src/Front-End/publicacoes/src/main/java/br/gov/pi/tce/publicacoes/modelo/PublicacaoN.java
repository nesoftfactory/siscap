package br.gov.pi.tce.publicacoes.modelo;

/**
 * 
 * Classe responsável por representar a entidade Publicacao.
 * 
 * 
 * @author Erick Guilherme Cavalcanti
 *
 */
public class PublicacaoN {

	private Long id;

	/**
	 * 
	 */
	public PublicacaoN() {
		super();
	}

	/**
	 * @param id
	 */
	public PublicacaoN(Long id) {

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

}
