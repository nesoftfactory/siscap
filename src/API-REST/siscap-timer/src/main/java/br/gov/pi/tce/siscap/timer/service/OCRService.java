package br.gov.pi.tce.siscap.timer.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.pi.tce.siscap.timer.client.OCRServiceClient;
import br.gov.pi.tce.siscap.timer.model.Publicacao;
import br.gov.pi.tce.siscap.timer.model.enums.SituacaoPublicacao;
import br.gov.pi.tce.siscap.timer.scheduler.ColetorPublicacoes;

@Service
public class OCRService {
	private static final Logger logger = LoggerFactory.getLogger(ColetorPublicacoes.class);

	@Autowired
	private OCRServiceClient ocrServiceClient;
	
	@Autowired
	private NotificacaoService notificacaoService;
	
	public void realizarOCR(Long idFonte) {
		realizarOCRPublicacao(idFonte);
		realizarOCRAnexo(idFonte);
		// TODO Implementar OCR de Outras fontes
	}

	private void realizarOCRAnexo(Long idFonte) {
		// TODO Implementar OCR de Anexo
	}

	private void realizarOCRPublicacao(Long idFonte) {
		Publicacao publicacaoAtual = null;
		try {
			List<Publicacao> listaPublicacoesParaOCR = ocrServiceClient.consultarPublicacaoAptaParaOCR(idFonte); 

			if (listaPublicacoesParaOCR != null && listaPublicacoesParaOCR.size() > 0) {
				
				logger.info("Iniciando OCR para " + listaPublicacoesParaOCR.size() + " publicação(ões) apta(s)");
				for (Publicacao publicacao : listaPublicacoesParaOCR) {
					publicacaoAtual = publicacao;
					logger.info("Iniciando OCR da publicação:" + publicacao);
					Publicacao publicacaoOCR = ocrServiceClient.realizarOCRPublicacao(publicacao); 
					if (publicacaoOCR != null
							&& publicacaoOCR.getSituacao().equals(SituacaoPublicacao.OCR_REALIZADO.getDescricao())) {
						logger.info("OCR da publicação:" + publicacao + " realizado com sucesso. Situação: "
								+ publicacaoOCR.getSituacao());
					} else {
						logger.error("OCR da publicação:" + publicacao + " NÃO realizado. Situação: "
								+ publicacaoOCR.getSituacao());
						notificacaoService.checarNotificacaoOCR(publicacaoAtual);
					}
				}
	
				logger.info("Finalizando processo de OCR de publicações");
			} else {
				logger.info("Não foram encontradas publicações aptas para OCR");				
			}
		} catch (Exception e) {
			logger.error("Erro ao realizar OCR de publicações.:" + e.getMessage());
			if(publicacaoAtual != null) {
				notificacaoService.checarNotificacaoOCR(publicacaoAtual);
			}
		}
	}

}
