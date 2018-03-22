package edu.oswego.reslife.deskapp.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {
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

		// Here we check if the user is logged in.
		HttpSession session = request.getSession(true);

		if (session.getAttribute("user") == null) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			response.sendRedirect("/staff/login");
		} else {
			response.setContentType("application/json");
			filterChain.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {

	}
}
