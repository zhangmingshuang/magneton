package org.magneton.support.adapter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * .
 *
 * @author zhangmsh 18/03/2022
 * @since 2.0.7
 */
@Component
public class WebMvcConfigurerAdapter implements WebMvcConfigurer {

	@Autowired(required = false)
	private List<MvcAdapter> mvcAdapters;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		if (this.mvcAdapters == null || this.mvcAdapters.isEmpty()) {
			return;
		}
		for (MvcAdapter mvcAdapter : this.mvcAdapters) {
			InterceptorRegistration interceptorRegistration = registry
					.addInterceptor(mvcAdapter.handlerInterceptorAdapter()).addPathPatterns(mvcAdapter.pathPatterns());
			String[] excludePathPatterns = mvcAdapter.excludePathPatterns();
			if (excludePathPatterns != null && excludePathPatterns.length > 0) {
				interceptorRegistration.excludePathPatterns(excludePathPatterns);
			}
		}
	}

}
