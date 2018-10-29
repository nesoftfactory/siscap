package br.gov.pi.tce.publicacoes.services;

import java.util.Calendar;
import java.util.Date;

import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import br.gov.pi.tce.publicacoes.controller.beans.PublicacaoController;
import br.gov.pi.tce.publicacoes.util.Propriedades;

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
	
	private static final Logger LOGGER = Logger.getLogger(ColetorPublicacoesService.class);
	
	@Schedule(hour="14", minute = "44")
	public void coletarDiarioOficialParnaiba() {
		LOGGER.info("Iniciando a Coleta do Diario Oficial da Parnaiba");
		Propriedades propriedades = Propriedades.getInstance();
		Date data = new Date();
		Date dataInicial = getData00Horas00Minutos00SeguntosMenosQuatidadeDias(data, propriedades.getValorInt("QUANTIDADE_DIAS_PARA_COLETA"));
		Date dataFinal = getData23Horas59Minutos59Seguntos(data);
		publicacaoController.getDiariosDOM(propriedades.getValorLong("ID_FONTE_DIARIO_OFICIAL_PARNAIBA"), dataInicial, dataFinal);
		LOGGER.info("Finalizando a Coleta do Diario Oficial da Parnaiba");
	}
	
	@Schedule(hour="11", minute = "30")
	public void coletarDiarioOficialTeresina() {
		LOGGER.info("Iniciando a Coleta do Diario Oficial de Teresina");
		Propriedades propriedades = Propriedades.getInstance();
		Date data = new Date();
		Date dataInicial = getData00Horas00Minutos00SeguntosMenosQuatidadeDias(data, propriedades.getValorInt("QUANTIDADE_DIAS_PARA_COLETA"));
		Date dataFinal = getData23Horas59Minutos59Seguntos(data);
		publicacaoController.getDiariosDOM(propriedades.getValorLong("ID_FONTE_DIARIO_OFICIAL_TERESINA"), dataInicial, dataFinal);
		LOGGER.info("Finalizando a Coleta do Diario Oficial de Teresina");
	}
	
	@Schedule(hour="11", minute = "50")
	public void coletarDiarioOficialMunicipios() {
		LOGGER.info("Iniciando a Coleta do Diario Oficial dos Municipios");
		Propriedades propriedades = Propriedades.getInstance();
		Date data = new Date();
		Date dataInicial = getData00Horas00Minutos00SeguntosMenosQuatidadeDias(data, propriedades.getValorInt("QUANTIDADE_DIAS_PARA_COLETA"));
		Date dataFinal = getData23Horas59Minutos59Seguntos(data);
		publicacaoController.getDiariosDOM(propriedades.getValorLong("ID_FONTE_DIARIO_OFICIAL_DOS_MUNICIPIOS"), dataInicial, dataFinal);
		LOGGER.info("Finalizando a Coleta do Diario Oficial dos Municipios");
	}
	
	@Schedule(hour="11", minute = "40")
	public void coletarDiarioOficialPiaui() {
		LOGGER.info("Iniciando a Coleta do Diario Oficial do Estado do Piaui");
		Propriedades propriedades = Propriedades.getInstance();
		Date data = new Date();
		Date dataInicial = getData00Horas00Minutos00SeguntosMenosQuatidadeDias(data, propriedades.getValorInt("QUANTIDADE_DIAS_PARA_COLETA"));
		Date dataFinal = getData23Horas59Minutos59Seguntos(data);
		publicacaoController.getDiariosEmDiarioOficialPI(dataInicial, dataFinal);
		LOGGER.info("Finalizando a Coleta do Diario Oficial do Estado do Piaui");
	}
	
	private Date getData00Horas00Minutos00SeguntosMenosQuatidadeDias(Date data, int quatidadeDias){
		if (quatidadeDias <= 0) {
			quatidadeDias = 0;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		cal.add(Calendar.DAY_OF_MONTH, (quatidadeDias * -1));
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
	private Date getData23Horas59Minutos59Seguntos(Date data){
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
	
}
