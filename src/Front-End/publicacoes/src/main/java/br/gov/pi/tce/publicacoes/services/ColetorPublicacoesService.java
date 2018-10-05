package br.gov.pi.tce.publicacoes.services;

import java.util.Date;

import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;

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
	public final static Long URL_FONTE_DIARIO_OFICIAL_DOS_MUNICIPIOS = Long.valueOf(2);//"http://www.diarioficialdosmunicipios.org";
	public final static Long URL_FONTE_DIARIO_OFICIAL_PIAUI = Long.valueOf(3);//"http://www.diariooficial.pi.gov.br";
	public final static Long URL_FONTE_DIARIO_OFICIAL_TERESINA = Long.valueOf(4);//"http://www.dom.teresina.pi.gov.br";
	public final static Long URL_FONTE_DIARIO_OFICIAL_PARNAIBA = Long.valueOf(5); //"http://dom.parnaiba.pi.gov.br";
	
	public final static int QUANTIDADE_DIAS = 5;

	@Schedule(hour="20", minute = "02")
	public void coletarDiarioOficialParnaiba() {
		Date dataInicial = new Date();
		dataInicial.setDate(dataInicial.getDate() - QUANTIDADE_DIAS);
		Date dataFinal = new Date();
		publicacaoController.getDiariosDOM(URL_FONTE_DIARIO_OFICIAL_PARNAIBA, dataInicial, dataFinal);
	}
	
	@Schedule(hour="20", minute = "04")
	public void coletarDiarioOficialTeresina() {
		Date dataInicial = new Date();
		dataInicial.setDate(dataInicial.getDate() - QUANTIDADE_DIAS);
		Date dataFinal = new Date();
		publicacaoController.getDiariosDOM(URL_FONTE_DIARIO_OFICIAL_TERESINA, dataInicial, dataFinal);
	}
	
	@Schedule(hour="18", minute = "40")
	public void coletarDiarioOficialMunicipios() {
		Date dataInicial = new Date();
		dataInicial.setDate(dataInicial.getDate() - QUANTIDADE_DIAS);
		Date dataFinal = new Date();
		publicacaoController.getDiariosDOM(URL_FONTE_DIARIO_OFICIAL_DOS_MUNICIPIOS, dataInicial, dataFinal);
	}
	
	@Schedule(hour="18", minute = "42")
	public void coletarDiarioOficialPiaui() {
		Date dataInicial = new Date();
		dataInicial.setDate(dataInicial.getDate() - QUANTIDADE_DIAS);
		Date dataFinal = new Date();
		publicacaoController.getDiariosEmDiarioOficialPI(dataInicial, dataFinal);
	}
	
}
