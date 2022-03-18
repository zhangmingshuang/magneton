package org.magneton.support.adapter;

import javax.annotation.Nullable;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * .
 *
 * @author zhangmsh 18/03/2022
 * @since 2.0.7
 */
public interface MvcAdapter {

	HandlerInterceptorAdapter handlerInterceptorAdapter();

	String[] pathPatterns();

	@Nullable
	default String[] excludePathPatterns() {
		return null;
	}

}
