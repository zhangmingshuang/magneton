package org.magneton.support.adapter;

import java.util.List;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * .
 *
 * @author zhangmsh 18/03/2022
 * @since 2.0.7
 */
@Component
public class InterceptorConfigsAdapter implements WebMvcConfigurer {

	private static final String[] EMPTY_STRINGS = new String[0];

	@Autowired(required = false)
	private List<InterceptorAdapter> interceptorAdapters;

	@Override
	public void addInterceptors(@NotNull InterceptorRegistry registry) {
		if (this.interceptorAdapters == null || this.interceptorAdapters.isEmpty()) {
			return;
		}
		for (InterceptorAdapter interceptorAdapter : this.interceptorAdapters) {

			HandlerInterceptorAdapter handlerInterceptorAdapter = interceptorAdapter.handlerInterceptorAdapter();
			if (handlerInterceptorAdapter == null) {
				continue;
			}
			InterceptorRegistration interceptorRegistration = registry.addInterceptor(handlerInterceptorAdapter)
					.addPathPatterns(interceptorAdapter.pathPatterns().toArray(EMPTY_STRINGS));
			Set<String> excludePathPatterns = interceptorAdapter.excludePathPatterns();
			if (excludePathPatterns != null && !excludePathPatterns.isEmpty()) {
				interceptorRegistration.excludePathPatterns(excludePathPatterns.toArray(EMPTY_STRINGS));
			}
		}
	}

}
