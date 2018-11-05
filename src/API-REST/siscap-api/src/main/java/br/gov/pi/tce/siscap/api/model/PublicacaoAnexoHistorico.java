package br.gov.pi.tce.siscap.api.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="publicacao_anexo_historico")
public class PublicacaoAnexoHistorico  {
	
	private Long id;
	private PublicacaoAnexo publicacaoAnexo;
	private String mensagem;
	private boolean sucesso;
	private LocalDateTime dataCriacao;
	private Usuario usuarioCriacao;

	public PublicacaoAnexoHistorico() {
	}

	public PublicacaoAnexoHistorico(PublicacaoAnexo publicacaoAnexo, String mensagem, boolean sucesso,Usuario usuarioLogado) {
		this.publicacaoAnexo = publicacaoAnexo;
		this.mensagem = mensagem;
		this.sucesso = sucesso;
		this.usuarioCriacao= usuarioLogado;
	}
	
	public PublicacaoAnexoHistorico(String mensagem, boolean sucesso,LocalDateTime dataCriacao, Usuario usuarioCriacao) {
		this.mensagem = mensagem;
		this.sucesso = sucesso;
		this.dataCriacao = dataCriacao;
		this.usuarioCriacao= usuarioCriacao;
	}



	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
		
	@NotNull
	public String getMensagem() {
		return mensagem;
	}
	
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	
	

	public boolean getSucesso() {
		return sucesso;
	}

	public void setSucesso(boolean sucesso) {
		this.sucesso = sucesso;
	}

	@NotNull
	@ManyToOne
	@JoinColumn(name="id_publicacao_anexo")
	public PublicacaoAnexo getPublicacaoAnexo() {
		return publicacaoAnexo;
	}

	public void setPublicacaoAnexo(PublicacaoAnexo publicacaoAnexo) {
		this.publicacaoAnexo = publicacaoAnexo;
	}

	@Column(name="data_criacao")
		public LocalDateTime getDataCriacao() {
		return dataCriacao;
	}
	
	public void setDataCriacao(LocalDateTime dataCriacao) {
		this.dataCriacao = dataCriacao;
	}
	
	@OneToOne
	@JoinColumn( name = "usuario_criacao" )
	public Usuario getUsuarioCriacao() {
		return usuarioCriacao;
	}
	
	public void setUsuarioCriacao(Usuario usuarioCriacao) {
		this.usuarioCriacao = usuarioCriacao;
	}

	@PrePersist
	public void prePersist() {
		final LocalDateTime atual = LocalDateTime.now();
		this.dataCriacao = atual;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PublicacaoAnexoHistorico other = (PublicacaoAnexoHistorico) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Transient
	public String getDataCriacaoString() {
		if(getDataCriacao() != null) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	        String dataCriacaoFormatada = getDataCriacao().format(formatter);
			return dataCriacaoFormatada;
		}
		else {
			return "";
		}
		
	}

}

