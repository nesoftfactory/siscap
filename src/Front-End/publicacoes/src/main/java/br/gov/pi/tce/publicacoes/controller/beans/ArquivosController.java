package br.gov.pi.tce.publicacoes.controller.beans;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import br.gov.pi.tce.publicacoes.clients.FonteServiceClient;
import br.gov.pi.tce.publicacoes.clients.PublicacaoServiceClient;
import br.gov.pi.tce.publicacoes.modelo.Arquivo;
import br.gov.pi.tce.publicacoes.modelo.Feriado;
import br.gov.pi.tce.publicacoes.modelo.Fonte;
import br.gov.pi.tce.publicacoes.modelo.Publicacao;
import br.gov.pi.tce.publicacoes.modelo.PublicacaoAnexo;

@ManagedBean(name="arquivosController")
@ViewScoped
public class ArquivosController extends BeanController {

	private static final long serialVersionUID = 1L;
	private static final String DIRETORIO_RAIZ = "file://" + System.getProperty("user.dir") + "/";

	private Arquivo arquivo;
	private Arquivo arquivoAnexo;
	private Publicacao publicacao;
	private PublicacaoAnexo publicacaoAnexo;

	private List<Fonte> fontes = Collections.EMPTY_LIST;
	
	private static final Logger LOGGER = Logger.getLogger(ArquivosController.class);
	
	@Inject
	private PublicacaoServiceClient publicacaoServiceClient;
	
	@Inject
	private FonteServiceClient fonteServiceClient;
	
	private List<Publicacao> publicacoes;
	
	@PostConstruct
	public void init() {
		limpar();
		iniciaFontes();
	}
	
	public void limpar() {
		publicacao = new Publicacao();
		arquivo = new Arquivo();
		arquivo.setNome("");
		publicacaoAnexo = new PublicacaoAnexo();
		publicacaoAnexo.setNome("");
		arquivoAnexo = new Arquivo();
		arquivoAnexo.setNome("");
	}
	
	private void iniciaFontes() {
		try {
			fontes = fonteServiceClient.consultarTodasFontes();
		}
		catch (EJBException e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "Serviço indisponível: Fontes.", e.getMessage());
			LOGGER.error("Erro ao iniciar fontes.:" + e.getMessage());
			e.printStackTrace();
		}
		catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao iniciar fontes." + e.getMessage(), e.getMessage());
			LOGGER.error("Erro ao iniciar fontes.:" + e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	public List<SelectItem> getFontesParaSelectItems(){
		return getSelectItens(fontes, "nome");
	}

	public void salvar() throws Exception {
		
		if (arquivo.getNome().isEmpty()) {
			addMessage(FacesMessage.SEVERITY_ERROR, "Arquivo de documento não informado.");
			//throw new ValidationException("Arquivo de documento não informado.");
			return;
		}
		
		publicacao.setPossuiAnexo(false);
		if (publicacaoAnexo.getNome().isEmpty() && !arquivoAnexo.getNome().isEmpty()) {
				addMessage(FacesMessage.SEVERITY_ERROR, "O nome do documento do arquivo anexo não foi informado.");
				return;
		}else if (arquivoAnexo.getNome().isEmpty() && !publicacaoAnexo.getNome().isEmpty())	{
				addMessage(FacesMessage.SEVERITY_ERROR, "O arquivo do anexo não foi informado.");
				return;
		} else if(!publicacaoAnexo.getNome().isEmpty() && !arquivoAnexo.getNome().isEmpty()){
			publicacao.setPossuiAnexo(true);
		}
		
		

		String dt = validaData(publicacao.getData());
		if(dt == null) {
			addMessage(FacesMessage.SEVERITY_ERROR, "Data inválida.");
			return;
		}
		LocalDate today = LocalDate.now();
		DateTimeFormatter calendarPattern = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate dataPub = LocalDate.parse(dt, calendarPattern);
		if(dataPub.isAfter(today)) {
			addMessage(FacesMessage.SEVERITY_ERROR, "Data inválida. A maior data permitida é:" + getToday());
			return;
		}
		publicacao.setData(dt);
		
		try {
			publicacoes = publicacaoServiceClient.consultarPublicacaoPorFiltro(publicacao.getFonte().getId(), null, null, null,null, publicacao.getData());
			SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
			Date data = formato.parse(publicacao.getData());
			Boolean isFeriado = isFeriado(data, publicacao.getFonte().getId());
			if(isFeriado){
				addMessage(FacesMessage.SEVERITY_ERROR, "Não pode ser cadastrado um documento para esta fonte na data solicitada, pois está configurado no sistema como feriado.");
			}
			
			if(publicacoes != null && !publicacoes.isEmpty()) {
				Publicacao publicacaoExistente = publicacoes.get(0);
				if(publicacaoExistente != null) {
					if(publicacaoExistente.getSucesso()) {
						addMessage(FacesMessage.SEVERITY_ERROR, "Já foi feito anteriormente um upload de arquivo para o documento solicitado.");
					}
					else {
						publicacaoServiceClient.alterarPublicacaoPorUpload(publicacaoExistente, publicacao, arquivo, publicacaoAnexo, arquivoAnexo);
						addMessage(FacesMessage.SEVERITY_INFO, "Documento salvo com sucesso.", "Documento atualizado com sucesso.");
						limpar();
					}
				}
			}
			else {
				publicacaoServiceClient.cadastrarPublicacaoPorUpload(publicacao, arquivo, publicacaoAnexo, arquivoAnexo);
				//			publicacaoServiceClient.armazenarArquivo(arquivo);
				//			if (publicacao.getPossuiAnexo() && !arquivoAnexo.getNome().isEmpty()) {
				//				publicacaoServiceClient.armazenarArquivo(arquivoAnexo);
				//			}
				addMessage(FacesMessage.SEVERITY_INFO, "Documento salvo com sucesso.", "Documento salvo com sucesso.");
				limpar();
			}
			
				
		} 
		catch (EJBException e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "Serviço indisponível: Documentos.", e.getMessage());
			LOGGER.error("Erro realizar o upload do documento.:" + e.getMessage());
			e.printStackTrace();
		}
		catch (Exception e) {
			LOGGER.error("Erro realizar o upload do documento.");
			LOGGER.error(e.getMessage());
			addMessage(FacesMessage.SEVERITY_ERROR, "Erro realizar o upload do documento: " + e.getMessage());
		    //throw new Exception(e);
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
	
	public Publicacao getPublicacao() {
		return publicacao;
	}

	public void setPublicacao(Publicacao publicacao) {
		this.publicacao = publicacao;
	}

	public Arquivo getArquivo() {
		return arquivo;
	}

	public void setArquivo(Arquivo arquivo) {
		this.arquivo = arquivo;
	}

	public Arquivo getArquivoAnexo() {
		return arquivoAnexo;
	}

	public void setArquivoAnexo(Arquivo arquivoAnexo) {
		this.arquivoAnexo = arquivoAnexo;
	}

	public PublicacaoAnexo getPublicacaoAnexo() {
		return publicacaoAnexo;
	}

	public void setPublicacaoAnexo(PublicacaoAnexo publicacaoAnexo) {
		this.publicacaoAnexo = publicacaoAnexo;
	}

	public String getToday() {
		LocalDate today = LocalDate.now();
		DateTimeFormatter calendarPattern = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return today.format(calendarPattern);
	}
	
	/**
	 * Verifica se a data é um feriado para uma determinada fonte.
	 * 
	 * @param date
	 * @param idFonte
	 * @return retorno
	 */
	private Boolean isFeriado(Date date, Long idFonte) {
		List<Feriado> feriadoList = publicacaoServiceClient.consultarFeriadoPorFontePeriodo(idFonte, asLocalDate(date),
				asLocalDate(date));
		Boolean retorno = (feriadoList != null && !(feriadoList.isEmpty()));
		return retorno;
	}

    
}