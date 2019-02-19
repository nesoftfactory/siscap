package br.gov.pi.tce.siscap.timer.model;

import java.util.List;

import br.gov.pi.tce.siscap.timer.model.enums.NotificacaoTipo;

public class NotificacaoConfig {

	private Long id;
	private NotificacaoTipo tipo;
	private List<Usuario> usuarios;
	private Boolean ativo;
	private int quantidadeTentativas;

	public NotificacaoConfig() {
		super();
	}

	public NotificacaoConfig(NotificacaoTipo tipo, List<Usuario> usuarios, Boolean ativo, int quantidadeTentativas) {
		super();
		setTipo(tipo);
		setUsuarios(usuarios);
		setAtivo(ativo);
		setQuantidadeTentativas(quantidadeTentativas);
	}

	public NotificacaoConfig(Long id, NotificacaoTipo tipo, List<Usuario> usuarios, Boolean ativo,
			int quantidadeTentativas) {
		this.id = id;
		setTipo(tipo);
		setUsuarios(usuarios);
		setAtivo(ativo);
		setQuantidadeTentativas(quantidadeTentativas);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public NotificacaoTipo getTipo() {
		return tipo;
	}

	public void setTipo(NotificacaoTipo tipo) {
		this.tipo = tipo;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public int getQuantidadeTentativas() {
		return quantidadeTentativas;
	}

	public void setQuantidadeTentativas(int quantidadeTentativas) {
		this.quantidadeTentativas = quantidadeTentativas;
	}

}
