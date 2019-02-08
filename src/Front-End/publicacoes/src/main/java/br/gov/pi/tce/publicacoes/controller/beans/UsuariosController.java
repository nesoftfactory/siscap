package br.gov.pi.tce.publicacoes.controller.beans;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;

import br.gov.pi.tce.publicacoes.clients.UsuarioServiceClient;
import br.gov.pi.tce.publicacoes.modelo.Usuario;
import br.gov.pi.tce.publicacoes.services.ColetorPublicacoesService;
import br.gov.pi.tce.publicacoes.services.OCRPublicacoesService;

@Named
@ViewScoped
public class UsuariosController extends BeanController {
	
	@Inject
	private UsuarioServiceClient usuarioServiceClient;
	
	@Inject
	private ColetorPublicacoesService coletorPublicacoesService;
	
	@Inject
	private OCRPublicacoesService ocrPublicacoesService;


	private static final long serialVersionUID = 1L;

	private Usuario usuario;
	private List<Usuario> usuarios;
	
	private static final Logger LOGGER = Logger.getLogger(UsuariosController.class);
	
	@PostConstruct
	public void init() {
		limpar();
		iniciaUsuarios();
		
	}
	
	public void limpar() {
		usuario = new Usuario();
	}

	public void editar(Usuario usuarioEditar) {
		usuario = usuarioEditar;
	}
	
	public void excluir(Usuario usuarioExcluir) {
		try {
			if(usuarioExcluir == null) {
				addMessage(FacesMessage.SEVERITY_ERROR, "Usuário não selecionado.", "");
			}
			else {
				if(usuarioExcluir.getId() > 0) {
					usuarioServiceClient.excluirUsuarioPorCodigo(usuarioExcluir.getId());
				}	
				addMessage(FacesMessage.SEVERITY_INFO, "Usuário excluído com sucesso.", "");
				iniciaUsuarios();
			}
			limpar();
		}
		catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao excluir usuário.", "");
			LOGGER.error("Erro ao excluir usuário.:" + e.getMessage());
			e.printStackTrace();
		}
	}
     
    
	private void iniciaUsuarios() {
		try {
			usuarios = usuarioServiceClient.consultarTodos();
		}
		catch (EJBException e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "Serviço indisponível: Usuários.", e.getMessage());
			LOGGER.error("Erro ao iniciar usuários.:" + e.getMessage());
			e.printStackTrace();
		}
		catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao iniciar usuários.", e.getMessage());
			LOGGER.error("Erro ao iniciar usuários.:" + e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	public void salvar() {
		try {
			if(usuario == null) {
				addMessage(FacesMessage.SEVERITY_ERROR, "Usuário não preenchido.", "");
			}
			else {
				if(usuario.getId() == null || usuario.getId() == 0) {
					usuarioServiceClient.cadastrarUsuario(usuario);
					addMessage(FacesMessage.SEVERITY_INFO, "Usuário criado com sucesso.", "");
				}	
				else {
					usuarioServiceClient.alterarUsuario(usuario);
					addMessage(FacesMessage.SEVERITY_INFO, "Usuário atualizado com sucesso.", "");
				}
				iniciaUsuarios();
			}
			limpar();
			
		}
		catch (EJBException e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "Serviço indisponível: Usuários.", e.getMessage());
			LOGGER.error("Erro ao salvar usuário.:" + e.getMessage());
			e.printStackTrace();
		}
		catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR,  "Erro ao salvar usuário." + e.getMessage(), e.getMessage());
			LOGGER.error("Erro ao salvar usuário:" + e.getMessage());
			e.printStackTrace();
		}
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}
	
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public void testapublicacaoTeresina() {
		try {
			coletorPublicacoesService.coletarDiarioOficialTeresina();
		}
		catch (EJBException e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "teste", e.getMessage());
			LOGGER.error("teste:" + e.getMessage());
			e.printStackTrace();
		}
		catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "teste", e.getMessage());
			LOGGER.error("teste" + e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	public void testapublicacaoParnaiba() {
		try {
			coletorPublicacoesService.coletarDiarioOficialParnaiba();
		}
		catch (EJBException e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "teste", e.getMessage());
			LOGGER.error("teste:" + e.getMessage());
			e.printStackTrace();
		}
		catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "teste", e.getMessage());
			LOGGER.error("teste" + e.getMessage());
			e.printStackTrace();
		}
		
	}
	public void testapublicacaoEstado() {
		try {
			coletorPublicacoesService.coletarDiarioOficialPiaui();
		}
		catch (EJBException e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "teste", e.getMessage());
			LOGGER.error("teste:" + e.getMessage());
			e.printStackTrace();
		}
		catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "teste", e.getMessage());
			LOGGER.error("teste" + e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	public void testapublicacaoMunicipios() {
		try {
			coletorPublicacoesService.coletarDiarioOficialMunicipios();
		}
		catch (EJBException e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "teste", e.getMessage());
			LOGGER.error("teste:" + e.getMessage());
			e.printStackTrace();
		}
		catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "teste", e.getMessage());
			LOGGER.error("teste" + e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	public void testOCRmunicipios() {
		try {
			ocrPublicacoesService.realizarOCRDiarioOficialMunicipios();
					}
		catch (EJBException e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "teste", e.getMessage());
			LOGGER.error("teste:" + e.getMessage());
			e.printStackTrace();
		}
		catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "teste", e.getMessage());
			LOGGER.error("teste" + e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	public void testOCRparnaiba() {
		try {
			
			ocrPublicacoesService.realizarOCRDiarioOficialParnaiba();
			
		}
		catch (EJBException e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "teste", e.getMessage());
			LOGGER.error("teste:" + e.getMessage());
			e.printStackTrace();
		}
		catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "teste", e.getMessage());
			LOGGER.error("teste" + e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	public void testOCRpiaui() {
		try {
			
			ocrPublicacoesService.realizarOCRDiarioOficialPiaui();
			
		}
		catch (EJBException e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "teste", e.getMessage());
			LOGGER.error("teste:" + e.getMessage());
			e.printStackTrace();
		}
		catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "teste", e.getMessage());
			LOGGER.error("teste" + e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	public void testOCRteresina() {
		try {
	
			ocrPublicacoesService.realizarOCRDiarioOficialTeresina();
		}
		catch (EJBException e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "teste", e.getMessage());
			LOGGER.error("teste:" + e.getMessage());
			e.printStackTrace();
		}
		catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "teste", e.getMessage());
			LOGGER.error("teste" + e.getMessage());
			e.printStackTrace();
		}
		
	}

	
}