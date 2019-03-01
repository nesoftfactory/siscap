package br.gov.pi.tce.publicacoes.controller.beans;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import br.gov.pi.tce.publicacoes.clients.ArquivoServiceClient;
import br.gov.pi.tce.publicacoes.clients.ElasticServiceClient;
import br.gov.pi.tce.publicacoes.clients.FonteServiceClient;
import br.gov.pi.tce.publicacoes.modelo.Arquivo;
import br.gov.pi.tce.publicacoes.modelo.Fonte;
import br.gov.pi.tce.publicacoes.modelo.elastic.BucketArquivo;
import br.gov.pi.tce.publicacoes.modelo.elastic.BucketDataPublicacao;
import br.gov.pi.tce.publicacoes.modelo.elastic.BucketFonte;
import br.gov.pi.tce.publicacoes.modelo.elastic.BucketPagina;
import br.gov.pi.tce.publicacoes.modelo.elastic.BucketPublicacao;
import br.gov.pi.tce.publicacoes.modelo.elastic.PublicacaoElasticAggregate;
import br.gov.pi.tce.publicacoes.modelo.elastic.PublicacaoElasticTO;

@Named
@ViewScoped
public class ConsultaDetalhadaPublicacaoController extends BeanController {

	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = Logger.getLogger(ConsultaDetalhadaPublicacaoController.class);
	
	private static final String HEADER_CONTENT_DISPOSITION = "Content-disposition";
	private static final String ATTACHMENT_FILENAME = "attachment; filename=";
	

	@Inject
	private ElasticServiceClient elasticServiceClient;
	
	@Inject
	private ArquivoServiceClient arquivoServiceClient;

	
	private String descricao;
	
	private List<PublicacaoElasticTO> listaPublicacoes = new ArrayList<PublicacaoElasticTO>();
	
	private String dataInicio;
	
	private Fonte fonte;
	
	private String dataFim;
	
	private List<Fonte> fontes = Collections.EMPTY_LIST;
	
	@Inject
	private FonteServiceClient fonteServiceClient;

	@PostConstruct
	public void init() {
		limpar();
		iniciaFontes();
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
			addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao iniciar fontes.", e.getMessage());
			LOGGER.error("Erro ao iniciar fontes:" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void consultar() {
		consultarComAgregacao(descricao);
	}

	
	private void consultarComAgregacao(String descricao) {
		try {
			LocalDate dtInicio = null;
			LocalDate dtFim = null;
			if(dataInicio == null || dataFim == null) {
				addMessage(FacesMessage.SEVERITY_ERROR, "As datas inicio e fim são obrigatórias.", "");
				return;
			}
			else {
				try {
					dtInicio = LocalDate.parse(dataInicio, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
					dtFim = LocalDate.parse(dataFim, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
					
					if(dtFim.isBefore(dtInicio)) {
						addMessage(FacesMessage.SEVERITY_ERROR, "A data final deve ser maior que a data inicial.", "");
						return;
					}
				} catch (Exception e) {
					addMessage(FacesMessage.SEVERITY_ERROR, "As datas inicio e/ou fim estão no formato errado (dd/MM/yyyy).", "");
					throw new Exception("Data Inválida");
				}

			}
			PublicacaoElasticAggregate res = elasticServiceClient.consultarComAgregador(fonte!=null?fonte.getId():null, dataInicio, dataFim, descricao);
			if(res != null && res.getAggregations() != null) {
				listaPublicacoes = getListaPublicacoesElasticTO(res);
			}
			else {
				addMessage(FacesMessage.SEVERITY_INFO, "Nenhum resultado foi encontrado.");
			}
		}
		catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "Serviço indisponível: ElasticSearch.", e.getMessage());
			LOGGER.error("Erro ao consultar documentos (ElasticSearch):" + e.getMessage());
		}
		
	}


	private List<PublicacaoElasticTO> getListaPublicacoesElasticTO(PublicacaoElasticAggregate res) {
		listaPublicacoes = new ArrayList<PublicacaoElasticTO>();
		if(res != null) {
			for (Iterator iterator = res.getAggregations().getArquivo().getBuckets().iterator(); iterator.hasNext();) {
				BucketArquivo bucketArquivo = (BucketArquivo) iterator.next();
				PublicacaoElasticTO peTO = new PublicacaoElasticTO();
				peTO.setIdArquivo(bucketArquivo.getKey());
				BucketPublicacao bucketPublicacao = bucketArquivo.getPublicacao().getBuckets().get(0);
				peTO.setNomePublicacao(bucketPublicacao.getKey());
				BucketDataPublicacao bucketDataPublicacao = bucketPublicacao.getDataPublicacao().getBuckets().get(0);
				peTO.setDataPublicacao(bucketDataPublicacao.getKey_as_string());
				BucketFonte bucketFonte= bucketDataPublicacao.getFonte().getBuckets().get(0);
				peTO.setFonte(bucketFonte.getKey());
				List<BucketPagina> listaBucketPagina = bucketFonte.getPaginas().getBuckets();
				for (BucketPagina bucketPagina : listaBucketPagina) {
					peTO.getPaginas().add(bucketPagina.getKey()+"");
					if(peTO.getConteudoPrimeiraPagina() == null || peTO.getConteudoPrimeiraPagina().equalsIgnoreCase("")) {
						carregaTextosOcrsPaginas(peTO, bucketPagina.getKey(), peTO.getPaginas().size() == listaBucketPagina.size());
					}
				}
				listaPublicacoes.add(peTO);
			}
		}
		return listaPublicacoes;
	}



	private void carregaTextosOcrsPaginas(PublicacaoElasticTO peTO, int pagina, boolean ultimaPAgina) {
		String texto = elasticServiceClient.getPagina(peTO.getIdArquivo(), pagina, descricao, ultimaPAgina);
		peTO.setConteudoPrimeiraPagina(texto);
	}


	public String getDescricao() {
		return descricao;
	}


	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}


	public void limpar() {
		descricao = "";
		listaPublicacoes = new ArrayList<>();
	}
	
	
	
	
	public List<PublicacaoElasticTO> getListaPublicacoes() {
		return listaPublicacoes;
	}


	public void setListaPublicacoes(List<PublicacaoElasticTO> listaPublicacoes) {
		this.listaPublicacoes = listaPublicacoes;
	}
	

	public List<SelectItem> getFontesParaSelectItems(){
		return getSelectItens(fontes, "nome");
	}


	public void downloadArquivo(){
		
		try {
			PublicacaoElasticTO publicacao = (PublicacaoElasticTO)FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("publicacao");
			Arquivo arquivo = arquivoServiceClient.consultarArquivoPorCodigo(publicacao.getIdArquivo());
			
			
			ExternalContext econtext = FacesContext.getCurrentInstance().getExternalContext();  
			HttpServletResponse response = (HttpServletResponse) econtext.getResponse();
			
			response.reset();
			response.addHeader(HEADER_CONTENT_DISPOSITION, ATTACHMENT_FILENAME + arquivo.getNome());
			try {
				response.getOutputStream().write(arquivo.getConteudo());
				response.flushBuffer();
			} catch (Exception e) {
				LOGGER.error("Erro realizar o download do arquivo:" + arquivo.getId());
				LOGGER.error(e.getMessage());
				e.printStackTrace();
			}
			FacesContext.getCurrentInstance().responseComplete();
		}
		catch(Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "Erro realizar download de documentos.", e.getMessage());
			LOGGER.error("Erro realizar download de documentos:" + e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	public String getDataInicio() {
		return dataInicio;
	}


	public void setDataInicio(String dataInicio) {
		this.dataInicio = dataInicio;
	}


	public Fonte getFonte() {
		return fonte;
	}


	public void setFonte(Fonte fonte) {
		this.fonte = fonte;
	}


	public String getDataFim() {
		return dataFim;
	}


	public void setDataFim(String dataFim) {
		this.dataFim = dataFim;
	}


	public List<Fonte> getFontes() {
		return fontes;
	}


	public void setFontes(List<Fonte> fontes) {
		this.fontes = fontes;
	}
	
	
	
	
	
	
}