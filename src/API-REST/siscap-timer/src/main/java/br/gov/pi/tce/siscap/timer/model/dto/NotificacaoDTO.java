package br.gov.pi.tce.siscap.timer.model.dto;

import java.time.LocalDateTime;
import java.util.List;

import br.gov.pi.tce.siscap.timer.model.Usuario;

public class NotificacaoDTO {

	private String tipo;
	private List<Usuario> usuarios;
	private Publicacao publicacao;
	private String texto;
	private LocalDateTime dataCriacao;
	private LocalDateTime dataAtualizacao;


	public NotificacaoDTO(String tipo, List<Usuario> usuarios, Long idPublicacao, String texto) {
		setTipo(tipo);
		setUsuarios(usuarios);
		setPublicacao(new Publicacao(idPublicacao));
		setTexto(texto);
		setDataCriacao(LocalDateTime.now());
		setDataAtualizacao(LocalDateTime.now());
	}

	public static class Publicacao {
		private Long id;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Publicacao(Long id) {
			super();
			this.id = id;
		}
		
	}
	
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public Publicacao getPublicacao() {
		return publicacao;
	}

	public void setPublicacao(Publicacao publicacao) {
		this.publicacao = publicacao;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public LocalDateTime getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(LocalDateTime dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public LocalDateTime getDataAtualizacao() {
		return dataAtualizacao;
	}

	public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

}
