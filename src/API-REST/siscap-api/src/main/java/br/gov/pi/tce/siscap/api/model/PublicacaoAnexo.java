package br.gov.pi.tce.siscap.api.model;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="publicacao_anexo")
public class PublicacaoAnexo extends BaseEntity {
	
	private Publicacao publicacao;
	private String nome;
	private Arquivo arquivo;
	private boolean sucesso;
	
	
	public PublicacaoAnexo() {
		super();
	}
	
	
	public PublicacaoAnexo(Long id, LocalDateTime dataCriacao, Usuario usuarioCriacao, LocalDateTime dataAtualizacao, Usuario usuarioAtualizacao, String nome, boolean sucesso, Long idPublicacao, Long idArquivo) {
		super();
		setId(id);
		setDataCriacao(dataCriacao);
		setUsuarioCriacao(usuarioCriacao);
		setDataAtualizacao(dataAtualizacao);
		setUsuarioAtualizacao(usuarioAtualizacao);
		setNome(nome);
		setSucesso(sucesso);
		Publicacao p = new Publicacao();
		p.setId(idPublicacao);
		setPublicacao(p);
		Arquivo a = new Arquivo();
		a.setId(idArquivo);
		setArquivo(a);
	}
	
	
	@NotNull
	@Size(min=3, max=50)
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="id_publicacao")
	public Publicacao getPublicacao() {
		return publicacao;
	}

	public void setPublicacao(Publicacao publicacao) {
		this.publicacao = publicacao;
	}

	

	public boolean getSucesso() {
		return sucesso;
	}

	public void setSucesso(boolean sucesso) {
		this.sucesso = sucesso;
	}

	
	@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
	@JsonIdentityReference(alwaysAsId=true)
	@ManyToOne(optional=true, cascade=CascadeType.ALL)
	@JoinColumn(name="id_arquivo")
	public Arquivo getArquivo() {
		return arquivo;
	}

	public void setArquivo(Arquivo arquivo) {
		this.arquivo = arquivo;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
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
		PublicacaoAnexo other = (PublicacaoAnexo) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}
	
}
