package br.gov.pi.tce.siscap.timer.scheduler;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import br.gov.pi.tce.siscap.timer.client.FonteServiceClient;
import br.gov.pi.tce.siscap.timer.config.property.SiscapTimerProperty;
import br.gov.pi.tce.siscap.timer.config.property.SiscapTimerProperty.Fontes.FonteAuto;
import br.gov.pi.tce.siscap.timer.model.Fonte;
import br.gov.pi.tce.siscap.timer.service.ColetorService;
import br.gov.pi.tce.siscap.timer.util.DateUtil;

@Service
public class ColetorPublicacoes {

	private static final Logger logger = LoggerFactory.getLogger(ColetorPublicacoes.class);

	@Autowired
	private SiscapTimerProperty property;

	@Autowired
	private ColetorService coletorService;

	@Autowired
	private FonteServiceClient fonteServiceClient;

	@EventListener()
	private void teste(ApplicationEvent event) {
		// Executar ao iniciar
		//this.coletarDiarioOficialPiaui();
	}

	@Scheduled(cron = "0 20 10,19 * * *")
	public void coletarDiarioOficialParnaiba() {
		coletar(property.getFontes().getParnaiba());
	}

	@Scheduled(cron = "0 30 10,19 * * *")
	public void coletarDiarioOficialTeresina() {
		coletar(property.getFontes().getTeresina());
	}

	@Scheduled(cron = "0 40 10,19 * * *")
	public void coletarDiarioOficialPiaui() {
		coletar(property.getFontes().getPiaui());
	}

	@Scheduled(cron = "0 50 10,19 * * *")
	public void coletarDiarioOficialMunicipios() {
		coletar(property.getFontes().getMunicipios());
	}
	
	private void coletar(FonteAuto fonteAutomatica) {
		//Date data = new Date(new Date().getTime() + (1000 * 60 * 60 * 24));
		Date data = new Date();

		Date dataInicial = DateUtil.getData00Horas00Minutos00SeguntosMenosQuatidadeDias(data,
				property.getQuantidadeDiasColeta());
		Date dataFinal = DateUtil.getData23Horas59Minutos59Seguntos(data);

		Fonte fonte = fonteServiceClient.consultarFontePorIdFonte(new Long(fonteAutomatica.getId()));

		logger.info("Iniciando a Coleta do Diario Oficial de " + fonteAutomatica.getEnumFonte() + " - "
				+ DateUtil.convertDateToString(dataInicial) + " a " + DateUtil.convertDateToString(dataFinal));

		fonteAutomatica.getEnumFonte().getColetor().coletar(coletorService, fonte, dataInicial, dataFinal);

		logger.info("Finalizada a Coleta do Diario Oficial de " + fonteAutomatica.getEnumFonte());
	}

}