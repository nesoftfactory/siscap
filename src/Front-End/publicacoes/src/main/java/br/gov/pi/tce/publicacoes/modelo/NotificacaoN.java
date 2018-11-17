package br.gov.pi.tce.publicacoes.modelo;

import java.util.List;

import br.gov.pi.tce.publicacoes.modelo.enums.NotificacaoTipo;

/**
 * 
 * Classe responsável por representar a entidade Notificacao.
 * 
 * 
 * @author Erick Guilherme Cavalcanti
 *
 */
public class NotificacaoN {

	private Long id;
	private NotificacaoTipo tipo;
	private List<UsuarioN> usuarios;
	private PublicacaoN publicacao;
	private String texto;

	/**
	 * 
	 */
	public NotificacaoN() {
		super();
	}

	public NotificacaoN(NotificacaoTipo tipo, List<UsuarioN> usuarios, PublicacaoN publicacao, String texto) {
		super();
		setTipo(tipo);
		setUsuarios(usuarios);
		setPublicacao(publicacao);
		setTexto(texto);
	}

	public NotificacaoN(Long id, NotificacaoTipo tipo, List<UsuarioN> usuarios, PublicacaoN publicacao, String texto) {
		this.id = id;
		setTipo(tipo);
		setUsuarios(usuarios);
		setPublicacao(publicacao);
		setTexto(texto);
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
	 * @return the publicacao
	 */
	public PublicacaoN getPublicacao() {
		return publicacao;
	}

	/**
	 * @param publicacao the publicacao to set
	 */
	public void setPublicacao(PublicacaoN publicacao) {
		this.publicacao = publicacao;
	}

	/**
	 * @return the texto
	 */
	public String getTexto() {
		return texto;
	}

	/**
	 * @param texto the texto to set
	 */
	public void setTexto(String texto) {
		this.texto = texto;
	}

}
