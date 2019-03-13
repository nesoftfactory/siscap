package br.gov.pi.tce.siscap.timer.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import br.gov.pi.tce.siscap.timer.config.property.SiscapTimerProperty;
import br.gov.pi.tce.siscap.timer.config.property.SiscapTimerProperty.Fontes.FonteAuto;
import br.gov.pi.tce.siscap.timer.service.OCRService;

@Service
public class OCRPublicacoes {
	private static final Logger logger = LoggerFactory.getLogger(OCRPublicacoes.class);
	
	@Autowired
	private SiscapTimerProperty property;

	@Autowired
	private OCRService ocrService;
	
	@EventListener()
	private void teste(ApplicationEvent event) {
		// Executar ao iniciar
		//this.realizarOCRDiarioOficialMunicipios();
	}

	@Scheduled(cron = "0 20 7,20 * * *")
	public void realizarOCRDiarioOficialParnaiba() {
		orquestrarOCR(property.getFontes().getParnaiba());
	}

	@Scheduled(cron = "0 50 7,20 * * *")
	public void realizarOCRDiarioOficialPiaui() {
		orquestrarOCR(property.getFontes().getPiaui());
	}

	@Scheduled(cron = "0 20 8,21 * * *")
	public void realizarOCRDiarioOficialTeresina() {
		orquestrarOCR(property.getFontes().getTeresina());
	}

	@Scheduled(cron = "0 50 8,21 * * *")
	public void realizarOCRDiarioOficialMunicipios() {
		orquestrarOCR(property.getFontes().getMunicipios());
	}
	
	private void orquestrarOCR(FonteAuto fonteAuto) {
		logger.info("Iniciando processo de OCR das publicações: " + fonteAuto.getEnumFonte());
		ocrService.realizarOCR(new Long(fonteAuto.getId()));
		logger.info("Finalizando processo de OCR das publicações: " + fonteAuto.getEnumFonte());
	}
}
