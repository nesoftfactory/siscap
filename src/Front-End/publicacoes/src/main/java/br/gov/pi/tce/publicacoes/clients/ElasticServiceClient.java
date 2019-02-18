package br.gov.pi.tce.publicacoes.clients;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import br.gov.pi.tce.publicacoes.modelo.elastic.Aggs;
import br.gov.pi.tce.publicacoes.modelo.elastic.AggsDataPublicacao;
import br.gov.pi.tce.publicacoes.modelo.elastic.AggsFonte;
import br.gov.pi.tce.publicacoes.modelo.elastic.AggsPaginas;
import br.gov.pi.tce.publicacoes.modelo.elastic.AggsPublicacao;
import br.gov.pi.tce.publicacoes.modelo.elastic.ArquivoGroupBy;
import br.gov.pi.tce.publicacoes.modelo.elastic.BodyConsultaAggragate;
import br.gov.pi.tce.publicacoes.modelo.elastic.BodyConsultaTextoOcr;
import br.gov.pi.tce.publicacoes.modelo.elastic.Bool;
import br.gov.pi.tce.publicacoes.modelo.elastic.DataPublicacaoGroupBy;
import br.gov.pi.tce.publicacoes.modelo.elastic.DataPublicacaoRangeElastic;
import br.gov.pi.tce.publicacoes.modelo.elastic.Filter;
import br.gov.pi.tce.publicacoes.modelo.elastic.FonteGroupBy;
import br.gov.pi.tce.publicacoes.modelo.elastic.MatchConsultaFonte;
import br.gov.pi.tce.publicacoes.modelo.elastic.MatchConsultaTexto;
import br.gov.pi.tce.publicacoes.modelo.elastic.MatchConsultaTextoArquivo;
import br.gov.pi.tce.publicacoes.modelo.elastic.MatchConsultaTextoPagina;
import br.gov.pi.tce.publicacoes.modelo.elastic.Must;
import br.gov.pi.tce.publicacoes.modelo.elastic.Order;
import br.gov.pi.tce.publicacoes.modelo.elastic.PaginaGroupBy;
import br.gov.pi.tce.publicacoes.modelo.elastic.PublicacaoElasticAggregate;
import br.gov.pi.tce.publicacoes.modelo.elastic.PublicacaoElasticGeral;
import br.gov.pi.tce.publicacoes.modelo.elastic.PublicacaoGroupBy;
import br.gov.pi.tce.publicacoes.modelo.elastic.Query;
import br.gov.pi.tce.publicacoes.modelo.elastic.Range;
import br.gov.pi.tce.publicacoes.modelo.elastic.Terms;
import br.gov.pi.tce.publicacoes.modelo.elastic.TermsArquivo;
import br.gov.pi.tce.publicacoes.modelo.elastic.TexoOcrElastic;
import br.gov.pi.tce.publicacoes.util.Propriedades;

@Local
@Stateless(name = "ElasticServiceClient")
public class ElasticServiceClient {


	private static final String RESPONSE_TYPE = "application/json;charset=UTF-8";
	private static final Logger LOGGER = Logger.getLogger(ElasticServiceClient.class);

	private Client client;
	private WebTarget webTarget;

	public ElasticServiceClient() {
		this.client = ClientBuilder.newClient();
	}

	public ElasticServiceClient(String token) {
		this.client = ClientBuilder.newClient();
	}


	public PublicacaoElasticGeral consultar(String texto) {
		try {
			Propriedades propriedades = Propriedades.getInstance();
			this.webTarget = this.client
						.target(propriedades.getValorString("URI_API_ELASTIC") + propriedades.getValorString("URI_API_ELASTIC_CONSULTA_BASICA"));
			Invocation.Builder invocationBuilder = this.webTarget.request(propriedades.getValorString("RESPONSE_TYPE"));
			Response response = invocationBuilder.get();
			PublicacaoElasticGeral pub = response.readEntity(PublicacaoElasticGeral.class);
			return pub;
		} catch (Exception e) {
			LOGGER.error("Erro ao consultar as publicacoes");
			throw e;
		}
	}

	public PublicacaoElasticAggregate consultarComAgragador(Long idFonte, String dtInicio, String dtFim, String descricao) {
		try {
			
			Propriedades propriedades = Propriedades.getInstance();
			this.webTarget = this.client
						.target(propriedades.getValorString("URI_API_ELASTIC") + propriedades.getValorString("URI_API_ELASTIC_CONSULTA_AGGRAGATE"));
			Invocation.Builder invocationBuilder = this.webTarget.request(propriedades.getValorString("RESPONSE_TYPE"));
			BodyConsultaAggragate bodyConsultaAggragate = createBodyConsultaAggragate(descricao, idFonte, dtInicio, dtFim);
			Response response = invocationBuilder.post(Entity.entity(bodyConsultaAggragate,RESPONSE_TYPE));
			PublicacaoElasticAggregate pub = response.readEntity(PublicacaoElasticAggregate.class);
			return pub;
		} catch (Exception e) {
			LOGGER.error("Erro ao consultar as publicacoes");
			throw e;
		}
	}

	private BodyConsultaAggragate createBodyConsultaAggragate(String campo, Long idFonte, String dtInicio, String dtFim) {
		PaginaGroupBy paginasGroupBy = new PaginaGroupBy(new Terms("num_pagina"));
		AggsPaginas aggsPaginas = new AggsPaginas(paginasGroupBy);
		FonteGroupBy fonteGroupBy = new FonteGroupBy(new Terms("nome_fonte.keyword"),aggsPaginas);
		AggsFonte aggsFonte = new AggsFonte(fonteGroupBy);
		DataPublicacaoGroupBy dataPublicacao = new DataPublicacaoGroupBy(new Terms("data_publicacao"),aggsFonte);
		AggsDataPublicacao aggsDataPublicacao = new AggsDataPublicacao(dataPublicacao);
		PublicacaoGroupBy publicacaoGroupBy = new PublicacaoGroupBy(new Terms("nome_publicacao.keyword"),aggsDataPublicacao);
		AggsPublicacao aggsPublicacao = new AggsPublicacao(publicacaoGroupBy);
		ArquivoGroupBy arquivo = new ArquivoGroupBy();
		arquivo.setTerms(new TermsArquivo("id_arquivo", new Order("desc")));
		arquivo.setAggs(aggsPublicacao);
		Aggs aggs = new Aggs(arquivo);
		
		BodyConsultaAggragate body = new BodyConsultaAggragate(aggs);
		
		MatchConsultaTexto match = new MatchConsultaTexto();
		match.setTexto_ocr(campo);
		Must must = new Must();
		must.setMatch(match);

		Bool bool = new Bool();
		List<Must> listaMust = new ArrayList<Must>();
		listaMust.add(must);
		
		if(idFonte != null) {
			MatchConsultaFonte match2 = new MatchConsultaFonte();
			match2.setId_fonte(idFonte);
			Must must2 = new Must();
			must2.setMatch(match2);
			listaMust.add(must2);
		}
		
		bool.setMust(listaMust);
		
		
		Filter filtro = new Filter(new Range(new DataPublicacaoRangeElastic(dtInicio, dtFim, "dd/MM/yyyy")));
		List <Filter> listFiltros = new ArrayList<Filter>();
		
		listFiltros.add(filtro);
		bool.setFilter(listFiltros);
		
		
		Query query = new Query();
		query.setBool(bool);
		
		body.setQuery(query);
		return body;
	}

	public String getPagina(Long idArquivo, int pagina, String busca, boolean ultimaPagina) {
		
		try {
			
			
			Propriedades propriedades = Propriedades.getInstance();
			this.webTarget = this.client
						.target(propriedades.getValorString("URI_API_ELASTIC") + propriedades.getValorString("URI_API_ELASTIC_CONSULTA_TEXTO"));
			Invocation.Builder invocationBuilder = this.webTarget.request(propriedades.getValorString("RESPONSE_TYPE"));
			BodyConsultaTextoOcr bodyConsultaTExtoOcr = createBodyConsultaTextoOcrEspecifico(idArquivo, pagina);
			Response response = invocationBuilder.post(Entity.entity(bodyConsultaTExtoOcr,RESPONSE_TYPE));
			TexoOcrElastic ocr = response.readEntity(TexoOcrElastic.class);
			String resultado = "";
			if(ocr == null || ocr.getHits() == null) {
				return "";
			}
			else {
				resultado = (String)ocr.getHits().getHits().get(0).get_source().getTexto_ocr();
			}
			return getSubstring(resultado, busca, ultimaPagina);
		} catch (Exception e) {
			LOGGER.error("Erro ao consultar as publicacoes");
			throw e;
		}
	}

	private String getSubstring(String ocr, String busca, boolean ultimaPagina) {
		int tamanhoMax = 350;
		int tamOcr = ocr.length();
		int inicio = ocr.toUpperCase().indexOf(busca.toUpperCase());
		if(inicio < 0 ) {
			if(!ultimaPagina) {
				return "";
			}
			else {
				if(tamOcr <= tamanhoMax) {
					return ocr;
				}
				else {
					return ocr.substring(0,tamanhoMax-1);
				}
			}
		}
		else {
			if(tamOcr <= tamanhoMax) {
				return ocr;
			}
			else {
				if(inicio < tamanhoMax) {
					return ocr.substring(0, tamanhoMax);
				}
				else {
					return ocr.substring(inicio-tamanhoMax, inicio+busca.length());
				}
			}
		}
	}
	
	
	private BodyConsultaTextoOcr createBodyConsultaTextoOcrEspecifico(Long idArquivo, int pagina) {
		BodyConsultaTextoOcr body = new BodyConsultaTextoOcr();
		
		
		MatchConsultaTextoPagina match1 = new MatchConsultaTextoPagina();
		match1.setNum_pagina(pagina);
		Must must1 = new Must();
		must1.setMatch(match1);
		
		
		MatchConsultaTextoArquivo match2 = new MatchConsultaTextoArquivo();
		match2.setId_arquivo(idArquivo);
		Must must2 = new Must();
		must2.setMatch(match2);
		
		Bool bool = new Bool();
		List<Must> listaMust = new ArrayList<Must>();
		listaMust.add(must2);
		listaMust.add(must1);
		
		bool.setMust(listaMust);
		
		
		Query query = new Query();
		query.setBool(bool);
		
		body.setQuery(query);
		return body;
	}

}
