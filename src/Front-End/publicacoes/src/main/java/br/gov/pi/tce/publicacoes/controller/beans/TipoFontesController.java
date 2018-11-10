package br.gov.pi.tce.publicacoes.controller.beans;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;

import br.gov.pi.tce.publicacoes.clients.FonteServiceClient;
import br.gov.pi.tce.publicacoes.modelo.TipoFonte;

@Named
@ViewScoped
public class TipoFontesController extends BeanController {

	private static final long serialVersionUID = 1L;

	private TipoFonte tipoFonte;
	private List<TipoFonte> tipoFontes;
	
	private static final Logger LOGGER = Logger.getLogger(TipoFontesController.class);
	
	@Inject
	private FonteServiceClient fonteServiceClient;
	
	@PostConstruct
	public void init() {
		limpar();
		iniciaTipoFontes();
	}
	
	public void editar(TipoFonte tipoFonteEditar) {
		tipoFonte = tipoFonteEditar;
	}
	
	
	private void iniciaTipoFontes() {
		try {
			tipoFontes = fonteServiceClient.consultarTodasTipoFontes();
		}
		catch (EJBException e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "Serviço indisponível: Tipos de fontes.", e.getMessage());
			LOGGER.error("Erro ao consultar tipos de fontes.:" + e.getMessage());
			e.printStackTrace();
		}
		catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao iniciar tipos de fontes.", e.getMessage());
			LOGGER.error("Erro ao consultar tipos de fontes:" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void limpar() {
		tipoFonte = new TipoFonte();
		tipoFonte.setAtivo(true);
	}
	
	
	public TipoFonte getTipoFonte() {
		return tipoFonte;
	}

	public void setTipoFonte(TipoFonte tipoFonte) {
		this.tipoFonte = tipoFonte;
	}

	public FonteServiceClient getFonteServiceClient() {
		return fonteServiceClient;
	}

	public void setFonteServiceClient(FonteServiceClient fonteServiceClient) {
		this.fonteServiceClient = fonteServiceClient;
	}

	public void setTipoFontes(List<TipoFonte> tipoFontes) {
		this.tipoFontes = tipoFontes;
	}

	public List<TipoFonte> getTipoFontes() {
		return tipoFontes;
	}
	
	public TipoFonte getTipoFonte(Long id) {
		try {
			if (id == null) {
				addMessage(FacesMessage.SEVERITY_ERROR, "Tipo de Fonte não selcionada", "");
			}
			
			tipoFonte = fonteServiceClient.consultarTipoFontePorCodigo(id);
			
		}
		catch (EJBException e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "Serviço indisponível: Tipo de fonte", e.getMessage());
			LOGGER.error("Erro ao buscar tipo de fonte específica.:" + e.getMessage());
			e.printStackTrace();
		}
		catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao buscar tipo de fonte específica.", "");
			LOGGER.error("Erro ao buscar tipo de fonte específica.:" + e.getMessage());
			e.printStackTrace();
		}
		
		return tipoFonte;
	}
	
	public void salvar() {
		try {
			if (tipoFonte == null) {
				addMessage(FacesMessage.SEVERITY_ERROR, "Tipo de fonte não selecionada.", "");
			}
			else {
				if (tipoFonte.getId() == null) {
					fonteServiceClient.cadastrarTipoFonte(tipoFonte);
					addMessage(FacesMessage.SEVERITY_INFO, "Tipo de fonte cadastrada com sucesso", "");
				}	
				else {
					fonteServiceClient.alterarTipoFonte(tipoFonte);
					addMessage(FacesMessage.SEVERITY_INFO, "Tipo de fonte atualizada com sucesso", "");
				}
				iniciaTipoFontes();
			}
			limpar();
		}
		catch (EJBException e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "Serviço indisponível: Tipo de fonte.", e.getMessage());
			LOGGER.error("Erro ao salvar tipo de fonte.:" + e.getMessage());
			e.printStackTrace();
		}
		catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR,  "Erro ao salvar tipo de fonte." + e.getMessage(), e.getMessage());
			LOGGER.error("Erro ao salvar tipo de fonte.:" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void excluir(TipoFonte tipoFonte) {
		try {
			if (tipoFonte == null) {
				addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao excluir tipo de fonte", "");
			}
			else {
				fonteServiceClient.excluirTipoFontePorCodigo(tipoFonte.getId());
				iniciaTipoFontes();
				addMessage(FacesMessage.SEVERITY_INFO, "Tipo de fonte excluída com sucesso", "");
			}
		}
		catch (EJBException e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "Serviço indisponível: Tipo de fonte.", e.getMessage());
			LOGGER.error("Erro ao excluir tipo de fonte.:" + e.getMessage());
			e.printStackTrace();
		}
		catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao excluir tipo de fonte", "");
			LOGGER.error("Erro ao excluir tipo de fonte.:" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void inativar(TipoFonte tipoFonte) {
		try {
			if (tipoFonte == null) {
				addMessage(FacesMessage.SEVERITY_ERROR, "Tipo de fonte não selecionada", "");
			}
			else {
				//fonteServiceClient.inativarTipoFonte(tipoFonte);
				//provisoriamente, mudar quando consertar API Inativar
				tipoFonte.setAtivo(false);
				fonteServiceClient.alterarTipoFonte(tipoFonte);
				iniciaTipoFontes();
				addMessage(FacesMessage.SEVERITY_INFO, "label.sucesso", "");
			}
		}
		catch (EJBException e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "Serviço indisponível: Tipo de fonte.", e.getMessage());
			LOGGER.error("Erro ao inativar tipo de fonte.:" + e.getMessage());
			e.printStackTrace();
		}
		catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao inativar tipo de fonte." + e.getMessage(), "");
			LOGGER.error("Erro ao inativar tipo de fonte.:" + e.getMessage());
			e.printStackTrace();
		}
	}

}
