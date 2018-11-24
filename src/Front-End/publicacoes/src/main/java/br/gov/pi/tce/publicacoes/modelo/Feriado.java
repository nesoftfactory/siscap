package br.gov.pi.tce.publicacoes.modelo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Classe responsável por representar a entidade Feriado.
 * 
 * @author Erick Guilherme Cavalcanti
 *
 */
public class Feriado {

	private Long id;
	private String nome;
	private String data;
	private List<Fonte> fontes;
	private Boolean ativo;
	private Boolean todasFontes;
	private Boolean fixo;

	public Feriado() {
		super();
	}

	public Feriado(String nome, String data, List<Fonte> fontes, Boolean ativo, Boolean todasFontes, Boolean fixo) {
		super();
		setNome(nome);
		setData(data);
		setData(data);
		setFontes(fontes);
		setAtivo(ativo);
		setTodasFontes(todasFontes);
		setFixo(fixo);
	}

	public Feriado(Long id, String nome, String data, List<Fonte> fontes, Boolean ativo, Boolean todasFontes,
			Boolean fixo) {
		this(nome, data, fontes, ativo, todasFontes, fixo);
		this.id = id;
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
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @param nome the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * @return the data
	 */
	public String getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(String data) {
		this.data = data;
	}

	/**
	 * @return the fontes
	 */
	public List<Fonte> getFontes() {
		return fontes;
	}

	/**
	 * @param fonte the fonte to set
	 */
	public void setFontes(List<Fonte> fontes) {
		this.fontes = fontes;
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
	 * @return todasFontes
	 */
	public Boolean getTodasFontes() {
		return todasFontes;
	}

	/**
	 * @param todasFontes
	 */
	public void setTodasFontes(Boolean todasFontes) {
		this.todasFontes = todasFontes;
	}

	/**
	 * @return fixo
	 */
	public Boolean getFixo() {
		return fixo;
	}

	/**
	 * @param fixo
	 */
	public void setFixo(Boolean fixo) {
		this.fixo = fixo;
	}

	@JsonIgnore
	public String getTextoAtivo() {
		return getAtivo() ? "Sim" : "Não";
	}

	@JsonIgnore
	public String getTextoTodasFontes() {
		return getTodasFontes() ? "Sim" : "Não";
	}

	@JsonIgnore
	public String getTextoFixo() {
		return getFixo() ? "Sim" : "Não";
	}
	
	@JsonIgnore
	public void ajustaFormatoDataParaAPI() {
		if(data != null && data.length() == 10) {
			String[] dataSplit = data.split("/");
			setData((dataSplit[2]+"-"+dataSplit[1]+"-"+dataSplit[0]));
		}
	}

}