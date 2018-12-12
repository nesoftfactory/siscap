package br.gov.pi.tce.publicacoes.services;

import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import br.gov.pi.tce.publicacoes.controller.beans.PublicacaoOCRController;

@Stateless
public class OCRPublicacoesService {
	
	@Inject
	private PublicacaoOCRController publicacaoOCRController;
	
	private static final Logger LOGGER = Logger.getLogger(OCRPublicacoesService.class);
	
	@Schedule(hour="23", minute = "20")
	public void realizarOCRDiarioOficialParnaiba() {
		LOGGER.info("Iniciando processo de OCR das publicações do Diario Oficial de Parnaiba");
		publicacaoOCRController.realizarOCRDiarioOficialParnaiba();
		LOGGER.info("Finalizando processo de OCR das publicações do Diario Oficial de Parnaiba");
	}
	
	@Schedule(hour="23", minute = "30")
	public void realizarOCRDiarioOficialMunicipios() {
		LOGGER.info("Iniciando processo de OCR das publicações do Diario Oficial dos Municipios");
		publicacaoOCRController.realizarOCRDiarioOficialMunicipios();
		LOGGER.info("Finalizando processo de OCR das publicações do Diario Oficial dos Municipios");
	}
	
	@Schedule(hour="23", minute = "40")
	public void realizarOCRDiarioOficialPiaui() {
		LOGGER.info("Iniciando processo de OCR das publicações do Diario Oficial do Piaui");
		publicacaoOCRController.realizarOCRDiarioOficialPiaui();
		LOGGER.info("Finalizando processo de OCR das publicações do Diario Oficial do Piaui");
	}
	
	@Schedule(hour="23", minute = "50")
	public void realizarOCRDiarioOficialTeresina() {
		LOGGER.info("Iniciando processo de OCR das publicações do Diario Oficial de Teresina");
		publicacaoOCRController.realizarOCRDiarioOficialTeresina();
		LOGGER.info("Finalizando processo de OCR das publicações do Diario Oficial de Teresina");
	}
}
