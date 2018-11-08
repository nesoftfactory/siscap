package br.gov.pi.tce.publicacoes.controller.beans;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
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
		catch (Exception e) {
			registrarMensagem(FacesMessage.SEVERITY_ERROR, "label.erro", e.getMessage());
		}
		
	}
	
	public List<SelectItem> getFontesParaSelectItems(){
		return getSelectItens(fontes, "nome");
	}

	public void salvar() throws Exception {
		
		if (arquivo.getNome().isEmpty()) {
			registrarMensagem(FacesMessage.SEVERITY_ERROR, "Arquivo de diário não informado.");
			//throw new ValidationException("Arquivo de diário não informado.");
		}
		
		if (publicacaoAnexo.getNome().isEmpty()) {
			
			if (!arquivoAnexo.getNome().isEmpty()) {
				registrarMensagem(FacesMessage.SEVERITY_ERROR, "O nome da publicação do arquivo anexo não foi informado.");
				//throw new ValidationException("O nome da publicação do arquivo anexo não foi informado.");
			}
			
			publicacao.setPossuiAnexo(false);
		} else {
			publicacao.setPossuiAnexo(true);
		}
		
		// Tratamento para data
		String[] dataSplit = publicacao.getData().split("-");
		publicacao.setData(dataSplit[2]+"/"+dataSplit[1]+"/"+dataSplit[0]);
		
		try {
			publicacoes = publicacaoServiceClient.consultarPublicacaoPorFiltro(publicacao.getFonte().getId(), null, null, null,null, publicacao.getData());
			SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
			Date data = formato.parse(publicacao.getData());
			Boolean isFeriado = isFeriado(data, publicacao.getFonte().getId());
			if(isFeriado){
				registrarMensagem(FacesMessage.SEVERITY_ERROR, "Não pode ser cadastrada uma publicação para esta fonte na data solicitada pois está configurada no sistema como feriado.");
			}
			
			if(publicacoes != null && !publicacoes.isEmpty()) {
				Publicacao publicacaoExistente = publicacoes.get(0);
				if(publicacaoExistente != null) {
					if(publicacaoExistente.getSucesso()) {
						registrarMensagem(FacesMessage.SEVERITY_ERROR, "Já foi feito anteriormente um upload de arquivo para a publicação solicitada.");
					}
					else {
						publicacaoServiceClient.alterarPublicacaoPorUpload(publicacaoExistente, publicacao, arquivo, publicacaoAnexo, arquivoAnexo);
						addMessage(FacesMessage.SEVERITY_INFO, "Diário salvo com sucesso.", "Diário atualizado com sucesso.");
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
				addMessage(FacesMessage.SEVERITY_INFO, "Diário salvo com sucesso.", "Diário salvo com sucesso.");
				limpar();
			}
				
		} catch (Exception e) {
			LOGGER.error("Erro realizar o upload da publicação.");
			LOGGER.error(e.getMessage());
			
			registrarMensagem(FacesMessage.SEVERITY_ERROR, "Erro realizar o upload da publicação.");
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
		DateTimeFormatter calendarPattern = DateTimeFormatter.ofPattern("dd/MM/yy");
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