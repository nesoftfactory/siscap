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

//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;

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

//			if (req.getRequestURI().contains("/admin/")) {
//				redireciona("/usuario/index.xhtml", response);
//			}
//			if (req.getRequestURI().endsWith("login2.xhtml")) {
//				redireciona("/usuario/index.xhtml", response);
//			}

			// redireciona("/index.xhtml", response);

//			HttpServletRequest httpRequest = (HttpServletRequest) request;
//			String header = httpRequest.getHeader(SecurityConstants.HEADER_STRING);
//			if (header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
//				chain.doFilter(httpRequest, response);
//				return;
//			}
//
//			UsernamePasswordAuthenticationToken authentication = getToken(httpRequest);
//			SecurityContextHolder.getContext().setAuthentication(authentication);

			chain.doFilter(request, response);
		} else {
			redireciona("/publicacoes/login2.xhtml", response);
		}
	}

//	@SuppressWarnings("unchecked")
//	private UsernamePasswordAuthenticationToken getToken(HttpServletRequest request) {
//
//		String token = request.getHeader(SecurityConstants.HEADER_STRING);
//
//		System.out.println("-----------------------------------------------------");
//		System.out.println("Token: " + token);
//		System.out.println("-----------------------------------------------------");
//
//		if (token != null) {
//
//			Claims claims = Jwts.parser().setSigningKey(SecurityConstants.SECRET.getBytes())
//					.parseClaimsJws(token.replace(SecurityConstants.TOKEN_PREFIX, "")).getBody();
//			String user = claims.getSubject();
//
//			ArrayList<String> roles = (ArrayList<String>) claims.get("roles");
//
//			ArrayList<MyGrantedAuthority> rolesList = new ArrayList<>();
//
//			if (roles != null) {
//				for (String role : roles) {
//					rolesList.add(new MyGrantedAuthority(role));
//				}
//			}
//			if (user != null) {
//				return new UsernamePasswordAuthenticationToken(user, null, null);
//			}
//			return null;
//		}
//		return null;
//	}

//	private String resolveToken(HttpServletRequest request) {
//		String bearerToken = request.getHeader(HEADER_STRING);
//		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
//			return bearerToken.substring(7, bearerToken.length());
//		}
//		return null;
//	}

	public void init(FilterConfig filterConfig) throws ServletException {

	}

	public void destroy() {

	}

	private void redireciona(String url, ServletResponse response) throws IOException {
		HttpServletResponse res = (HttpServletResponse) response;
		res.sendRedirect(url);
	}

}
