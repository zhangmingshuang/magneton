package org.magneton.support.adapter;

import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class InterceptorConfigsAdapter implements WebMvcConfigurer {

	private static final String[] EMPTY_STRINGS = new String[0];

	@Autowired(required = false)
	private List<InterceptorAdapter> interceptorAdapters;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		if (this.interceptorAdapters == null || this.interceptorAdapters.isEmpty()) {
			return;
		}
		for (InterceptorAdapter interceptorAdapter : this.interceptorAdapters) {

			HandlerInterceptorAdapter handlerInterceptorAdapter = interceptorAdapter.handlerInterceptorAdapter();
			if (handlerInterceptorAdapter == null) {
				continue;
			}
			InterceptorRegistration interceptorRegistration = registry.addInterceptor(handlerInterceptorAdapter);

			String[] pathPatterns = interceptorAdapter.pathPatterns().toArray(EMPTY_STRINGS);
			interceptorRegistration.addPathPatterns(pathPatterns);
			Set<String> excludePathPatterns = interceptorAdapter.excludePathPatterns();
			if (excludePathPatterns != null && !excludePathPatterns.isEmpty()) {
				String[] excludes = excludePathPatterns.toArray(EMPTY_STRINGS);
				interceptorRegistration.excludePathPatterns(excludes);
				log.info("interceptor {}, path:{}, exclude:{}", handlerInterceptorAdapter.getClass(), pathPatterns,
						excludes);
			}
			else {
				log.info("interceptor {}, path:{}", handlerInterceptorAdapter.getClass(), pathPatterns);
			}
		}
	}

}
