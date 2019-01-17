package br.gov.pi.tce.publicacoes.autenticacao;

import java.io.IOException;
//import java.util.ArrayList;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter(servletNames = { "Faces Servlet" })
public class ControleDeAcesso implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession session = req.getSession();

		if ((session.getAttribute("ADMINLogado") != null) || (session.getAttribute("USUARIOLogado") != null)
		// || (session.getAttribute("SUPERADMINLogado") != null)
				|| (req.getRequestURI().endsWith("login2.xhtml"))
				|| (req.getRequestURI().contains("javax.faces.resource/"))) {

			//if (req.getRequestURI().contains("/admin/")) {
			if (req.getRequestURI().contains("Admin") && session.getAttribute("ADMINLogado") != null) {
				redireciona("/publicacoes/index.xhtml", response);
			}
//			if (req.getRequestURI().endsWith("login2.xhtml")) {
//				redireciona("/usuario/index.xhtml", response);
//			}

			// redireciona("/index.xhtml", response);

			chain.doFilter(request, response);
		} else {
			redireciona("/publicacoes/login2.xhtml", response);
		}
	}

	public void init(FilterConfig filterConfig) throws ServletException {

	}

	public void destroy() {

	}

	private void redireciona(String url, ServletResponse response) throws IOException {
		HttpServletResponse res = (HttpServletResponse) response;
		res.sendRedirect(url);
	}

}
