package br.gov.pi.tce.publicacoes.controller.beans;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;

import br.gov.pi.tce.publicacoes.clients.NotificacaoServiceClient;
import br.gov.pi.tce.publicacoes.modelo.Notificacao;

@Named
@ViewScoped
public class NotificacaoController extends BeanController {

	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = Logger.getLogger(NotificacaoController.class);
	
	
	@Inject
	private NotificacaoServiceClient notificacaoServiceClient;
	
	private List<Notificacao> historicoNotificacoesPublicacaoSelecionada;
	
	@PostConstruct
	public void init() {
		try {
			popupNotificacoes();
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}
	
	

	public void popupNotificacoes() throws Exception {
		String  idPublicacao = (String)FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
		historicoNotificacoesPublicacaoSelecionada = notificacaoServiceClient.consultarNotificacoesPorIdPublicacao(Long.valueOf(idPublicacao));
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("historicoAnexoPublicacaoSelecionada", historicoNotificacoesPublicacaoSelecionada);
	}



	public List<Notificacao> getHistoricoNotificacoesPublicacaoSelecionada() {
		return historicoNotificacoesPublicacaoSelecionada;
	}



	public void setHistoricoNotificacoesPublicacaoSelecionada(
			List<Notificacao> historicoNotificacoesPublicacaoSelecionada) {
		this.historicoNotificacoesPublicacaoSelecionada = historicoNotificacoesPublicacaoSelecionada;
	}



	

}