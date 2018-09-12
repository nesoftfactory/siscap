package br.gov.pi.tce.publicacoes.controller.beans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.pi.tce.publicacoes.clients.FonteServiceClient;
import br.gov.pi.tce.publicacoes.modelo.Fonte;
import br.gov.pi.tce.publicacoes.modelo.TipoFonte;

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

	public void editar(TipoFonte tipoFonteEditar) {
		tipoFonte = tipoFonteEditar;
	}
	
	public void excluir(TipoFonte tipoFonteExcluir) {
		tipoFontes.remove(tipoFonteExcluir);
	}
     
	private void iniciaTipoFontes() {
		tipoFontes = fonteServiceClient.getDefaultTipoFontes();
	}

	public List<TipoFonte> getTipoFontes() {
		return tipoFontes;
	}
	
	public TipoFonte getTipoFonte() {
		return tipoFonte;
	}

	public void setTipoFonte(TipoFonte tipoFonte) {
		this.tipoFonte = tipoFonte;
	}

}
