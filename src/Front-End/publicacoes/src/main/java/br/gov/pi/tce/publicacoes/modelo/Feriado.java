package br.gov.pi.tce.publicacoes.modelo;

import java.text.SimpleDateFormat;
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
	private Boolean geral;
	private Boolean fixo;

	public Feriado() {
		super();
	}
	
	public Feriado(String nome, String data, List<Fonte> fontes, Boolean ativo, Boolean geral, Boolean fixo) {
		super();
		setNome(nome);
		setData(data);
		setData(data);
		setFontes(fontes);
		setAtivo(ativo);
		setGeral(geral);
		setFixo(fixo);
	}
	
	public Feriado(Long id, String nome, String data, List<Fonte> fontes, Boolean ativo, Boolean geral, Boolean fixo) {
		this(nome, data, fontes, ativo, geral, fixo);
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
	 * @return geral
	 */
	public Boolean getGeral() {
		return geral;
	}

	/**
	 * @param geral
	 */
	public void setGeral(Boolean geral) {
		this.geral = geral;
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
	public String getTextoGeral() {
		return getGeral() ? "Sim" : "Não";
	}
	
	@JsonIgnore
	public String getTextoFixo() {
		return getFixo() ? "Sim" : "Não";
	}
		
}