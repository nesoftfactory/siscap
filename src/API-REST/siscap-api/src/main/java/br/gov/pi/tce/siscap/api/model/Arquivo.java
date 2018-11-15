package br.gov.pi.tce.siscap.api.model;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name="arquivo")
public class Arquivo extends BaseEntity {

	private String nome;
	private Long tamanho;
	private String tipo;
	private String link;
	private byte[] conteudo;

	public Arquivo() {
	}
	
	
	public Arquivo(Long id, String nome,  Long tamanho, String tipo, String link, byte[] conteudo) throws IOException {
		this.setId(id);
		this.setNome(nome);
		this.setTamanho(tamanho);
		this.setConteudo(conteudo);
		this.setTipo(tipo);
		this.setLink(link);
	}
	
	public Arquivo(MultipartFile partFile, String link, Usuario usuario) throws IOException {
		this.setNome(partFile.getOriginalFilename());
		this.setTamanho(partFile.getSize());
		this.setConteudo(partFile.getBytes());
		this.setTipo(partFile.getContentType());
		this.setLink(link);
		this.setDataAtualizacao(LocalDateTime.now());
		this.setDataCriacao(LocalDateTime.now());
		this.setUsuarioCriacao(usuario);
		this.setUsuarioAtualizacao(usuario);
	}

	@Lob
	@Column(nullable = false)
	public byte[] getConteudo() {
		return conteudo;
	}

	public void setConteudo(byte[] conteudo) {
		this.conteudo = conteudo;
	}

	@Column(name = "NOME", length = 255, nullable = false)
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Column(name = "TAMANHO", nullable = true)
	public Long getTamanho() {
		return tamanho;
	}

	public void setTamanho(Long tamanho) {
		this.tamanho = tamanho;
	}

	@Column(name = "TIPO", length = 100, nullable = false)
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
	
	@Override
	public String toString() {
		return nome;
	}
}
