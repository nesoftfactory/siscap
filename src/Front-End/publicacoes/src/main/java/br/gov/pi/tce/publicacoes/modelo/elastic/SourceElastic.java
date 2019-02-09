package br.gov.pi.tce.publicacoes.modelo.elastic;

public class SourceElastic {
	
	Long num_pagina;
    String nome_publicacao;
    Long id;
    String data_publicacao;
    String nome_fonte;
    
    
    
	public Long getNum_pagina() {
		return num_pagina;
	}
	public void setNum_pagina(Long num_pagina) {
		this.num_pagina = num_pagina;
	}
	public String getNome_publicacao() {
		return nome_publicacao;
	}
	public void setNome_publicacao(String nome_publicacao) {
		this.nome_publicacao = nome_publicacao;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getData_publicacao() {
		return data_publicacao;
	}
	public void setData_publicacao(String data_publicacao) {
		this.data_publicacao = data_publicacao;
	}
	public String getNome_fonte() {
		return nome_fonte;
	}
	public void setNome_fonte(String nome_fonte) {
		this.nome_fonte = nome_fonte;
	}
    
    
    

}
