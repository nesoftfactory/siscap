package br.gov.pi.tce.publicacoes.controller.beans;

import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

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
		catch (Exception e) {
			registrarMensagem(FacesMessage.SEVERITY_ERROR, "label.erro", e.getMessage());
		}
		
	}

	public void editar(Feriado feriadoEditar) {
		feriado = feriadoEditar;
	}
	
	private void iniciaFeriados() {
		try {
			feriados = feriadoServiceClient.consultarTodosFeriados();
		}
		catch (Exception e) {
			registrarMensagem(FacesMessage.SEVERITY_ERROR, "label.erro", e.getMessage());
		}
	}

	public void setFontes(List<Fonte> fontes) {
		this.fontes = fontes;
	}


	public void limpar() {
		feriado = new Feriado();
		feriado.setFontes(Collections.EMPTY_LIST);
	}

	public List<Feriado> getFeriados() {
		return feriados;
	}
	
	public Feriado getFeriado(Long id) {
		try {
			if (id == null) {
				registrarMensagem(FacesMessage.SEVERITY_ERROR, "label.erro", "");
			}
			
			feriado = feriadoServiceClient.consultarFeriadoPorCodigo(id);
			
		}
		catch (Exception e) {
			registrarMensagem(FacesMessage.SEVERITY_ERROR, "label.erro", "");
		}
		
		return feriado;
	}
	
	public void salvar() {
		try {
			if (feriado == null) {
				registrarMensagem(FacesMessage.SEVERITY_ERROR, "label.erro", "");
			}
			else {
				if ((feriado.getTodasFontes()) && (feriado.getFontes().size() > 0)) {
					feriado.setFontes(Collections.EMPTY_LIST);
				}
				
				if ((feriado.getFontes().size() == 0) && (feriado.getTodasFontes() == false)) { 
					feriado.setTodasFontes(true);
				}
				
				if (feriado.getId() == null) {
					feriadoServiceClient.cadastrarFeriado(feriado);
				}	
				else {
					feriadoServiceClient.alterarFeriado(feriado);
				}
				iniciaFeriados();
				iniciaFontes();
				registrarMensagem(FacesMessage.SEVERITY_INFO, "label.sucesso", "");
			}
			limpar();
		}
		catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR,  "", e.getMessage());
		}
	}
	
	public void excluir(Feriado feriado) {
		try {
			if (feriado == null) {
				registrarMensagem(FacesMessage.SEVERITY_ERROR, "label.erro", "");
			}
			else {
				feriadoServiceClient.excluirFeriadoPorCodigo(feriado.getId());
				iniciaFeriados();
				iniciaFontes();
				registrarMensagem(FacesMessage.SEVERITY_INFO, "label.sucesso", "");
			}
		}
		catch (Exception e) {
			registrarMensagem(FacesMessage.SEVERITY_ERROR, "label.erro", "");
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