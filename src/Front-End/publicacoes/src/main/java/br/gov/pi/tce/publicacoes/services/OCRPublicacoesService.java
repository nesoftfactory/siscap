package br.gov.pi.tce.publicacoes.services;

import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import br.gov.pi.tce.publicacoes.controller.beans.PublicacaoOCRController;
import br.gov.pi.tce.publicacoes.controller.beans.SegurancaController;
import br.gov.pi.tce.publicacoes.modelo.RespostaToken;
import br.gov.pi.tce.publicacoes.util.Propriedades;

@Stateless
public class OCRPublicacoesService {

	@Inject
	private PublicacaoOCRController publicacaoOCRController;

	@Inject
	private SegurancaController segurancaController;

	private String tokenService;

	private static final Logger LOGGER = Logger.getLogger(OCRPublicacoesService.class);

	private String getToken() {
		String token = null;
//		Propriedades propriedades = Propriedades.getInstance();
//		RespostaToken respostaToken = segurancaController.pegarToken(propriedades.getValorString("TOKEN_CLIENT"),
//				propriedades.getValorString("TOKEN_USERNAME"), propriedades.getValorString("TOKEN_PASSWORD"),
//				propriedades.getValorString("TOKEN_GRAND_TYPE"));
//		if (respostaToken != null) {
//			token = respostaToken.getAccess_token();
//		}
		return token;
	}

	//@Schedule(hour = "19", minute = "11")
	public void realizarOCRDiarioOficialParnaiba() {
		LOGGER.info("Iniciando processo de OCR das publicações do Diario Oficial de Parnaiba");
		if (tokenService == null || tokenService.equals("")) {
			tokenService = getToken();
		}
		publicacaoOCRController.realizarOCRDiarioOficialParnaiba(tokenService);
		LOGGER.info("Finalizando processo de OCR das publicações do Diario Oficial de Parnaiba");
	}

	//@Schedule(hour = "09", minute = "11")
	public void realizarOCRDiarioOficialMunicipios() {
		LOGGER.info("Iniciando processo de OCR das publicações do Diario Oficial dos Municipios");
		if (tokenService == null || tokenService.equals("")) {
			tokenService = getToken();
		}
		publicacaoOCRController.realizarOCRDiarioOficialMunicipios(tokenService);
		LOGGER.info("Finalizando processo de OCR das publicações do Diario Oficial dos Municipios");
	}

	//@Schedule(hour = "09", minute = "11")
	public void realizarOCRDiarioOficialPiaui() {
		LOGGER.info("Iniciando processo de OCR das publicações do Diario Oficial do Piaui");
		if (tokenService == null || tokenService.equals("")) {
			tokenService = getToken();
		}
		publicacaoOCRController.realizarOCRDiarioOficialPiaui(tokenService);
		LOGGER.info("Finalizando processo de OCR das publicações do Diario Oficial do Piaui");
	}

	@Schedule(hour = "19", minute = "46")
	public void realizarOCRDiarioOficialTeresina() {
		LOGGER.info("Iniciando processo de OCR das publicações do Diario Oficial de Teresina");
		if (tokenService == null || tokenService.equals("")) {
			tokenService = getToken();
		}
		publicacaoOCRController.realizarOCRDiarioOficialTeresina(tokenService);
		LOGGER.info("Finalizando processo de OCR das publicações do Diario Oficial de Teresina");
	}
}
