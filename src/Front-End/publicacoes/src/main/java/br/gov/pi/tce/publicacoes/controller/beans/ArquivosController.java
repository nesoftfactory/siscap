package br.gov.pi.tce.publicacoes.controller.beans;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ValidationException;
import javax.faces.model.SelectItem;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import br.gov.pi.tce.publicacoes.controller.beans.BeanController;
import br.gov.pi.tce.publicacoes.clients.FonteServiceClient;
import br.gov.pi.tce.publicacoes.clients.PublicacaoServiceClient;
import br.gov.pi.tce.publicacoes.modelo.Arquivo;
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
	
	@Inject
	private PublicacaoServiceClient publicacaoServiceClient;
	
	@Inject
	private FonteServiceClient fonteServiceClient;
	
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
			throw new ValidationException("Arquivo de diário não informado.");
		}
		
		if (publicacaoAnexo.getNome().isEmpty()) {
			
			if (!arquivoAnexo.getNome().isEmpty()) {
				throw new ValidationException("O nome da publicação do arquivo anexo não foi informado.");
			}
			
			publicacao.setPossuiAnexo(false);
		} else {
			publicacao.setPossuiAnexo(true);
		}
		
		// Tratamento para data
		String[] dataSplit = publicacao.getData().split("-");
		publicacao.setData(dataSplit[2]+"/"+dataSplit[1]+"/"+dataSplit[0]);
		
		try {
			
		publicacaoServiceClient.cadastrarPublicacaoPorUpload(publicacao, arquivo, publicacaoAnexo, arquivoAnexo);
		publicacaoServiceClient.armazenarArquivo(arquivo);
		if (!arquivoAnexo.getNome().isEmpty()) {
			publicacaoServiceClient.armazenarArquivo(arquivoAnexo);
		}
		
		addMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Diário salvo com sucesso.");
			
		} catch (Exception e) {
		    throw new Exception(e);
		}
			
		limpar();
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
    
}