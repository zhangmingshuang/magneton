package org.magneton.support.adapter;

import com.google.common.collect.Sets;
import java.util.Set;
import javax.annotation.Nullable;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * .
 *
 * @author zhangmsh 18/03/2022
 * @since 2.0.7
 */
public interface InterceptorAdapter {

	/**
	 * 获取拦截器
	 * @return 拦截器，如果返回 {@code null}表示该拦截器不需要装载。此时
	 * {@link #pathPatterns()}及{@link #excludePathPatterns()}不起作用。
	 */
	@Nullable
	HandlerInterceptorAdapter handlerInterceptorAdapter();

	/**
	 * 拦截器要拦截的地址列表，支持表达式
	 * @return 地址列表
	 */
	Set<String> pathPatterns();

	/**
	 * 拦截器要拦截的忽略地址列表，支持表达式
	 * @return 忽略地址列表
	 */
	@Nullable
	default Set<String> excludePathPatterns() {
		return null;
	}

	default Set<String> merge(@Nullable Set<String> append, String... init) {
		Set<String> values = Sets.newHashSet(init);
		if (append != null) {
			values.addAll(append);
		}
		return values;
	}

}
