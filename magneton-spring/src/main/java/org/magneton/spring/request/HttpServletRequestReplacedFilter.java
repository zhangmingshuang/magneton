package org.magneton.spring.request;

import org.magneton.spring.properties.MagnetonProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
@WebFilter(urlPatterns = "/*", filterName = "requestReplaced")
@ConditionalOnProperty(prefix = MagnetonProperties.PREFIX, name = "cached-http-request-wrapper", havingValue = "true")
public class HttpServletRequestReplacedFilter implements Filter {

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		ServletRequest requestWrapper = null;
		if (servletRequest instanceof HttpServletRequest) {
			requestWrapper = new CachedHttpRequestWrapper((HttpServletRequest) servletRequest);
		}
		if (requestWrapper == null) {
			filterChain.doFilter(servletRequest, servletResponse);
		}
		else {
			filterChain.doFilter(requestWrapper, servletResponse);
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

}