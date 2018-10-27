package br.gov.pi.tce.publicacoes.controller.beans;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.pi.tce.publicacoes.clients.FonteServiceClient;
import br.gov.pi.tce.publicacoes.modelo.TipoFonte;
import br.gov.pi.tce.publicacoes.modelo.Usuario;

@Named
@ViewScoped
public class TipoFontesController extends BeanController {

	private static final long serialVersionUID = 1L;

	private TipoFonte tipoFonte;
	private List<TipoFonte> tipoFontes;
	
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
		catch (Exception e) {
			registrarMensagem(FacesMessage.SEVERITY_ERROR, "label.erro", e.getMessage());
		}
	}
	
	public void limpar() {
		tipoFonte = new TipoFonte();
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
				registrarMensagem(FacesMessage.SEVERITY_ERROR, "label.erro", "");
			}
			
			tipoFonte = fonteServiceClient.consultarTipoFontePorCodigo(id);
			
		}
		catch (Exception e) {
			registrarMensagem(FacesMessage.SEVERITY_ERROR, "label.erro", "");
		}
		
		return tipoFonte;
	}
	
	public void salvar() {
		try {
			if (tipoFonte == null) {
				registrarMensagem(FacesMessage.SEVERITY_ERROR, "label.erro", "");
			}
			else {
				if (tipoFonte.getId() == null) {
					fonteServiceClient.cadastrarTipoFonte(tipoFonte);
				}	
				else {
					fonteServiceClient.alterarTipoFonte(tipoFonte);
				}
				iniciaTipoFontes();
				registrarMensagem(FacesMessage.SEVERITY_INFO, "label.sucesso", "");
			}
			limpar();
		}
		catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR,  "", e.getMessage());
		}
	}
	
	public void excluir(TipoFonte tipoFonte) {
		try {
			if (tipoFonte == null) {
				registrarMensagem(FacesMessage.SEVERITY_ERROR, "label.erro", "");
			}
			else {
				fonteServiceClient.excluirTipoFontePorCodigo(tipoFonte.getId());
				iniciaTipoFontes();
				registrarMensagem(FacesMessage.SEVERITY_INFO, "label.sucesso", "");
			}
		}
		catch (Exception e) {
			registrarMensagem(FacesMessage.SEVERITY_ERROR, "label.erro", "");
		}
	}
	
	public void inativar(TipoFonte tipoFonte) {
		try {
			if (tipoFonte == null) {
				registrarMensagem(FacesMessage.SEVERITY_ERROR, "label.erro", "");
			}
			else {
				//fonteServiceClient.inativarTipoFonte(tipoFonte);
				//provisoriamente, mudar quando consertar API Inativar
				tipoFonte.setAtivo(false);
				fonteServiceClient.alterarTipoFonte(tipoFonte);
				iniciaTipoFontes();
				registrarMensagem(FacesMessage.SEVERITY_INFO, "label.sucesso", "");
			}
		}
		catch (Exception e) {
			registrarMensagem(FacesMessage.SEVERITY_ERROR, "label.erro", "");
		}
	}

}
