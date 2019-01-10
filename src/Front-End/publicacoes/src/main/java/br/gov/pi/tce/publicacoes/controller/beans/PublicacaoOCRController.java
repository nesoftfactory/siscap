package br.gov.pi.tce.publicacoes.controller.beans;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import br.gov.pi.tce.publicacoes.clients.PublicacaoOCRServiceClient;
import br.gov.pi.tce.publicacoes.clients.PublicacaoServiceClient;
import br.gov.pi.tce.publicacoes.modelo.Publicacao;
import br.gov.pi.tce.publicacoes.modelo.PublicacaoAnexo;
import br.gov.pi.tce.publicacoes.modelo.enums.SituacaoPublicacao;
import br.gov.pi.tce.publicacoes.services.NotificacaoService;
import br.gov.pi.tce.publicacoes.util.Propriedades;

@Stateless
public class PublicacaoOCRController extends BeanController{

	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = Logger.getLogger(PublicacaoOCRController.class);
	
	@Inject
	private PublicacaoOCRServiceClient publicacaoOCRServiceClient;
	
	@EJB
	private NotificacaoService notificacao;
	
	Propriedades propriedades = Propriedades.getInstance();

	public void realizarOCRDiarioOficialParnaiba(String token) {
		realizarOCRPublicacaoGenerico(propriedades.getValorLong("ID_FONTE_DIARIO_OFICIAL_PARNAIBA"), token);
		realizarOCRAnexoPublicacaoGenerico(propriedades.getValorLong("ID_FONTE_DIARIO_OFICIAL_PARNAIBA"), token);
		
	}

	public void realizarOCRDiarioOficialMunicipios(String token) {
		realizarOCRPublicacaoGenerico(propriedades.getValorLong("ID_FONTE_DIARIO_OFICIAL_DOS_MUNICIPIOS"), token);
		realizarOCRAnexoPublicacaoGenerico(propriedades.getValorLong("ID_FONTE_DIARIO_OFICIAL_DOS_MUNICIPIOS"), token);
		
	}

	public void realizarOCRDiarioOficialPiaui(String token) {
		realizarOCRPublicacaoGenerico(propriedades.getValorLong("ID_FONTE_DIARIO_OFICIAL_PIAUI"), token);
		realizarOCRAnexoPublicacaoGenerico(propriedades.getValorLong("ID_FONTE_DIARIO_OFICIAL_PIAUI"), token);
		
	}

	public void realizarOCRDiarioOficialTeresina(String token) {
		realizarOCRPublicacaoGenerico(propriedades.getValorLong("ID_FONTE_DIARIO_OFICIAL_TERESINA"), token);
		realizarOCRAnexoPublicacaoGenerico(propriedades.getValorLong("ID_FONTE_DIARIO_OFICIAL_TERESINA"), token);
	}
	
	
	public void realizarOCRPublicacaoGenerico(Long fonte, String token) {
		try {
			LOGGER.info("Iniciando processo de OCR de publicações");
			
			LOGGER.info("Iniciando busca das publicações que devem ter OCR realizado");
			if (token != null) {
				publicacaoOCRServiceClient = new PublicacaoOCRServiceClient(token);
			}
			List<Publicacao> listaPublicacoesParaOCR = publicacaoOCRServiceClient.consultarTodasPublicacoesAptasParaOCR(fonte);
			LOGGER.info("Finalizando busca publicações que devem ter OCR realizado com sucesso");
			
			LOGGER.info("Iniciando OCR de cada publicação apta");
			for (Publicacao publicacao : listaPublicacoesParaOCR) {
				LOGGER.info("Iniciando OCR da publicação:" + publicacao.getId());
				Publicacao publicacaoOCR = publicacaoOCRServiceClient.realizarOCRPublicacao(publicacao);
				if(publicacaoOCR != null && publicacaoOCR.getSituacao().equals(SituacaoPublicacao.OCR_REALIZADO.getDescricao())) {
					LOGGER.info("OCR da publicação:" + publicacao.getId() + " realizado com sucesso. Situação: " + publicacaoOCR.getSituacao());
				}
				else {
					LOGGER.error("OCR da publicação:" + publicacao.getId() + " NÃO realizado. Situação: " + publicacaoOCR.getSituacao());
				}
				LOGGER.info("Finalizando OCR da publicação:" + publicacao.getId());
			}
			LOGGER.info("Finalizando OCR de cada publicação apta");
			
			LOGGER.info("Finalizando processo de OCR de publicações");
			
		}
		catch (EJBException e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "Serviço indisponível: OCR de publicações.", e.getMessage());
			LOGGER.error("Erro ao realizar OCR de publicações.:" + e.getMessage());
			e.printStackTrace();
		}
		catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao realizar OCR de publicações." + e.getMessage(), e.getMessage());
			LOGGER.error("Erro ao realizar OCR de publicações.:" + e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	
	public void realizarOCRAnexoPublicacaoGenerico(Long fonte, String token) {
		try {
			LOGGER.info("Iniciando processo de OCR de anexo de publicações");
			
			LOGGER.info("Iniciando busca de anexos das publicações que devem ter OCR realizado");
			if (token != null) {
				publicacaoOCRServiceClient = new PublicacaoOCRServiceClient(token);
			}
			List<PublicacaoAnexo> listaAnexosPublicacoesParaOCR = publicacaoOCRServiceClient.consultarTodosAnexosPublicacoesAptosParaOCR(fonte);
			LOGGER.info("Finalizando busca de anexos de publicações que devem ter OCR realizado com sucesso");
			
			LOGGER.info("Iniciando OCR de cada anexo de publicação apta");
			for (PublicacaoAnexo publicacaoAnexo : listaAnexosPublicacoesParaOCR) {
				LOGGER.info("Iniciando OCR do anexo: " +publicacaoAnexo.getId()  + ", da publicação:" + publicacaoAnexo.getPublicacao().getId());
				PublicacaoAnexo publicacaoAnexoOCR = publicacaoOCRServiceClient.realizarOCRAnexo(publicacaoAnexo);
				if(publicacaoAnexoOCR != null && publicacaoAnexoOCR.getSituacao().equals(SituacaoPublicacao.OCR_REALIZADO.getDescricao())) {
					LOGGER.info("OCR do anexo:" + publicacaoAnexo.getId() + " da publicação:" + publicacaoAnexo.getPublicacao().getId() + " realizado com sucesso. Situação: " + publicacaoAnexoOCR.getSituacao());
				}
				else {
					LOGGER.error("OCR do anexo:" + publicacaoAnexo.getId() + " da publicação:" + publicacaoAnexo.getPublicacao().getId() + " NÃO realizado com sucesso. Situação: " + publicacaoAnexoOCR.getSituacao());
				}
				LOGGER.info("Finalizando OCR do anexo da publicação:" + publicacaoAnexo.getId());
			}
			LOGGER.info("Finalizando OCR de cada anexo de publicação apta");
			
			LOGGER.info("Finalizando processo de OCR de anexo de publicações");
			
		}
		catch (EJBException e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "Serviço indisponível: OCR de publicações.", e.getMessage());
			LOGGER.error("Erro ao realizar OCR de publicações.:" + e.getMessage());
			e.printStackTrace();
		}
		catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao realizar OCR de publicações." + e.getMessage(), e.getMessage());
			LOGGER.error("Erro ao realizar OCR de publicações.:" + e.getMessage());
			e.printStackTrace();
		}
		
	}


	
	
}
