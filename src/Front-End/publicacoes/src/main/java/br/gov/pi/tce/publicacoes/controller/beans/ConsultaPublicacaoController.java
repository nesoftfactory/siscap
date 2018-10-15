package br.gov.pi.tce.publicacoes.controller.beans;

import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;

import br.gov.pi.tce.publicacoes.clients.FonteServiceClient;
import br.gov.pi.tce.publicacoes.clients.PublicacaoServiceClient;
import br.gov.pi.tce.publicacoes.modelo.Fonte;
import br.gov.pi.tce.publicacoes.modelo.Publicacao;

@Named
@ViewScoped
public class ConsultaPublicacaoController extends BeanController {

	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = Logger.getLogger(ConsultaPublicacaoController.class);
	
	private List<Fonte> fontes = Collections.EMPTY_LIST;

	private List<Publicacao> publicacoes;
	
	private String dataInicio;
	
	private String nome;
	
	private Fonte fonte;
	
	private String dataFim;
	
	private Boolean sucesso;
	
	@Inject
	private PublicacaoServiceClient publicacaoServiceClient;
	
	@Inject
	private FonteServiceClient fonteServiceClient;
	


	@PostConstruct
	public void init() {
		limpar();
		iniciaFontes();
		iniciaPublicacoes();
	}
	
	
	public void consultar() {
		try {
			publicacoes = publicacaoServiceClient.consultarPublicacaoPorFiltro(fonte!=null?fonte.getId():null, nome, dataInicio, dataFim,sucesso);
		}
		catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR,  "", e.getMessage());
		}
	}
	
	
	private void iniciaFontes() {
		try {
			fontes = fonteServiceClient.consultarTodasFontes();
		}
		catch (Exception e) {
			registrarMensagem(FacesMessage.SEVERITY_ERROR, "label.erro", e.getMessage());
		}
	}
	
	
	
	public Fonte getFonte() {
		return fonte;
	}


	public void setFonte(Fonte fonte) {
		this.fonte = fonte;
	}


	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


	public Boolean getSucesso() {
		return sucesso;
	}





	public void setSucesso(Boolean sucesso) {
		this.sucesso = sucesso;
	}





	public List<Fonte> getFontes() {
		return fontes;
	}




	public void setFontes(List<Fonte> fontes) {
		this.fontes = fontes;
	}




	public String getDataInicio() {
		return dataInicio;
	}




	public void setDataInicio(String dataInicio) {
		this.dataInicio = dataInicio;
	}




	public String getDataFim() {
		return dataFim;
	}




	public void setDataFim(String dataFim) {
		this.dataFim = dataFim;
	}




	public List<SelectItem> getFontesParaSelectItems(){
		return getSelectItens(fontes, "nome");
	}
	

	public List<Publicacao> getPublicacoes() {
		return publicacoes;
	}



	public void setPublicacoes(List<Publicacao> publicacoes) {
		this.publicacoes = publicacoes;
	}

	

	
	private void iniciaPublicacoes() {
		try {
			publicacoes = publicacaoServiceClient.consultarTodasPublicacoes();
		}
		catch (Exception e) {
			registrarMensagem(FacesMessage.SEVERITY_ERROR, "label.erro", e.getMessage());
		}
	}


	public void limpar() {
		this.dataInicio = "";		
		this.nome = nome;		
		this.fonte = null;		
		this.dataFim = "";		
		this.sucesso = null;
		
	}
	
 

}