package edu.oswego.reslife.deskapp.filters;

import javax.servlet.*;
import java.io.IOException;

public class ApiFilter implements Filter {
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

		// Here we should check if the user is logged in and has access to whatever resource he is trying to load.

		response.setContentType("application/json");
		filterChain.doFilter(request, response);
	}

	@Override
	public void destroy() {

	}
}
