package br.gov.pi.tce.publicacoes.controller.beans;

import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;

import br.gov.pi.tce.publicacoes.clients.FonteServiceClient;
import br.gov.pi.tce.publicacoes.modelo.Fonte;
import br.gov.pi.tce.publicacoes.modelo.TipoFonte;

@Named
@ViewScoped
public class FontesController extends BeanController {

	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = Logger.getLogger(FontesController.class);

	private Fonte fonte;
	
	@Inject
	private FonteServiceClient fonteServiceClient;
	
	private List<Fonte> fontes;
	
	private List<TipoFonte> tiposFontes = Collections.EMPTY_LIST;

	@PostConstruct
	public void init() {
		LOGGER.info("Iniciando a classe");
		limpar();
		try {
			iniciaFontes();
			iniciaTiposFontes();
		}
		catch (EJBException e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "Serviço indisponível: Fontes.", e.getMessage());
			LOGGER.error("Erro ao consultar fontes.:" + e.getMessage());
			e.printStackTrace();
		}
		catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao consultar fontes.", e.getMessage());
			LOGGER.error("Erro ao iniciar usuários.:" + e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	public List<SelectItem> getTiposFontesParaSelectItems(){
		return getSelectItens(tiposFontes, "nome");
	}
	
	private void iniciaTiposFontes() {
		tiposFontes = fonteServiceClient.consultarTodasTipoFontes();
		
	}

	public void editar(Fonte fonteEditar) {
		fonte = fonteEditar;
	}
	
	private void iniciaFontes() {
		fontes = fonteServiceClient.consultarTodasFontes();
	}

	public void setTiposFontes(List<TipoFonte> tiposFontes) {
		this.tiposFontes = tiposFontes;
	}

	public void limpar() {
		fonte = new Fonte();
		fonte.setAtivo(true);
	}

	public List<Fonte> getFontes() {
		return fontes;
	}
	
	public Fonte getFonte(Long id) {
		try {
			if (id == null) {
				addMessage(FacesMessage.SEVERITY_ERROR, "Fonte não selecionada.", "");
			}
			
			fonte = fonteServiceClient.consultarFontePorCodigo(id);
			
		}
		catch (EJBException e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "Serviço indisponível: Fonte.", e.getMessage());
			LOGGER.error("Erro ao consultar fonte.:" + e.getMessage());
			e.printStackTrace();
		}
		catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao recuperar fonte", "");
			LOGGER.error("Erro ao recuperar fonte:" + e.getMessage());
			e.printStackTrace();
		}
		
		return fonte;
	}
	
	public void salvar() {
		try {
			if (fonte == null) {
				addMessage(FacesMessage.SEVERITY_ERROR, "Fonte não selecionada", "");
			}
			else {
				if (fonte.getId() == null) {
					fonteServiceClient.cadastrarFonte(fonte);
					addMessage(FacesMessage.SEVERITY_INFO, "Fonte cadastrada com sucesso.", "");
				}	
				else {
					fonteServiceClient.alterarFonte(fonte);
					addMessage(FacesMessage.SEVERITY_INFO, "Fonte atualizada com sucesso.", "");

				}
				iniciaFontes();
				iniciaTiposFontes();
			}
			limpar();
		}
		catch (EJBException e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "Serviço indisponível.", e.getMessage());
			LOGGER.error("Erro ao salvar fonte.:" + e.getMessage());
			e.printStackTrace();
		}
		catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR,  "Erro ao salvar fonte.", "label.erro");
			LOGGER.error("Erro ao salvar fonte:" + e.getMessage());
			e.printStackTrace();
			
		}
	}
	
	public void excluir(Fonte fonte) {
		try {
			if (fonte == null) {
				addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao excluir fonte.", "");
			}
			else {
				fonteServiceClient.excluirFontePorCodigo(fonte.getId());
				iniciaFontes();
				iniciaTiposFontes();
				addMessage(FacesMessage.SEVERITY_INFO, "Fonte excluída com sucesso", "");
			}
		}
		catch (EJBException e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "Serviço indisponível: Fonte.", e.getMessage());
			LOGGER.error("Erro ao excluir fonte.:" + e.getMessage());
			e.printStackTrace();
		}
		catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao excluir fonte.", "");
			LOGGER.error("Erro ao excluir fonte:" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void inativar(Fonte fonte) {
		try {
			if (fonte == null) {
				addMessage(FacesMessage.SEVERITY_ERROR, "Fonte não selecionada", "");
			}
			else {
				//fonteServiceClient.inativarFonte(fonte);
				//provisoriamente, mudar quando consertar API Inativar
				fonte.setAtivo(false);
				fonteServiceClient.alterarFonte(fonte);
				iniciaFontes();
				addMessage(FacesMessage.SEVERITY_INFO, "Erro ao inativar fonte.", "");
			}
		}
		catch (EJBException e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "Serviço indisponível: Fonte.", e.getMessage());
			LOGGER.error("Erro ao inativar fonte.:" + e.getMessage());
			e.printStackTrace();
		}
		catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao inativar fonte.", "");
			LOGGER.error("Erro ao inativar fonte:" + e.getMessage());
			e.printStackTrace();
		}
	}

	public Fonte getFonte() {
		return fonte;
	}

	public void setFonte(Fonte fonte) {
		this.fonte = fonte;
	}


	public void setFontes(List<Fonte> fontes) {
		this.fontes = fontes;
	}
 

}