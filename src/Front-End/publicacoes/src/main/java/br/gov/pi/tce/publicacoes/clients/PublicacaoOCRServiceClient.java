package br.gov.pi.tce.publicacoes.clients;

import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import br.gov.pi.tce.publicacoes.autenticacao.AutenticadorToken;
import br.gov.pi.tce.publicacoes.modelo.Publicacao;
import br.gov.pi.tce.publicacoes.modelo.PublicacaoAnexo;
import br.gov.pi.tce.publicacoes.util.Propriedades;

@Local
@Stateless(name="PublicacaoOCRServiceClient")
public class PublicacaoOCRServiceClient{
	
	
	private static final String ANEXOS_APTOS = "anexos_aptos";

	private static final String PUBLICACOES_APTAS = "publicacoes_aptas";

	private static final Logger LOGGER = Logger.getLogger(PublicacaoOCRServiceClient.class);

	private Client client;
	private WebTarget webTarget;
	
	private static final String RESPONSE_TYPE = "application/json;charset=UTF-8";
	
	public PublicacaoOCRServiceClient(){
		this.client = ClientBuilder.newClient().register(new AutenticadorToken());  
	}
	
	public PublicacaoOCRServiceClient(String token){
		this.client = ClientBuilder.newClient().register(new AutenticadorToken(token));  
	}

	public List<Publicacao> consultarTodasPublicacoesAptasParaOCR(Long fonte) {
		try {
			Propriedades propriedades = Propriedades.getInstance();
			this.webTarget = this.client.target(propriedades.getValorString("URI_API") + propriedades.getValorString("URI_OCR_PUBLICACOES")).path(String.valueOf(fonte)+ "/" + PUBLICACOES_APTAS);
			Invocation.Builder invocationBuilder =  this.webTarget.request(propriedades.getValorString("RESPONSE_TYPE"));
			Response response = invocationBuilder.get();
			List<Publicacao> list = response.readEntity(new GenericType<List<Publicacao>>() {});
			return list;
		}
		catch (Exception e) {
			throw e;
		}
	}

	
	public List<PublicacaoAnexo> consultarTodosAnexosPublicacoesAptosParaOCR(Long fonte) {
		try {
			Propriedades propriedades = Propriedades.getInstance();
			this.webTarget = this.client.target(propriedades.getValorString("URI_API") + propriedades.getValorString("URI_OCR_PUBLICACOES") + String.valueOf(fonte)+ "/" + ANEXOS_APTOS);
			Invocation.Builder invocationBuilder =  this.webTarget.request(propriedades.getValorString("RESPONSE_TYPE"));
			Response response = invocationBuilder.get();
			List<PublicacaoAnexo> list = response.readEntity(new GenericType<List<PublicacaoAnexo>>() {});
			return list;
		}
		catch (Exception e) {
			throw e;
		}
	}

	public PublicacaoAnexo realizarOCRAnexo(PublicacaoAnexo anexo) throws Exception{
		Propriedades propriedades = Propriedades.getInstance();
		this.webTarget = this.client.target(propriedades.getValorString("URI_API") + propriedades.getValorString("URI_OCR_PUBLICACOES")).path(String.valueOf(anexo.getId()) + "/anexo");
		Response response = this.webTarget.request(RESPONSE_TYPE).get();
		trataRetorno(response);
		return response.readEntity(PublicacaoAnexo.class);
	}
	
	
	public Publicacao realizarOCRPublicacao(Publicacao publicacao) throws Exception {
		Propriedades propriedades = Propriedades.getInstance();
		this.webTarget = this.client.target(propriedades.getValorString("URI_API") + propriedades.getValorString("URI_OCR_PUBLICACOES")).path(String.valueOf(publicacao.getId()) + "/publicacao");
		Response response = this.webTarget.request(RESPONSE_TYPE).get();
		trataRetorno(response);
		return response.readEntity(Publicacao.class);
	}
	
	
	
	private void trataRetorno(Response response) throws Exception {
		if(response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
			List erros = response.readEntity(List.class);
			if(erros != null && !erros.isEmpty()) {
				Map p;
				String msg = (String)((Map)erros.get(0)).get("mensagemUsuario");
				throw new Exception(msg);
			}
			else {
				throw new Exception("Erro interno.");
			}
		}
	}

	
	
	
	
	
}
