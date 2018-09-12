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
public class FontesController extends BeanController {

	private static final long serialVersionUID = 1L;

	private Fonte fonte;
	private List<Fonte> fontes;
	
	@Inject
	private FonteServiceClient fonteServiceClient;
	
	@PostConstruct
	public void init() {
		limpar();
		iniciaFontes();
	}
	
	public void limpar() {
		fonte = new Fonte();
	}

	public void editar(Fonte fonteEditar) {
		fonte = fonteEditar;
	}
	
	public void excluir(Fonte fonteExcluir) {
		fontes.remove(fonteExcluir);
	}
     
	private void iniciaFontes() {
		
		TipoFonte tipoFonte1 = fonteServiceClient.consultarTipoFontePorNome(TipoFonte.TIPO_FONTE_PADRAO_1);
		TipoFonte tipoFonte2 = fonteServiceClient.consultarTipoFontePorNome(TipoFonte.TIPO_FONTE_PADRAO_2);
		
		fontes = new ArrayList<>();
		fontes.add(new Fonte("Diário Oficial do Estado", "http://www.diariooficial.pi.gov.br/", tipoFonte1));
		fontes.add(new Fonte("Diário Oficial dos Municípios", "http://www.diarioficialdosmunicipios.org/", tipoFonte2));
		fontes.add(new Fonte("Diário Oficial de Teresina", "http://www.dom.teresina.pi.gov.br/", tipoFonte2));
		fontes.add(new Fonte("Diário Oficial de Parnaíba", "http://dom.parnaiba.pi.gov.br/", tipoFonte2));
	}

	public List<Fonte> getFontes() {
		return fontes;
	}
	
	public Fonte getFonte() {
		return fonte;
	}

	public void setFonte(Fonte fonte) {
		this.fonte = fonte;
	}

}