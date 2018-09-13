package br.gov.pi.tce.publicacoes.controller.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.pi.tce.publicacoes.clients.FonteServiceClient;
import br.gov.pi.tce.publicacoes.modelo.Fonte;
import br.gov.pi.tce.publicacoes.modelo.TipoFonte;
import br.gov.pi.tce.publicacoes.modelo.Usuario;

@Named
@ViewScoped
public class TipoFontesController extends BeanController {

	private static final long serialVersionUID = 1L;

	private TipoFonte tipoFonte;
	private ArrayList<TipoFonte> tipoFontes;
	
	@Inject
	private FonteServiceClient fonteServiceClient;
	
	@PostConstruct
	public void init() {
		limpar();
		iniciaTipoFontes();
	}
	
	public void limpar() {
		tipoFonte = new TipoFonte();
	}
	
	private void iniciaTipoFontes() {
		tipoFontes = fonteServiceClient.getDefaultTipoFontes();
	}
	
	public List<TipoFonte> getTipoFontes() {
		return fonteServiceClient.consultarTodasTipoFontes();
	}
	
	public TipoFonte getTipoFonte(UUID id) {
		try {
			if (id == null) {
				registrarMensagem(FacesMessage.SEVERITY_ERROR, "label.erro", null);
			}
			
			tipoFonte = fonteServiceClient.consultarTipoFontePorCodigo(id);
			
		}
		catch (Exception e) {
			registrarMensagem(FacesMessage.SEVERITY_ERROR, "label.erro", null);
		}
		
		return tipoFonte;
	}
	
	public void salvar() {
		try {
			if (tipoFonte == null) {
				registrarMensagem(FacesMessage.SEVERITY_ERROR, "label.erro", null);
			}
			else {
				if (tipoFonte.getId() == null) {
					fonteServiceClient.cadastrarTipoFonte(tipoFonte);
				}	
				else {
					fonteServiceClient.alterarTipoFonte(tipoFonte);
				}
				registrarMensagem(FacesMessage.SEVERITY_INFO, "label.sucesso", null);
			}
			limpar();
		}
		catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR,  null, e.getMessage());
		}
	}
	
	public void excluir(TipoFonte tipoFonte) {
		try {
			if (tipoFonte == null) {
				registrarMensagem(FacesMessage.SEVERITY_ERROR, "label.erro", null);
			}
			else {
				fonteServiceClient.excluirTipoFontePorCodigo(tipoFonte.getId());
				registrarMensagem(FacesMessage.SEVERITY_INFO, "label.sucesso", null);
			}
		}
		catch (Exception e) {
			registrarMensagem(FacesMessage.SEVERITY_ERROR, "label.erro", null);
		}
	}

}
