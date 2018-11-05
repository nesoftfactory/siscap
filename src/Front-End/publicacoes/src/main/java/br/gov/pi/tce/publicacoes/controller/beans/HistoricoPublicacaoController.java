package br.gov.pi.tce.publicacoes.controller.beans;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;

import br.gov.pi.tce.publicacoes.clients.PublicacaoHistoricoServiceClient;
import br.gov.pi.tce.publicacoes.modelo.PublicacaoHistorico;

@Named
@ViewScoped
public class HistoricoPublicacaoController extends BeanController {

	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = Logger.getLogger(HistoricoPublicacaoController.class);
	
	
	@Inject
	private PublicacaoHistoricoServiceClient publicacaoHistoricoServiceClient;
	
	private List<PublicacaoHistorico> historicoPublicacaoSelecionada;
	
	
	@PostConstruct
	public void init() {
		try {
			popupHistorico();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		}
	}
	
	

	public void popupHistorico() throws IOException {
		String  idPublicacao = (String)FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
		historicoPublicacaoSelecionada = publicacaoHistoricoServiceClient.consultarPublicacaoHistoricoPeloIdPublicacao(Long.valueOf(idPublicacao));
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("historicoPublicacaoSelecionada", historicoPublicacaoSelecionada);
	}



	public List<PublicacaoHistorico> getHistoricoPublicacaoSelecionada() {
		return historicoPublicacaoSelecionada;
	}



	public void setHistoricoPublicacaoSelecionada(List<PublicacaoHistorico> historicoPublicacaoSelecionada) {
		this.historicoPublicacaoSelecionada = historicoPublicacaoSelecionada;
	}
	
	
	
}