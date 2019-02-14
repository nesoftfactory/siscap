package br.gov.pi.tce.publicacoes.controller.beans;

import java.text.ParseException;
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

import br.gov.pi.tce.publicacoes.clients.FeriadoServiceClient;
import br.gov.pi.tce.publicacoes.clients.FonteServiceClient;
import br.gov.pi.tce.publicacoes.modelo.Feriado;
import br.gov.pi.tce.publicacoes.modelo.Fonte;


@Named
@ViewScoped
public class FeriadosController extends BeanController {

	private static final long serialVersionUID = 1L;

	private Feriado feriado;
	
	@Inject
	private FeriadoServiceClient feriadoServiceClient;
	
	@Inject
	private FonteServiceClient fonteServiceClient;
	
	private List<Feriado> feriados;
	
	private List<Fonte> fontes = Collections.EMPTY_LIST;
	
	private static final Logger LOGGER = Logger.getLogger(FeriadosController.class);
	
	@PostConstruct
	public void init() {
		limpar();
		iniciaFeriados();
		iniciaFontes();
	}
	
	public List<SelectItem> getFontesParaSelectItems(){
		return getSelectItens(fontes, "nome");
	}
	
	private void iniciaFontes() {
		try {
			fontes = fonteServiceClient.consultarTodasFontes();
		}
		catch (EJBException e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "Serviço indisponível: Fontes", e.getMessage());
			LOGGER.error("Erro ao iniciar fontes.:" + e.getMessage());
			e.printStackTrace();
		}
		catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao iniciar fontes.", e.getMessage());
			LOGGER.error("Erro ao iniciar fontes.:" + e.getMessage());
			e.printStackTrace();
		}
		
	}

	public void editar(Feriado feriadoEditar) {
		feriado = feriadoEditar;
	}
	
	private void iniciaFeriados() {
		try {
			feriados = feriadoServiceClient.consultarTodosFeriados();
		}
		catch (EJBException e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "Serviço indisponível: Feriados.", e.getMessage());
			LOGGER.error("Erro ao iniciar feriados.:" + e.getMessage());
			e.printStackTrace();
		}
		catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao iniciar feriados.", e.getMessage());
			LOGGER.error("Erro ao iniciar feriados.:" + e.getMessage());
			e.printStackTrace();
		}
	}

	public void setFontes(List<Fonte> fontes) {
		this.fontes = fontes;
	}


	public void limpar() {
		feriado = new Feriado();
		feriado.setFontes(Collections.EMPTY_LIST);
		feriado.setAtivo(true);
	}

	public List<Feriado> getFeriados() {
		return feriados;
	}
	
	public Feriado getFeriado(Long id) {
		try {
			if (id == null) {
				addMessage(FacesMessage.SEVERITY_ERROR, "Feriado não selecionado.", "");
			}
			
			feriado = feriadoServiceClient.consultarFeriadoPorCodigo(id);
			
		}
		catch (EJBException e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "Serviço indisponível: Feriados.", e.getMessage());
			LOGGER.error("Erro ao recuperar feriado.:" + e.getMessage());
			e.printStackTrace();
		}
		catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao recuperar feriado.", "");
			LOGGER.error("Erro ao recuperar feriado.:" + e.getMessage());
			e.printStackTrace();
		}
		
		return feriado;
	}
	
	public void salvar() {
		try {
			
			String dt = validaData(feriado.getData());
			if(dt == null) {
				addMessage(FacesMessage.SEVERITY_ERROR, "Data inválida.");
				return;
			}
			feriado.setData(dt);
			if (feriado == null) {
				addMessage(FacesMessage.SEVERITY_ERROR, "Feriado não selecionado.", "");
			}
			else {
				if ((feriado.getTodasFontes()) && (feriado.getFontes().size() > 0)) {
					feriado.setFontes(Collections.EMPTY_LIST);
				}
				
				if ((feriado.getFontes().size() == 0) && (feriado.getTodasFontes() == false)) { 
					feriado.setTodasFontes(true);
				}
				
				if (feriado.getId() == null) {
					feriado.ajustaFormatoDataParaAPI();
					feriadoServiceClient.cadastrarFeriado(feriado);
				}	
				else {
					feriado.ajustaFormatoDataParaAPI();
					feriadoServiceClient.alterarFeriado(feriado);
				}
				iniciaFeriados();
				iniciaFontes();
				addMessage(FacesMessage.SEVERITY_INFO, "Feriado salvo com sucesso.", "");
			}
			limpar();
		}
		catch (EJBException e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "Serviço indisponível.", e.getMessage());
			LOGGER.error("Erro ao salvar feriado.:" + e.getMessage());
			e.printStackTrace();
		}
		catch (ParseException e) {
			addMessage(FacesMessage.SEVERITY_ERROR,  "Erro ao salvar feriado. Data Inválida.", e.getMessage());
			LOGGER.error("Erro ao salvar feriado. Data Inválida:" + e.getMessage());
			e.printStackTrace();
		}
		catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR,  "Erro ao salvar feriado." + e.getMessage(), e.getMessage());
			feriado.ajustaFormatoDataParaExibir();

			LOGGER.error("Erro ao salvar feriado.:" + e.getMessage());
			e.printStackTrace();
		}
		
	}

	
	public void excluir(Feriado feriado) {
		try {
			if (feriado == null) {
				addMessage(FacesMessage.SEVERITY_ERROR, "Feriado não selecionado", "");
			}
			else {
				feriadoServiceClient.excluirFeriadoPorCodigo(feriado.getId());
				iniciaFeriados();
				iniciaFontes();
				addMessage(FacesMessage.SEVERITY_INFO, "Feriado excluído com sucesso.", "");
			}
		}
		catch (EJBException e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "Serviço indisponível.", e.getMessage());
			LOGGER.error("Erro ao excluir feriado.:" + e.getMessage());
			e.printStackTrace();
		}
		catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao excluir feriado.", "");
			LOGGER.error("Erro ao excluir feriado.:" + e.getMessage());
			e.printStackTrace();
		}
	}

	public String getTodasAsFontes(Feriado feriado) {
		String fontesInformation = "";
		if (feriado.getFontes().size() == 0) {
			fontesInformation = "Todas as Fontes";
		}
		return fontesInformation;
	}
	
	public Feriado getFeriado() {
		return feriado;
	}

	public void setFeriado(Feriado feriado) {
		this.feriado = feriado;
	}

	public void setFeriados(List<Feriado> feriados) {
		this.feriados = feriados;
	}
	
	public void desabilitarFontes() {
		if (feriado.getTodasFontes()) {
			feriado.setFontes(Collections.EMPTY_LIST);
		}
	}
	
	public void habilitaTodasFontes() {
		if (feriado.getTodasFontes()) {
			feriado.setFontes(Collections.EMPTY_LIST);
		} else {
			if (feriado.getFontes().size() == fontes.size()) {
				feriado.setTodasFontes(true);
				feriado.setFontes(Collections.EMPTY_LIST);
			}
		}
		
	}


}