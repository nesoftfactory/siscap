package br.gov.pi.tce.publicacoes.controller.beans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import modelo.Fonte;

@Named
@ViewScoped
public class FontesController extends BeanController {

	private static final long serialVersionUID = 1L;

	private Fonte fonte;
	private List<Fonte> fontes;
	
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
		fontes = new ArrayList<>();
		fontes.add(new Fonte("Diário Oficial do Estado", "Estadual", "http://www.diariooficial.pi.gov.br/"));
		fontes.add(new Fonte("Diário Oficial dos Municípios", "Municipal", "http://www.diarioficialdosmunicipios.org/"));
		fontes.add(new Fonte("Diário Oficial de Teresina", "Municipal", "http://www.dom.teresina.pi.gov.br/"));
		fontes.add(new Fonte("Diário Oficial de Parnaíba", "Municipal", "http://dom.parnaiba.pi.gov.br/"));
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