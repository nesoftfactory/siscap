package br.gov.pi.tce.publicacoes.controller.beans;

import javax.ejb.Stateless;
//import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
//import javax.faces.view.ViewScoped;
import javax.inject.Inject;
//import javax.inject.Named;

import org.apache.log4j.Logger;

import br.gov.pi.tce.publicacoes.clients.SegurancaServiceClient;
import br.gov.pi.tce.publicacoes.modelo.RequisicaoToken;
import br.gov.pi.tce.publicacoes.modelo.RespostaToken;

//@Named
//@ViewScoped
@Stateless
public class SegurancaController extends BeanController {

	private static final long serialVersionUID = 1L;

	@Inject
	private SegurancaServiceClient segurancaServiceClient;

	private static final Logger LOGGER = Logger.getLogger(SegurancaController.class);

//	@PostConstruct
//	public void init() {
//
//	}

	public RespostaToken pegarToken(String client, String username, String password, String grant_type) {
		RespostaToken respostaToken = null;
		try {
			RequisicaoToken requisicaoToken = new RequisicaoToken(client, username, password, grant_type);
			respostaToken = segurancaServiceClient.pegarToken(requisicaoToken);
		} catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_INFO, "Usuário e/ou Senha inválidos.", e.getMessage());
			LOGGER.error("Usuário e/ou Senha inválidos.: " + e.getMessage());
			e.printStackTrace();
		}
		return respostaToken;
	}

}