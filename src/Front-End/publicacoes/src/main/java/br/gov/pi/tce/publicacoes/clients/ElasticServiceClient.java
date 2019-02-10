package br.gov.pi.tce.publicacoes.clients;

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
import br.gov.pi.tce.publicacoes.modelo.elastic.Bool;
import br.gov.pi.tce.publicacoes.modelo.elastic.DataPublicacaoGroupBy;
import br.gov.pi.tce.publicacoes.modelo.elastic.FonteGroupBy;
import br.gov.pi.tce.publicacoes.modelo.elastic.Match;
import br.gov.pi.tce.publicacoes.modelo.elastic.Must;
import br.gov.pi.tce.publicacoes.modelo.elastic.Order;
import br.gov.pi.tce.publicacoes.modelo.elastic.PaginaGroupBy;
import br.gov.pi.tce.publicacoes.modelo.elastic.PaginaOcrElastic;
import br.gov.pi.tce.publicacoes.modelo.elastic.PublicacaoElasticAggregate;
import br.gov.pi.tce.publicacoes.modelo.elastic.PublicacaoElasticGeral;
import br.gov.pi.tce.publicacoes.modelo.elastic.PublicacaoGroupBy;
import br.gov.pi.tce.publicacoes.modelo.elastic.Query;
import br.gov.pi.tce.publicacoes.modelo.elastic.Terms;
import br.gov.pi.tce.publicacoes.modelo.elastic.TermsArquivo;
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

	public PublicacaoElasticAggregate consultarComAgragador(String descricao) {
		try {
			
			Propriedades propriedades = Propriedades.getInstance();
			this.webTarget = this.client
						.target(propriedades.getValorString("URI_API_ELASTIC") + propriedades.getValorString("URI_API_ELASTIC_CONSULTA_AGGRAGATE"));
			Invocation.Builder invocationBuilder = this.webTarget.request(propriedades.getValorString("RESPONSE_TYPE"));
			BodyConsultaAggragate bodyConsultaAggragate = createBodyConsultaAggragate(descricao);
			Response response = invocationBuilder.post(Entity.entity(bodyConsultaAggragate,RESPONSE_TYPE));
			PublicacaoElasticAggregate pub = response.readEntity(PublicacaoElasticAggregate.class);
			return pub;
		} catch (Exception e) {
			LOGGER.error("Erro ao consultar as publicacoes");
			throw e;
		}
	}

	private BodyConsultaAggragate createBodyConsultaAggragate(String campo) {
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
		
		
		
		Match match = new Match();
		match.setTexto_ocr(campo);
		Must must = new Must();
		must.setMatch(match);
		Bool bool = new Bool();
		bool.setMust(must);
		Query query = new Query();
		query.setBool(bool);
		
		body.setQuery(query);
		return body;
	}

	public PaginaOcrElastic getPagina(Long idArquivo, String pagina) {
		//TODO
		PaginaOcrElastic novo = new PaginaOcrElastic();
		novo.setIdArquivo(idArquivo);
		novo.setNumPagina(Long.valueOf(pagina));
		novo.setTextoOcr("TEste de OCR");
		return novo;
	}

}
