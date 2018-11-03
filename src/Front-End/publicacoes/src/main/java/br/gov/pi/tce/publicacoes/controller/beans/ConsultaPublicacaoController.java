package br.gov.pi.tce.publicacoes.controller.beans;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.ViewHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import br.gov.pi.tce.publicacoes.clients.ArquivoServiceClient;
import br.gov.pi.tce.publicacoes.clients.FonteServiceClient;
import br.gov.pi.tce.publicacoes.clients.PublicacaoHistoricoServiceClient;
import br.gov.pi.tce.publicacoes.clients.PublicacaoServiceClient;
import br.gov.pi.tce.publicacoes.modelo.Arquivo;
import br.gov.pi.tce.publicacoes.modelo.Fonte;
import br.gov.pi.tce.publicacoes.modelo.Publicacao;
import br.gov.pi.tce.publicacoes.modelo.PublicacaoHistorico;

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
	
	@Inject
	private ArquivoServiceClient arquivoServiceClient;
	
	@Inject
	private PublicacaoHistoricoServiceClient publicacaoHistoricoServiceClient;
	
	private static final String HEADER_CONTENT_DISPOSITION = "Content-disposition";
	private static final String ATTACHMENT_FILENAME = "attachment; filename=";
	
	private List<PublicacaoHistorico> historicoPublicacaoSelecionada;

	


	@PostConstruct
	public void init() {
		limpar();
		iniciaFontes();
		iniciaPublicacoes();
	}
	
	
	public void consultar() {
		try {
			if(dataInicio == null || dataFim == null) {
				registrarMensagem(FacesMessage.SEVERITY_ERROR, "label.datas.obrigatorias", "");
			}
			else {
				publicacoes = publicacaoServiceClient.consultarPublicacaoPorFiltro(fonte!=null?fonte.getId():null, nome, dataInicio, dataFim,sucesso);
			}
		}
		catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR,  "", e.getMessage());
		}
	}
	
	
	
	
	public List<PublicacaoHistorico> getHistoricoPublicacaoSelecionada() {
		return historicoPublicacaoSelecionada;
	}


	public void setHistoricoPublicacaoSelecionada(List<PublicacaoHistorico> historicoPublicacaoSelecionada) {
		this.historicoPublicacaoSelecionada = historicoPublicacaoSelecionada;
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
			registrarMensagem(FacesMessage.SEVERITY_INFO, "label.sucesso", "");
			registrarMensagem(FacesMessage.SEVERITY_INFO, "label.download.sucesso");
			registrarMensagem(FacesMessage.SEVERITY_ERROR, "label.datas.obrigatorias", "label.datas.obrigatorias");
			publicacoes = publicacaoServiceClient.consultarTodasPublicacoes();
			registrarMensagem(FacesMessage.SEVERITY_ERROR, "label.datas.obrigatorias", "label.datas.obrigatorias");
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
	
	
	public void downloadArquivo(){
		
		Publicacao publicacao = (Publicacao)FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("publicacao");
		Arquivo arquivo = arquivoServiceClient.consultarArquivoPorCodigo(publicacao.getArquivo());
		
		
		ExternalContext econtext = FacesContext.getCurrentInstance().getExternalContext();  
		HttpServletResponse response = (HttpServletResponse) econtext.getResponse();
		
		response.reset();
		response.addHeader(HEADER_CONTENT_DISPOSITION, ATTACHMENT_FILENAME + arquivo.getNome());
		try {
			response.getOutputStream().write(arquivo.getConteudo());
			response.flushBuffer();
		} catch (Exception e) {
			LOGGER.error("Erro realizar o download do arquivo:" + arquivo.getId());
			LOGGER.error(e.getMessage());
		}
		FacesContext.getCurrentInstance().responseComplete();
	}
	
	
	public void getHistoricoPublicacao(){
		Publicacao publicacao = (Publicacao)FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("publicacao");
		historicoPublicacaoSelecionada = publicacaoHistoricoServiceClient.consultarPublicacaoHistoricoPeloIdPublicacao(publicacao.getId());
		LOGGER.error("rerer");
	}
	
	public void downloadArquivoAnexo(){
		
		Publicacao publicacao = (Publicacao)FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("publicacao");
		if(publicacao != null && publicacao.getPossuiAnexo()) {
			Arquivo arquivo = arquivoServiceClient.consultarArquivoPorCodigo(publicacao.getPublicacaoAnexo().getArquivo());
			ExternalContext econtext = FacesContext.getCurrentInstance().getExternalContext();  
			HttpServletResponse response = (HttpServletResponse) econtext.getResponse();
			response.reset();
			response.addHeader(HEADER_CONTENT_DISPOSITION, ATTACHMENT_FILENAME + arquivo.getNome());
			try {
				response.getOutputStream().write(arquivo.getConteudo());
				response.flushBuffer();
			} catch (Exception e) {
				LOGGER.error("Erro realizar o download do arquivo:" + arquivo.getId());
				LOGGER.error(e.getMessage());
			}
			FacesContext.getCurrentInstance().responseComplete();
		}
		
	}
	
	public void popup() throws IOException {
		Publicacao publicacao = (Publicacao)FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("publicacao");
		historicoPublicacaoSelecionada = publicacaoHistoricoServiceClient.consultarPublicacaoHistoricoPeloIdPublicacao(publicacao.getId());
		HttpServletResponse res = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String uri = req.getRequestURI();
        res.getWriter().println("<script>window.open('" + "publicacaoHistorico.xhtml" + "','_blank', 'location=yes,height=600,width=800,scrollbars=yes,status=yes'); window.parent.location.href= '"+uri+"';</script>");
        FacesContext.getCurrentInstance().responseComplete(); 
	}
}