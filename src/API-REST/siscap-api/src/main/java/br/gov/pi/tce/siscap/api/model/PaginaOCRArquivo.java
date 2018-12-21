package br.gov.pi.tce.siscap.api.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="pagina_ocr_arquivo")
public class PaginaOCRArquivo extends BaseEntity {

	private String ocr;
	private Integer pagina;
	private Arquivo arquivo;

	public PaginaOCRArquivo() {
	}
	
	
	public PaginaOCRArquivo(Integer pagina, String ocr, Arquivo arquivo) {
		super();
		this.ocr = ocr;
		this.pagina = pagina;
		this.arquivo = arquivo;
	}

	public PaginaOCRArquivo(Integer pagina, String ocr, Arquivo arquivo, Usuario usuarioCriacao, Usuario usuarioAtualizacao) {
		this(pagina,ocr,arquivo);
		setUsuarioCriacao(usuarioCriacao);
		setUsuarioAtualizacao(usuarioAtualizacao);
	}
	

	@Column(name = "OCR", length = 8000, nullable = false)
	public String getOcr() {
		return ocr;
	}

	public void setOcr(String ocr) {
		this.ocr = ocr;
	}



	@Column(name = "PAGINA", nullable = false)
	public Integer getPagina() {
		return pagina;
	}




	public void setPagina(Integer pagina) {
		this.pagina = pagina;
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


}
