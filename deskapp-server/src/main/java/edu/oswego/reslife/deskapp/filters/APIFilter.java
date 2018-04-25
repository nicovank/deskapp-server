package edu.oswego.reslife.deskapp.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class APIFilter implements Filter {
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		try {
			doFilter((HttpServletRequest) request, (HttpServletResponse) response, filterChain);
		} catch (ClassCastException e) {
			throw new ServletException(e);
		}
	}

	private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		String headerToken = request.getHeader("Token");
		String sessionToken = (String) request.getSession().getAttribute("token");

		if (headerToken == null || !headerToken.equals(sessionToken)) {
			response.sendRedirect("/login/");
		} else {
			response.setContentType("application/json");
			filterChain.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {

	}
}
