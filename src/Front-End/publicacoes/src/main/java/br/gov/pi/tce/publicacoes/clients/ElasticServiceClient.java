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
import br.gov.pi.tce.publicacoes.modelo.elastic.FonteGroupBy;
import br.gov.pi.tce.publicacoes.modelo.elastic.Match;
import br.gov.pi.tce.publicacoes.modelo.elastic.Must;
import br.gov.pi.tce.publicacoes.modelo.elastic.Order;
import br.gov.pi.tce.publicacoes.modelo.elastic.PaginaGroupBy;
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
		List<Must> listaMust = new ArrayList<Must>();
		listaMust.add(must);
		bool.setMust(listaMust);
		Query query = new Query();
		query.setBool(bool);
		
		body.setQuery(query);
		return body;
	}

	public String getPagina(Long idArquivo, int pagina, String busca, boolean ultimaPagina) {
		//TODO
		String retorno = "";
		String ocr = "4\r\n" + 
				"Quarta-feira, 07 de novembro de 2018\r\n" + 
				"DOM - Teresina - Ano 2018 - nº 2.397\r\n" + 
				"CÓDIGO\r\n" + 
				"NOME COMPLETO DA\r\n" + 
				"UNIDADE DE ENSINO\r\n" + 
				"LOCALIZAÇÃO\r\n" + 
				"ZONA\r\n" + 
				"MODALIDADE\r\n" + 
				"DE ENSINO\r\n" + 
				"DATA DE\r\n" + 
				"INÍCIO\r\n" + 
				"Gabinete do Prefeito Municipal de Teresina (PI), em 1º de no-\r\n" + 
				"vembro de 2018.\r\n" + 
				"44030\r\n" + 
				"CENTRO MUNICIPAL DE\r\n" + 
				"EDUCAÇÃO INFANTIL TIA\r\n" + 
				"HELENA MEDEIROS\r\n" + 
				"SÃO JOAQUIM\r\n" + 
				"NORTE\r\n" + 
				"EDUCAÇÃO\r\n" + 
				"INFANTIL\r\n" + 
				"03/03/1986\r\n" + 
				"FIRMINO DA SILVEIRA SOARES FILHO\r\n" + 
				"Prefeito de Teresina\r\n" + 
				"Art. 2º Este Decreto entra em vigor na data de sua publicação.\r\n" + 
				"RAIMUNDO EUGÊNIO BARBOSA DOS SANTOS ROCHA\r\n" + 
				"Secretário Municipal de Governo\r\n" + 
				"Art. 3° Revogam-se as disposições em contrário.\r\n" + 
				"DECRETO Nº 18.114, DE 6 DE NOVEMBRO DE 2018\r\n" + 
				"Gabinete do Prefeito Municipal de Teresina (PI), em 1º de no-\r\n" + 
				"vembro de 2018.\r\n" + 
				"FIRMINO DA SILVEIRA SOARES FILHO\r\n" + 
				"Prefeito de Teresina\r\n" + 
				"Regulamenta a Lei Municipal n° 4.274, de 17\r\n" + 
				"de maio de 2012, com alterações posteriores,\r\n" + 
				"que “Dispõe sobre a eleição de Diretores, Vice-\r\n" + 
				"-Diretores ou Diretores-Adjuntos das Unidades\r\n" + 
				"de Ensino da Rede Pública Municipal de Ensino\r\n" + 
				"de Teresina e dá outras providências”.\r\n" + 
				"RAIMUNDO EUGÊNIO BARBOSA DOS SANTOS ROCHA\r\n" + 
				"Secretário Municipal de Governo\r\n" + 
				"DECRETO Nº 18.108, DE 1º DE NOVEMBRO DE 2018.\r\n" + 
				"O PREFEITO MUNICIPAL DE TERESINA, Estado do Piauí,\r\n" + 
				"no uso de suas atribuições legais que lhe confere o inciso XXV, do art. 71,\r\n" + 
				"da Lei Orgânica do Município; com base na Lei Municipal n° 4.274, de\r\n" + 
				"17.05.2012, no Ofício nº 6.258/2018/GAB, da Secretaria Municipal de Edu-\r\n" + 
				"cação – SEMEC, e\r\n" + 
				"O PREFEITO MUNICIPAL DE TERESINA, Estado do Piauí,\r\n" + 
				"no uso das atribuições legais que lhe confere o art. 71, XXV, da Lei Orgâni-\r\n" + 
				"ca do Município; com base na Lei Complementar nº 2.959, de 26.12.2000\r\n" + 
				"(Lei de Organização Administrativa do Poder Executivo Municipal), com\r\n" + 
				"alterações posteriores, em especial pela Lei Complementar nº 4.995, de\r\n" + 
				"07.04.2017, resolve\r\n" + 
				"CONSIDERANDO a necessidade permanente de zelar pela\r\n" + 
				"aplicação dos princípios norteadores da atividade administrativa, em espe-\r\n" + 
				"cial aos Princípios da Legalidade, Impessoalidade, Moralidade, Publicidade\r\n" + 
				"e Eficiência;\r\n" + 
				"EXONERAR\r\n" + 
				"THAÍS ROSAL LEMOS do cargo de Assistente Técnico, Sím-\r\n" + 
				"bolo Especial, da Procuradoria Geral do Município - PGM.\r\n" + 
				"CONSIDERANDO que a Constituição Federal de 1988, em\r\n" + 
				"seu art. 2016, inciso VI, estabeleceu, formalmente, uma perspectiva de ges-\r\n" + 
				"tão democrática para o ensino público;\r\n" + 
				"Gabinete do Prefeito Municipal de Teresina (PI), em 1º de no-\r\n" + 
				"vembro de 2018.\r\n" + 
				"FIRMINO DA SILVEIRA SOARES FILHO\r\n" + 
				"Prefeito de Teresina\r\n" + 
				"CONSIDERANDO que a Lei Federal nº 9.394, de 20.12.1996\r\n" + 
				"(Lei de Diretrizes e Bases da Educação - LDB), em obediência aos preceitos\r\n" + 
				"constitucionais, fixou em seu art. 3º, inciso VIII, bem como em seu art. 14,\r\n" + 
				"os princípios que nortearão os sistemas de ensino e as definições de normas\r\n" + 
				"da gestão democrática do ensino público,\r\n" + 
				"RAIMUNDO EUGÊNIO BARBOSA DOS SANTOS ROCHA\r\n" + 
				"Secretário Municipal de Governo\r\n" + 
				"DECRETA:\r\n" + 
				"DECRETO Nº 18.109, DE 1º DE NOVEMBRO DE 2018.\r\n" + 
				"Art. 1º Fica regulamentada a eleição de Diretores, Vice-Dire-\r\n" + 
				"tores ou Diretores-Adjuntos das Unidades de Ensino da Rede Pública Mu-\r\n" + 
				"nicipal de Ensino de Teresina e estabelece indicadores para a avaliação da\r\n" + 
				"execução do Contrato de Gestão.\r\n" + 
				"O PREFEITO MUNICIPAL DE TERESINA, Estado do Piauí,\r\n" + 
				"no uso das atribuições legais que lhe confere o art. 71, XXV, da Lei Orgânica\r\n" + 
				"do Município; com base na Lei Complementar nº 2.959, 26.12.2000 (Lei de\r\n" + 
				"Organização Administrativa do Poder Executivo Municipal), com alterações\r\n" + 
				"posteriores, em especial pela Lei Complementar nº 4.994, de 07.04.2017,\r\n" + 
				"resolve\r\n" + 
				"Art. 2º Os Diretores das Unidades de Ensino da Rede Pública\r\n" + 
				"Municipal de Ensino de Teresina serão nomeados, pelo Prefeito Municipal,\r\n" + 
				"para um mandato de 3 (três) anos, conforme disposto no art. 4º, inciso III, da\r\n" + 
				"Lei Municipal n° 4.274, de 17.05.2012, com modificações posteriores, após\r\n" + 
				"a realização de eleições diretas, com ampla participação da Comunidade\r\n" + 
				"Escolar, permitida uma reeleição, para um único período subsequente, na\r\n" + 
				"forma do art. 8°, § 2º, deste Decreto.\r\n" + 
				"NOMEAR\r\n" + 
				"THAÍS ROSAL LEMOS para exercer o cargo de Assessor\r\n" + 
				"Administrativo, Símbolo Especial, da Secretaria Municipal de Governo –\r\n" + 
				"SEMGOV.\r\n" + 
				"Gabinete do Prefeito Municipal de Teresina (PI), em 1º de no-\r\n" + 
				"vembro de 2018.\r\n" + 
				"§ 1º A eleição de Diretores, Vice-Diretores ou Diretores-Ad-\r\n" + 
				"juntos, nos termos estabelecidos no caput, do art. 2°, deste Decreto, ocorrerá\r\n" + 
				"simultaneamente nas Unidades de Ensino da Rede Pública Municipal de\r\n" + 
				"Ensino de Teresina, sendo a chapa composta por um candidato a Diretor e\r\n" + 
				"um candidato a Vice-Diretor ou Diretor Adjunto, conforme o caso.\r\n" + 
				"FIRMINO DA SILVEIRA SOARES FILHO\r\n" + 
				"Prefeito de Teresina\r\n" + 
				"RAIMUNDO EUGÊNIO BARBOSA DOS SANTOS ROCHA\r\n" + 
				"Secretário Municipal de Governo\r\n" + 
				"§ 2º Nas Unidades de Ensino da Rede Pública Municipal de\r\n" + 
				"Ensino de Teresina que possuírem até 6 (seis) turmas ativas, a chapa será\r\n" + 
				"composta apenas pelo candidato ao cargo de Diretor.\r\n" + 
				"DECRETO Nº 18.110, DE 1º DE NOVEMBRO DE 2018.\r\n" + 
				"§ 3º A eleição para a escolha dos Diretores, Vice-Diretores ou\r\n" + 
				"Diretores-Adjuntos das Unidades de Ensino da Rede Pública Municipal de\r\n" + 
				"Ensino de Teresina não prejudica o disposto no art. 75, II, in fine, da Lei\r\n" + 
				"Orgânica do Município.\r\n" + 
				"O PREFEITO MUNICIPAL DE TERESIN stado do\r\n" + 
				"no uso das atribuições legais que lhe confere o art. 71, XXV, da Lei Orgânica\r\n" + 
				"do Município; com base na Lei Complementar nº 2.959, 26.12.2000 (Lei de\r\n" + 
				"Organização Administrativa do Poder Executivo Municipal), com alterações\r\n" + 
				"posteriores, em especial pela Lei Complementar nº 4.994, de 07.04.2017,\r\n" + 
				"resolve\r\n" + 
				"Art. 3º A Secretaria Municipal de Educação - SEMEC promo-\r\n" + 
				"verá Curso de Gestão, nas dimensões pedagógica, administrativa e financei-\r\n" + 
				"ra, para os Diretores, Vice-Diretores ou Diretores-Adjuntos eleitos, que cor-\r\n" + 
				"rerá à custa da própria Secretaria, cuja aprovação é requisito indispensável\r\n" + 
				"para o exercício do mandato.\r\n" + 
				"EXONERAR\r\n" + 
				"SALOMÃO MAZUAD SALHA do cargo de Assessor Admi-\r\n" + 
				"nistrativo, Símbolo Especial, do Gabinete do Prefeito.\r\n" + 
				"§ 1º Considerar-se-á aprovado, para fins de alcance de desem-\r\n" + 
				"penho satisfatório no Curso de Gestão Escolar, o candidato que obtiver, no\r\n" + 
				"\r\n" + 
				"";
		
		try {
//			
//			if(ocr == null || ocr.trim().equalsIgnoreCase("")) {
//				return retorno;
//			}
//			
//			retorno = getSubstring(ocr, busca, ultimaPagina);
//			
//			Propriedades propriedades = Propriedades.getInstance();
//			this.webTarget = this.client
//						.target(propriedades.getValorString("URI_API_ELASTIC") + propriedades.getValorString("URI_API_ELASTIC_CONSULTA_TEXTO"));
//			Invocation.Builder invocationBuilder = this.webTarget.request(propriedades.getValorString("RESPONSE_TYPE"));
//			BodyConsultaTextoOcr bodyConsultaTExtoOcr = createBodyConsultaTextoOcrEspecifico(idArquivo, pagina);
//			Response response = invocationBuilder.post(Entity.entity(bodyConsultaTExtoOcr,RESPONSE_TYPE));
//			
//			//TODO criar o TexoOcrElastic
//			TexoOcrElastic ocr = response.readEntity(TexoOcrElastic.class);
////			novo.setIdArquivo(idArquivo);
////			novo.setNumPagina(Long.valueOf(pagina));
////			novo.setTextoOcr(ocr.getTextoOcr());
//			
			return getSubstring(ocr, busca, ultimaPagina);
		} catch (Exception e) {
			LOGGER.error("Erro ao consultar as publicacoes");
			throw e;
		}
	}

	private String getSubstring(String ocr, String busca, boolean ultimaPagina) {
		int tamanhoMax = 350;
		int tamOcr = ocr.length();
		int inicio = ocr.indexOf(busca);
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
	
	
	private BodyConsultaTextoOcr createBodyConsultaTextoOcrEspecifico(Long idArquivo, String pagina) {
		BodyConsultaTextoOcr body = new BodyConsultaTextoOcr();
		Match match1 = new Match();
		match1.setTexto_ocr("id_arquivo");
		Must must1 = new Must();
		must1.setMatch(match1);
		
		
		Match match2 = new Match();
		match2.setTexto_ocr("num_pagina");
		Must must2 = new Must();
		must2.setMatch(match2);
		
		Bool bool = new Bool();
		List<Must> listaMust = new ArrayList<Must>();
		listaMust.add(must1);
		listaMust.add(must2);
		
		bool.setMust(listaMust);
		
		
		Query query = new Query();
		query.setBool(bool);
		
		body.setQuery(query);
		return body;
	}

}
