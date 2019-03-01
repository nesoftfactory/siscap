package br.gov.pi.tce.publicacoes.controller.beans;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;

import br.gov.pi.tce.publicacoes.clients.AnexoPublicacaoHistoricoServiceClient;
import br.gov.pi.tce.publicacoes.modelo.AnexoPublicacaoHistorico;

@Named
@ViewScoped
public class HistoricoAnexoPublicacaoController extends BeanController {

	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = Logger.getLogger(HistoricoAnexoPublicacaoController.class);
	
	
	@Inject
	private AnexoPublicacaoHistoricoServiceClient anexoPublicacaoHistoricoServiceClient;
	
	
	private List<AnexoPublicacaoHistorico> historicoAnexoPublicacaoSelecionada;
	
	
	@PostConstruct
	public void init() {
		try {
			popupHistorico();
		}	
		catch (EJBException e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "Serviço indisponível: Histórico de anexo de documento.", e.getMessage());
			LOGGER.error("Erro ao iniciar histórico de anexo de documento.:" + e.getMessage());
			e.printStackTrace();
		}
		 catch (IOException e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao iniciar histórico de anexo de documento.", e.getMessage());
			LOGGER.error("Erro ao iniciar histórico de anexo de documento.:" + e.getMessage());
			e.printStackTrace();
		}
		catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao iniciar usuários.", e.getMessage());
			LOGGER.error("Erro ao iniciar histórico de anexo de documento.:" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	

	public void popupHistorico() throws IOException {
		String  idPublicacaoAnexo = (String)FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
		historicoAnexoPublicacaoSelecionada = anexoPublicacaoHistoricoServiceClient.consultarAnexoPublicacaoHistoricoPeloIdAnexoPublicacao(Long.valueOf(idPublicacaoAnexo));
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("historicoAnexoPublicacaoSelecionada", historicoAnexoPublicacaoSelecionada);
	}



	public List<AnexoPublicacaoHistorico> getHistoricoAnexoPublicacaoSelecionada() {
		return historicoAnexoPublicacaoSelecionada;
	}



	public void setHistoricoAnexoPublicacaoSelecionada(List<AnexoPublicacaoHistorico> historicoAnexoPublicacaoSelecionada) {
		this.historicoAnexoPublicacaoSelecionada = historicoAnexoPublicacaoSelecionada;
	}



	
	
	
	
}