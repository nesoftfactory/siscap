package br.gov.pi.tce.siscap.timer.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AuthenticationFilter implements Filter {
	private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		logger.debug("Iniciando AuthenticationFilter >> ");
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {

		/*
		 * HttpServletRequest request = (HttpServletRequest) req; HttpServletResponse
		 * response = (HttpServletResponse) resp;
		 */
		chain.doFilter(req, resp);
	}

	@Override
	public void destroy() {
		logger.debug("Destruindo AuthenticationFilter >> ");
	}

}
