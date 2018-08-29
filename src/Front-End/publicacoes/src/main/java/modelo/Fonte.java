package modelo;

public class Fonte {
	
	private Long id;
	private String descricao;
	private String esfera;
	private String url;

	
	

	public Fonte() {
		super();
	}
	
	
	public Fonte(String descricao, String esfera, String url) {
		super();
		this.descricao = descricao;
		this.esfera = esfera;
		this.url = url;
	}
	

	public Fonte(Long id, String descricao, String esfera, String url) {
		this(descricao,esfera,url);
		this.id = id;
	}
	
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getEsfera() {
		return esfera;
	}

	public void setEsfera(String esfera) {
		this.esfera = esfera;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}