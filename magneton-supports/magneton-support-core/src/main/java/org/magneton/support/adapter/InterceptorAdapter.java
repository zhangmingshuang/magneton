package org.magneton.support.adapter;

import com.google.common.collect.Sets;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Nullable;
import java.util.Set;

/**
 * 拦截适配器.
 *
 * 支持使用 {@link org.springframework.core.annotation.Order} 或者
 * {@link javax.annotation.Priority} 注解来进行优先级排序。
 *
 * 优先级根据数值从从小到大，值越小越优化。
 *
 * @author zhangmsh 18/03/2022
 * @since 2.0.7
 * @see org.springframework.core.annotation.Order
 * @see org.springframework.core.annotation.AnnotationAwareOrderComparator
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

	/**
	 * 合并地址列表
	 * @param append 追加的地址列表
	 * @param init 初始地址列表
	 * @return 合并后的地址列表
	 */
	default Set<String> merge(@Nullable Set<String> append, String... init) {
		Set<String> values = Sets.newHashSet(init);
		if (append != null) {
			values.addAll(append);
		}
		return values;
	}

}