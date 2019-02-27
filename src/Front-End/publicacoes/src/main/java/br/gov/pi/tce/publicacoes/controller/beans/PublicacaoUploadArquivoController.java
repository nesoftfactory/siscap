package br.gov.pi.tce.publicacoes.controller.beans;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import br.gov.pi.tce.publicacoes.clients.PublicacaoServiceClient;
import br.gov.pi.tce.publicacoes.modelo.Arquivo;
import br.gov.pi.tce.publicacoes.modelo.Publicacao;
import br.gov.pi.tce.publicacoes.modelo.PublicacaoAnexo;

@Named
@ViewScoped
public class PublicacaoUploadArquivoController extends BeanController {

	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = Logger.getLogger(PublicacaoUploadArquivoController.class);
	
	private static final String DIRETORIO_RAIZ = "file://" + System.getProperty("user.dir") + "/";
	
	private Arquivo arquivo;
	private Arquivo arquivoAnexo;
	private Publicacao publicacaoSelecionada;
	private PublicacaoAnexo publicacaoAnexo;
	private String idPublicacao;
	
	@Inject
	private PublicacaoServiceClient publicacaoServiceClient;
	
	
	@PostConstruct
	public void init() {
		try {
			limpar();
			popupUploadManualArquivo();
		}
		catch (EJBException e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "Serviço indisponível: Upload Manual.", e.getMessage());
			LOGGER.error("Erro ao iniciar upload manual:" + e.getMessage());
			e.printStackTrace();
		}
		 catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao iniciar upload manual.", e.getMessage());
			LOGGER.error("Erro ao iniciar upload manual:" + e.getMessage());
			e.printStackTrace();
		 }	
		
	}
	
	private void popupUploadManualArquivo()  {
		idPublicacao = (String)FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
		if (idPublicacao != null) {
			publicacaoSelecionada = publicacaoServiceClient.consultarPublicacaoPorCodigo(Long.valueOf(idPublicacao));
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("publicacaoSelecionada",
					publicacaoSelecionada);
		}
	}
	
	public void salvarArquivo()  {
		try {
			if(arquivo == null || arquivo.getNome() ==null || arquivo.getNome().equals("")) {
				addMessage(FacesMessage.SEVERITY_WARN, "Favor selecionar um arquivo PDF.");
			}else {
				publicacaoSelecionada.setNome(arquivo.getNome());
				publicacaoSelecionada.setSucesso(Boolean.TRUE);
				publicacaoSelecionada.setData(publicacaoSelecionada.getDataString());
				//publicacaoSelecionada.setDataCriacao(publicacaoSelecionada.getDataString());
				publicacaoSelecionada.setQuantidadeTentativasIndexacao(0L);
				publicacaoSelecionada.setQuantidadeTentativasOCR(0L);
//				publicacaoSelecionada.setPossuiNotificacao(Boolean.FALSE);
//				publicacaoSelecionada.setPossuiAnexo(Boolean.FALSE);
//				publicacaoSelecionada.setSituacao("");
				publicacaoSelecionada = publicacaoServiceClient.alterarPublicacao(publicacaoSelecionada, arquivo, true);
				addMessage(FacesMessage.SEVERITY_INFO, "Upload manual realizado com sucesso");
			}
		} catch (Exception e) {
			LOGGER.error("Erro realizar o upload manual do anexo.");
			LOGGER.error(e.getMessage());
			addMessage(FacesMessage.SEVERITY_ERROR, "Erro realizar o upload manual do anexo: " + e.getMessage());
		}
	}
	
	public void salvarArquivoAnexo()  {
		try {
			if(arquivoAnexo == null || arquivoAnexo.getNome() ==null || arquivoAnexo.getNome().equals("")) {
				addMessage(FacesMessage.SEVERITY_WARN, "Favor selecionar um arquivo PDF.");
			}else {
				//publicacaoSelecionada.setNome(arquivoAnexo.getNome());
				//publicacaoSelecionada.setSucesso(Boolean.TRUE);
				publicacaoSelecionada.setData(publicacaoSelecionada.getDataString());
				//publicacaoSelecionada.setDataCriacao(publicacaoSelecionada.getDataString());
				//publicacaoSelecionada.setQuantidadeTentativasIndexacao(0L);
				//publicacaoSelecionada.setQuantidadeTentativasOCR(0L);
//				publicacaoSelecionada.setPossuiNotificacao(Boolean.FALSE);
				publicacaoSelecionada.setPossuiAnexo(Boolean.TRUE);
//				publicacaoSelecionada.setSituacao("");
				//publicacaoSelecionada = publicacaoServiceClient.alterarPublicacao(publicacaoSelecionada, arquivoAnexo, true);
				
				publicacaoAnexo.setPublicacao(publicacaoSelecionada);
				publicacaoAnexo.setNome(arquivoAnexo.getNome());
				publicacaoAnexo.setSucesso(Boolean.TRUE);
				publicacaoAnexo = publicacaoServiceClient.cadastrarPublicacaoAnexo(publicacaoAnexo, arquivoAnexo, true);
				addMessage(FacesMessage.SEVERITY_INFO, "Upload manual do anexo realizado com sucesso");
			}
		} catch (Exception e) {
			LOGGER.error("Erro realizar o upload manual do anexo.");
			LOGGER.error(e.getMessage());
			addMessage(FacesMessage.SEVERITY_ERROR, "Erro realizar o upload manual do anexo: " + e.getMessage());
		}
	}
	
	public void uploadFile(FileUploadEvent event) throws IOException {
		UploadedFile uploadedFile = event.getFile();
		arquivo.setNome(uploadedFile.getFileName());
		arquivo.setTamanho(uploadedFile.getSize());
		arquivo.setTipo(uploadedFile.getContentType());
		arquivo.setConteudo(uploadedFile.getContents());
		arquivo.setLink(DIRETORIO_RAIZ + arquivo.getNome());
		arquivo.setInputStream(uploadedFile.getInputstream());
	   }    
		
	public void uploadFileAnexo(FileUploadEvent event) throws IOException {
		UploadedFile uploadedFile = event.getFile();
		arquivoAnexo.setNome(uploadedFile.getFileName());
		arquivoAnexo.setTamanho(uploadedFile.getSize());
		arquivoAnexo.setTipo(uploadedFile.getContentType());
		arquivoAnexo.setConteudo(uploadedFile.getContents());
		arquivoAnexo.setLink(DIRETORIO_RAIZ + arquivoAnexo.getNome());
		arquivoAnexo.setInputStream(uploadedFile.getInputstream());
    }
	
	public void limpar() {
		publicacaoSelecionada = new Publicacao();
		limparTela();
	}

	public void limparTela() {
		arquivo = new Arquivo();
		arquivo.setNome("");
		publicacaoAnexo = new PublicacaoAnexo();
		publicacaoAnexo.setNome("");
		arquivoAnexo = new Arquivo();
		arquivoAnexo.setNome("");
	}
	
	/**
	 * @return the publicacaoSelecionada
	 */
	public Publicacao getPublicacaoSelecionada() {
		return publicacaoSelecionada;
	}

	/**
	 * @param publicacaoSelecionada the publicacaoSelecionada to set
	 */
	public void setPublicacaoSelecionada(Publicacao publicacaoSelecionada) {
		this.publicacaoSelecionada = publicacaoSelecionada;
	}

	/**
	 * @return the arquivo
	 */
	public Arquivo getArquivo() {
		return arquivo;
	}

	/**
	 * @param arquivo the arquivo to set
	 */
	public void setArquivo(Arquivo arquivo) {
		this.arquivo = arquivo;
	}

	/**
	 * @return the arquivoAnexo
	 */
	public Arquivo getArquivoAnexo() {
		return arquivoAnexo;
	}

	/**
	 * @param arquivoAnexo the arquivoAnexo to set
	 */
	public void setArquivoAnexo(Arquivo arquivoAnexo) {
		this.arquivoAnexo = arquivoAnexo;
	}

	/**
	 * @return the publicacaoAnexo
	 */
	public PublicacaoAnexo getPublicacaoAnexo() {
		return publicacaoAnexo;
	}

	/**
	 * @param publicacaoAnexo the publicacaoAnexo to set
	 */
	public void setPublicacaoAnexo(PublicacaoAnexo publicacaoAnexo) {
		this.publicacaoAnexo = publicacaoAnexo;
	}
	
}