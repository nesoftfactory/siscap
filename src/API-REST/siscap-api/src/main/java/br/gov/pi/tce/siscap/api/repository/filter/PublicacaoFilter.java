package br.gov.pi.tce.siscap.api.repository.filter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

public class PublicacaoFilter {

	private Long idFonte;
	private String nome;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private LocalDate data;
	
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private LocalDate dataInicio;
	
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private LocalDate dataFim;
	
	private Boolean sucesso;
	
	private String first;
	
	public String getFirst() {
		return first;
	}

	public void setFirst(String first) {
		this.first = first;
	}

	public String getPageSize() {
		return pageSize;
	}

	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}

	private String pageSize;
	private int primeiroRegistro;
	private int quantidadeRegistros;
	private String propriedadeOrdenacao;
	private boolean ascendente;
	
	public int getPrimeiroRegistro() {
		return primeiroRegistro;
	}

	public void setPrimeiroRegistro(int primeiroRegistro) {
		this.primeiroRegistro = primeiroRegistro;
	}

	public int getQuantidadeRegistros() {
		return quantidadeRegistros;
	}

	public void setQuantidadeRegistros(int quantidadeRegistros) {
		this.quantidadeRegistros = quantidadeRegistros;
	}

	public String getPropriedadeOrdenacao() {
		return propriedadeOrdenacao;
	}

	public void setPropriedadeOrdenacao(String propriedadeOrdenacao) {
		this.propriedadeOrdenacao = propriedadeOrdenacao;
	}

	public boolean isAscendente() {
		return ascendente;
	}

	public void setAscendente(boolean ascendente) {
		this.ascendente = ascendente;
	}

	public Long getIdFonte() {
		return idFonte;
	}
	
	public void setIdFonte(Long idFonte) {
		this.idFonte = idFonte;
	}
	
	public String getNome() {
		return nome;
	}

	public String getNomeDecoded() {
		try {
			return URLDecoder.decode(getNome(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return getNome();
		}
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public LocalDate getData() {
		return data;
	}
	
	public void setData(LocalDate data) {
		this.data = data;
	}

	public LocalDate getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(LocalDate dataInicio) {
		this.dataInicio = dataInicio;
	}

	public LocalDate getDataFim() {
		return dataFim;
	}

	public void setDataFim(LocalDate dataFim) {
		this.dataFim = dataFim;
	}

	public Boolean getSucesso() {
		return sucesso;
	}

	public void setSucesso(Boolean sucesso) {
		this.sucesso = sucesso;
	}
	
	
}
