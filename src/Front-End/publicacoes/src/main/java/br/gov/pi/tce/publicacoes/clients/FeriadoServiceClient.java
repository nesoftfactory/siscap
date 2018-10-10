package br.gov.pi.tce.publicacoes.clients;

import java.io.Console;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import br.gov.pi.tce.publicacoes.modelo.Feriado;
import br.gov.pi.tce.publicacoes.modelo.Fonte;

@Local
@Stateless(name="FeriadoServiceClient")
public class FeriadoServiceClient{
	
	private static final String RESPONSE_TYPE = "application/json;charset=UTF-8";
	private final String URI_FERIADOS = "http://localhost:7788/feriados/";

	private Client client;
	private WebTarget webTarget;
	
	public FeriadoServiceClient(){
		this.client = ClientBuilder.newClient();  
	}
	
	private Invocation.Builder chamadaAPI(Long id) {
		if (id == null) {
			this.webTarget = this.client.target(URI_FERIADOS);
		} else {
			this.webTarget = this.client.target(URI_FERIADOS).path(String.valueOf(id));
		}
		return this.webTarget.request(RESPONSE_TYPE);
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
	
	public List<Fonte> consultarTodasFontes(){
		FonteServiceClient fonteServiceClient = new FonteServiceClient();
		return fonteServiceClient.consultarTodasFontes();
	}
	
	public List<Feriado> consultarTodosFeriados() throws Exception{
		try {
			Response response = chamadaAPI(null).get();
			List<Feriado> list = response.readEntity(new GenericType<List<Feriado>>() {});
			
			// Tratamento para data
			for (Feriado feriado : list) {
				String[] dataSplit = feriado.getData().split("-");
				feriado.setData(dataSplit[2]+"/"+dataSplit[1]+"/"+dataSplit[0]);
			}
			
			return list;
		}
		catch (Exception e) {
			throw e;
		}
	}
	
	public Feriado cadastrarFeriado(Feriado feriado) throws Exception{
		Response response = chamadaAPI(null).post(Entity.entity(feriado, RESPONSE_TYPE));
		trataRetorno(response);
		return response.readEntity(Feriado.class);
	}

	public Feriado alterarFeriado(Feriado feriado) throws Exception{
		Response response = chamadaAPI(feriado.getId()).put(Entity.entity(feriado, RESPONSE_TYPE));
		trataRetorno(response);
		return response.readEntity(Feriado.class);
	}

	public Feriado consultarFeriadoPorCodigo(Long id){
		Response response = chamadaAPI(id).get();
		if(response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
			return null;
		} else {
			return response.readEntity(Feriado.class);
		}
	}

	public String excluirFeriadoPorCodigo(Long id){
		Response response = chamadaAPI(id).delete();
		if(response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
			return null;
		} else {
			return response.readEntity(String.class);
		}
	}

}
