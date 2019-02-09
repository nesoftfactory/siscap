package br.gov.pi.tce.publicacoes.controller.beans;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;

import br.gov.pi.tce.publicacoes.clients.ElasticServiceClient;
import br.gov.pi.tce.publicacoes.modelo.elastic.BucketArquivo;
import br.gov.pi.tce.publicacoes.modelo.elastic.BucketDataPublicacao;
import br.gov.pi.tce.publicacoes.modelo.elastic.BucketFonte;
import br.gov.pi.tce.publicacoes.modelo.elastic.BucketPagina;
import br.gov.pi.tce.publicacoes.modelo.elastic.BucketPublicacao;
import br.gov.pi.tce.publicacoes.modelo.elastic.PublicacaoElasticAggregate;
import br.gov.pi.tce.publicacoes.modelo.elastic.PublicacaoElasticGeral;
import br.gov.pi.tce.publicacoes.modelo.elastic.PublicacaoElasticTO;

@Named
@ViewScoped
public class ConsultaDetalhadaPublicacaoController extends BeanController {

	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = Logger.getLogger(ConsultaDetalhadaPublicacaoController.class);
	

	@Inject
	private ElasticServiceClient elasticServiceClient;

	
	private String descricao;
	
	private List<PublicacaoElasticTO> listaPublicacoes = new ArrayList<PublicacaoElasticTO>();

	@PostConstruct
	public void init() {
		limpar();
	}
	
	
	public void consultar() {
		//consultar(descricao);
		//consultarComAgregacao(descricao);
		consultarComAgregacao(descricao);
		System.out.println();
	}

	
	private void consultarComAgregacao(String descricao) {
		PublicacaoElasticAggregate res = elasticServiceClient.consultarComAgragador(descricao);
		if(res != null) {
			listaPublicacoes = getListaPublicacoesElasticTO(res);
		}
		//TODO se lista for vazia colcoar mensagem que não foram encontrados dados
		//FAzer testes de dados de entrada antes de consultar e antes de montar o query
		//colocar os campos de data e deixar já preparado caso as datas venham preenchidas
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
				}
				listaPublicacoes.add(peTO);
			}
		}
		return listaPublicacoes;
	}



	private PublicacaoElasticGeral consultar(String texto) {
		PublicacaoElasticGeral res = elasticServiceClient.consultar(texto);
		return res;
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
	
	


	public void downloadArquivo(){
		
//		try {
//			Publicacao publicacao = (Publicacao)FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("publicacao");
//			Arquivo arquivo = arquivoServiceClient.consultarArquivoPorCodigo(publicacao.getArquivo());
//			
//			
//			ExternalContext econtext = FacesContext.getCurrentInstance().getExternalContext();  
//			HttpServletResponse response = (HttpServletResponse) econtext.getResponse();
//			
//			response.reset();
//			response.addHeader(HEADER_CONTENT_DISPOSITION, ATTACHMENT_FILENAME + arquivo.getNome());
//			try {
//				response.getOutputStream().write(arquivo.getConteudo());
//				response.flushBuffer();
//			} catch (Exception e) {
//				LOGGER.error("Erro realizar o download do arquivo:" + arquivo.getId());
//				LOGGER.error(e.getMessage());
//				e.printStackTrace();
//			}
//			FacesContext.getCurrentInstance().responseComplete();
//		}
//		catch(Exception e) {
//			addMessage(FacesMessage.SEVERITY_ERROR, "Erro realizar download de publicações.", e.getMessage());
//			LOGGER.error("Erro realizar download de publicações:" + e.getMessage());
//			e.printStackTrace();
//		}
		
	}
	
	
	
	
}