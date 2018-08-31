package br.gov.pi.tce.publicacoes.controller.beans;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.pi.tce.publicacoes.clients.UsuarioServiceClient;
import modelo.Usuario;

@Named
@ViewScoped
public class UsuariosController extends BeanController {
	
	@Inject
	private UsuarioServiceClient usuarioServiceClient;

	private static final long serialVersionUID = 1L;

	private Usuario usuario;
	private List<Usuario> usuarios;
	
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
		usuarios.remove(usuarioExcluir);
	}
     
    
	private void iniciaUsuarios() {
		try {
			usuarios = usuarioServiceClient.ConsultarTodos();
		}
		catch (Exception e) {
			//TODO testar cadastramento de mensagens
			registrarMensagem(FacesMessage.SEVERITY_ERROR, "label.erro", null);
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

	
}