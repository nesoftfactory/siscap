package br.gov.pi.tce.publicacoes.services;

import java.util.Date;

import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;

import br.gov.pi.tce.publicacoes.controller.beans.BeanController;
import br.gov.pi.tce.publicacoes.controller.beans.PublicacaoController;

/**
 * Classe responsável por executar o agendamento das coletas das publicações nos sites.
 * 
 * 
 * @author Erick Guilherme Cavalcanti
 *
 */
@Stateless
public class ColetorPublicacoesService {

	@Inject
	private PublicacaoController publicacaoController;
	
	// URL das fontes dos diários oficiais
	public final static String URL_FONTE_DIARIO_OFICIAL_PARNAIBA = "http://dom.parnaiba.pi.gov.br";
	public final static String URL_FONTE_DIARIO_OFICIAL_TERESINA = "http://www.dom.teresina.pi.gov.br";
	public final static String URL_FONTE_DIARIO_OFICIAL_DOS_MUNICIPIOS = "http://www.diarioficialdosmunicipios.org";
	public final static String URL_FONTE_DIARIO_OFICIAL_PIAUI = "http://www.diariooficial.pi.gov.br";
	
	public final static int QUANTIDADE_DIAS = 5;

	@Schedule(hour="15", minute = "55")
	public void coletarDiarioOficialParnaiba() {
		Date dataInicial = new Date();
		dataInicial.setDate(dataInicial.getDate() - QUANTIDADE_DIAS);
		Date dataFinal = new Date();
		//PublicacaoController publicacaoController = new PublicacaoController();
		publicacaoController.getDiariosDOM(URL_FONTE_DIARIO_OFICIAL_PARNAIBA, dataInicial, dataFinal);
	}
	
	@Schedule(hour="15", minute = "54")
	public void coletarDiarioOficialTeresina() {
		Date dataInicial = new Date();
		dataInicial.setDate(dataInicial.getDate() - QUANTIDADE_DIAS);
		Date dataFinal = new Date();
		//PublicacaoController publicacaoController = new PublicacaoController();
		publicacaoController.getDiariosDOM(URL_FONTE_DIARIO_OFICIAL_TERESINA, dataInicial, dataFinal);
	}
	
	@Schedule(hour="15", minute = "53")
	public void coletarDiarioOficialMunicipios() {
		Date dataInicial = new Date();
		dataInicial.setDate(dataInicial.getDate() - QUANTIDADE_DIAS);
		Date dataFinal = new Date();
		//PublicacaoController publicacaoController = new PublicacaoController();
		publicacaoController.getDiariosDOM(URL_FONTE_DIARIO_OFICIAL_DOS_MUNICIPIOS, dataInicial, dataFinal);
	}
	
	@Schedule(hour="15", minute = "52")
	public void coletarDiarioOficialPiaui() {
		Date dataInicial = new Date();
		dataInicial.setDate(dataInicial.getDate() - QUANTIDADE_DIAS);
		Date dataFinal = new Date();
		//PublicacaoController publicacaoController = new PublicacaoController();
		publicacaoController.getDiariosEmDiarioOficialPI(dataInicial, dataFinal);
	}
	
}
