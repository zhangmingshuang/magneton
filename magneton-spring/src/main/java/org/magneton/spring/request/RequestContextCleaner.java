package org.magneton.spring.request;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.ServletRequestHandledEvent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet Request Finally Processor.
 *
 * @apiNote Do something after the request is processed. This is a listener for
 * {@link ServletRequestHandledEvent} after doService.
 * @author zhangmsh
 * @since 2023.1
 * @see org.springframework.web.servlet.FrameworkServlet#processRequest(HttpServletRequest,
 * HttpServletResponse)
 * @see org.springframework.web.servlet.DispatcherServlet#doService(HttpServletRequest,
 * HttpServletResponse)
 */
@Component
public class RequestContextCleaner implements ApplicationListener<ServletRequestHandledEvent> {

	@Override
	public void onApplicationEvent(ServletRequestHandledEvent servletRequestHandledEvent) {
		RequestContext.clean();
	}

}
