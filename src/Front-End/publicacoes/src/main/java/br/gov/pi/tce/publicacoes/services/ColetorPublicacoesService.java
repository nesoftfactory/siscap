package br.gov.pi.tce.publicacoes.services;

import java.util.Date;

import javax.ejb.Schedule;
import javax.ejb.Stateless;

import br.gov.pi.tce.publicacoes.controller.beans.utils.ColetorPublicacaoUtil;

/**
 * Classe responsável por executar o agendamento das coletas das publicações nos sites.
 * 
 * @author Erick Guilherme Cavalcanti
 *
 */
@Stateless
public class ColetorPublicacoesService {
	
	// URL das fontes dos diários oficiais
	public final static String URL_FONTE_DIARIO_OFICIAL_PARNAIBA = "http://dom.parnaiba.pi.gov.br";
	public final static String URL_FONTE_DIARIO_OFICIAL_TERESINA = "http://www.dom.teresina.pi.gov.br";
	public final static String URL_FONTE_DIARIO_OFICIAL_DOS_MUNICIPIOS = "http://www.diarioficialdosmunicipios.org";
	public final static String URL_FONTE_DIARIO_OFICIAL_PIAUI = "http://www.diariooficial.pi.gov.br";
	
	public final static int QUANTIDADE_DIAS = 10;

	@Schedule(hour="0", minute = "16")
	public void coletarDiarioOficialParnaiba() {
		Date dataInicial = new Date();
		dataInicial.setDate(dataInicial.getDate() - QUANTIDADE_DIAS);
		Date dataFinal = new Date();
		ColetorPublicacaoUtil.getDiariosDOM(URL_FONTE_DIARIO_OFICIAL_PARNAIBA, dataInicial, dataFinal);
	}
	
	@Schedule(hour="8")
	public void coletarDiarioOficialTeresina() {
		Date dataInicial = new Date();
		dataInicial.setDate(dataInicial.getDate() - QUANTIDADE_DIAS);
		Date dataFinal = new Date();
		ColetorPublicacaoUtil.getDiariosDOM(URL_FONTE_DIARIO_OFICIAL_TERESINA, dataInicial, dataFinal);
	}
	
	@Schedule(hour="9")
	public void coletarDiarioOficialMunicipios() {
		Date dataInicial = new Date();
		dataInicial.setDate(dataInicial.getDate() - QUANTIDADE_DIAS);
		Date dataFinal = new Date();
		ColetorPublicacaoUtil.getDiariosDOM(URL_FONTE_DIARIO_OFICIAL_DOS_MUNICIPIOS, dataInicial, dataFinal);
	}
	
	@Schedule(hour="10")
	public void coletarDiarioOficialPiaui() {
		Date dataInicial = new Date();
		dataInicial.setDate(dataInicial.getDate() - QUANTIDADE_DIAS);
		Date dataFinal = new Date();
		ColetorPublicacaoUtil.getDiariosEmDiarioOficialPI(dataInicial, dataFinal);
	}
	
}
