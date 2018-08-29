package br.gov.pi.tce.publicacoes.controller.beans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import modelo.Usuario;

@Named
@ViewScoped
public class UsuariosController extends BeanController {

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
		usuarios = new ArrayList<>();
		usuarios.add(new Usuario("Helton Souto", "helton@siscap.com", "helton"));
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