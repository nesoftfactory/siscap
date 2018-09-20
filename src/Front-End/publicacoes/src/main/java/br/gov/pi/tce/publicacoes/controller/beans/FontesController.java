package br.gov.pi.tce.publicacoes.controller.beans;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.pi.tce.publicacoes.clients.FonteServiceClient;
import br.gov.pi.tce.publicacoes.modelo.Fonte;
import br.gov.pi.tce.publicacoes.modelo.TipoFonte;

@Named
@ViewScoped
public class FontesController extends BeanController {

	private static final long serialVersionUID = 1L;

	private Fonte fonte;
	
	
	@Inject
	private FonteServiceClient fonteServiceClient;
	
	
	private List<Fonte> fontes;
	
	
	
	public List<TipoFonte> getTiposFontes(){
		return fonteServiceClient.consultarTodasTipoFontes();
	}
	
	@PostConstruct
	public void init() {
		limpar();
		iniciaFontes();
	}
	
	public void editar(Fonte fonteEditar) {
		fonte = fonteEditar;
	}
	
	private void iniciaFontes() {
		try {
			fontes = fonteServiceClient.consultarTodasFontes();
		}
		catch (Exception e) {
			registrarMensagem(FacesMessage.SEVERITY_ERROR, "label.erro", e.getMessage());
		}
	}
	
	public void limpar() {
		fonte = new Fonte();
	}

	public List<Fonte> getFontes() {
		return fontes;
	}
	
	public Fonte getFonte(Long id) {
		try {
			if (id == null) {
				registrarMensagem(FacesMessage.SEVERITY_ERROR, "label.erro", null);
			}
			
			fonte = fonteServiceClient.consultarFontePorCodigo(id);
			
		}
		catch (Exception e) {
			registrarMensagem(FacesMessage.SEVERITY_ERROR, "label.erro", null);
		}
		
		return fonte;
	}
	
	public void salvar() {
		try {
			if (fonte == null) {
				registrarMensagem(FacesMessage.SEVERITY_ERROR, "label.erro", null);
			}
			else {
				if (fonte.getId() == null) {
					fonteServiceClient.cadastrarFonte(fonte);
				}	
				else {
					fonteServiceClient.alterarFonte(fonte);
				}
				iniciaFontes();
				registrarMensagem(FacesMessage.SEVERITY_INFO, "label.sucesso", null);
			}
			limpar();
		}
		catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR,  null, e.getMessage());
		}
	}
	
	public void excluir(Fonte fonte) {
		try {
			if (fonte == null) {
				registrarMensagem(FacesMessage.SEVERITY_ERROR, "label.erro", null);
			}
			else {
				fonteServiceClient.excluirFontePorCodigo(fonte.getId());
				iniciaFontes();
				registrarMensagem(FacesMessage.SEVERITY_INFO, "label.sucesso", null);
			}
		}
		catch (Exception e) {
			registrarMensagem(FacesMessage.SEVERITY_ERROR, "label.erro", null);
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