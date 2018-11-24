package br.gov.pi.tce.publicacoes.modelo;

import java.util.List;

import br.gov.pi.tce.publicacoes.modelo.enums.NotificacaoTipo;

/**
 * 
 * Classe responsável por representar a entidade NotificacaoConfig.
 * 
 * 
 * @author Erick Guilherme Cavalcanti
 *
 */
public class NotificacaoConfig {

	private Long id;
	private NotificacaoTipo tipo;
	private List<UsuarioN> usuarios;
	private Boolean ativo;
	private int quantidadeTentativas;

	/**
	 * 
	 */
	public NotificacaoConfig() {
		super();
	}

	public NotificacaoConfig(NotificacaoTipo tipo, List<UsuarioN> usuarios, Boolean ativo, int quantidadeTentativas) {
		super();
		setTipo(tipo);
		setUsuarios(usuarios);
		setAtivo(ativo);
		setQuantidadeTentativas(quantidadeTentativas);
	}

	public NotificacaoConfig(Long id, NotificacaoTipo tipo, List<UsuarioN> usuarios, Boolean ativo,
			int quantidadeTentativas) {
		this.id = id;
		setTipo(tipo);
		setUsuarios(usuarios);
		setAtivo(ativo);
		setQuantidadeTentativas(quantidadeTentativas);
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
	 * @return the tipo
	 */
	public NotificacaoTipo getTipo() {
		return tipo;
	}

	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(NotificacaoTipo tipo) {
		this.tipo = tipo;
	}

	/**
	 * @return the usuarios
	 */
	public List<UsuarioN> getUsuarios() {
		return usuarios;
	}

	/**
	 * @param usuarios the usuarios to set
	 */
	public void setUsuarios(List<UsuarioN> usuarios) {
		this.usuarios = usuarios;
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
	 * @return the quantidadeTentativas
	 */
	public int getQuantidadeTentativas() {
		return quantidadeTentativas;
	}

	/**
	 * @param quantidadeTentativas the quantidadeTentativas to set
	 */
	public void setQuantidadeTentativas(int quantidadeTentativas) {
		this.quantidadeTentativas = quantidadeTentativas;
	}

}
