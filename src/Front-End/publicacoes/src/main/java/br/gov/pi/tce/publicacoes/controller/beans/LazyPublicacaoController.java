package br.gov.pi.tce.publicacoes.controller.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.gov.pi.tce.publicacoes.controller.beans.ConsultaPublicacaoController;
import br.gov.pi.tce.publicacoes.modelo.Publicacao;

@Named
@ViewScoped
public class LazyPublicacaoController extends LazyDataModel<Publicacao> implements Serializable {

	
	
	private static final long serialVersionUID = 1L;
	
	
	public LazyPublicacaoController() {

	}
	public LazyPublicacaoController(int total) {
		this.setRowCount(total);
	}
	

	public List<Publicacao> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
		
		ConsultaPublicacaoController publicacaoController = null;
        try {
          	publicacaoController = getFacadeWithJNDI(ConsultaPublicacaoController.class);
                        
         } catch (NamingException ex) {
            }
		List<Publicacao> publicacoes = new ArrayList<Publicacao>();
		publicacoes = publicacaoController.consultarPublicacaoPorPagina(first, pageSize);
		
		//sort
        if(sortField != null) {
            Collections.sort(publicacoes, new LazySorter(sortField, sortOrder));
        }
		
		return publicacoes;
	}
	
	public <T> T getFacadeWithJNDI(Class<T> classToFind) throws NamingException {
        BeanManager bm = getBeanManager();
        Bean<T> bean = (Bean<T>) bm.getBeans(classToFind).iterator().next();
        CreationalContext<T> ctx = bm.createCreationalContext(bean);
        T dao = (T) bm.getReference(bean, classToFind, ctx);
        return dao;
    }

    private BeanManager getBeanManager() throws NamingException {
        try {
            InitialContext initialContext = new InitialContext();
            return (BeanManager) initialContext.lookup("java:comp/BeanManager");
        }
        catch (NamingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
