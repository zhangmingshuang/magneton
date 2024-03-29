package org.magneton.support.adapter;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ArrayUtil;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.magneton.foundation.MoreReflection;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.OrderUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * .
 *
 * @author zhangmsh 18/03/2022
 * @since 2.0.7
 * @see org.springframework.core.annotation.AnnotationAwareOrderComparator
 */
@Slf4j
@Component
public class InterceptorConfigsAdapter implements WebMvcConfigurer, ApplicationContextAware, BeanPostProcessor {

	private static final String[] EMPTY_STRINGS = new String[0];

	private static final Class[] EMPTY = new Class[0];

	private List<InterceptorAdapter> interceptorAdapters;

	private Map<String, Class<? extends InterceptorAdapter>[]> excludeInterceptorPaths = Maps.newConcurrentMap();

	private ApplicationContext applicationContext;

	public InterceptorConfigsAdapter(@Autowired(required = false) List<InterceptorAdapter> interceptorAdapters) {
		this.interceptorAdapters = interceptorAdapters;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;

		Map<String, Object> globalExcludeInterceptors = applicationContext
				.getBeansWithAnnotation(GlobalExcludeInterceptor.class);
		if (!globalExcludeInterceptors.isEmpty()) {
			globalExcludeInterceptors.forEach((name, gei) -> {
				GlobalExcludeInterceptor globalExcludeInterceptor = this.getGlobalExcludeInterceptor(gei);
				if (globalExcludeInterceptor == null) {
					throw new NullPointerException(
							String.format("GlobalExcludeInterceptor is not found on (%s) %s", name, gei));
				}
				for (String exclude : globalExcludeInterceptor.value()) {
					if (!Strings.isNullOrEmpty(exclude)) {
						this.excludeInterceptorPaths.put(exclude, EMPTY);
					}
				}
			});
		}
	}

	@Nullable
	private GlobalExcludeInterceptor getGlobalExcludeInterceptor(Object object) {
		Class<?> clazz = object.getClass();
		GlobalExcludeInterceptor globalExcludeInterceptor = AnnotatedElementUtils.getMergedAnnotation(clazz,
				GlobalExcludeInterceptor.class);
		if (globalExcludeInterceptor == null) {
			globalExcludeInterceptor = AnnotationUtils.findAnnotation(clazz, GlobalExcludeInterceptor.class);
		}
		if (globalExcludeInterceptor == null) {
			globalExcludeInterceptor = clazz.getAnnotation(GlobalExcludeInterceptor.class);
		}
		if (globalExcludeInterceptor == null) {
			globalExcludeInterceptor = clazz.getDeclaredAnnotation(GlobalExcludeInterceptor.class);
		}
		if (globalExcludeInterceptor == null) {
			globalExcludeInterceptor = AnnotationUtil.getAnnotation(clazz, GlobalExcludeInterceptor.class);
		}
		if (globalExcludeInterceptor == null && AopUtils.isAopProxy(clazz)) {
			clazz = AopUtils.getTargetClass(clazz);
			return this.getGlobalExcludeInterceptor(clazz);
		}
		return globalExcludeInterceptor;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

		RequestMapping beanRequestMapping = AnnotatedElementUtils.getMergedAnnotation(bean.getClass(),
				RequestMapping.class);
		// 仅作用于注解了RequestMapping的类
		if (beanRequestMapping != null) {
			// bean process
			ExcludeInterceptor beanExcludeInterceptor = AnnotatedElementUtils.getMergedAnnotation(bean.getClass(),
					ExcludeInterceptor.class);
			boolean next = true;
			if (beanExcludeInterceptor != null) {
				// 类级过滤
				Class<? extends InterceptorAdapter>[] activeInterceptorAdapter = beanExcludeInterceptor.value();
				String[] paths = beanRequestMapping.value();
				if (paths != null && paths.length > 0) {
					for (String path : paths) {
						String exclude = path + (path.endsWith("/") ? "**" : "/**");
						this.excludeInterceptorPaths.put(exclude, activeInterceptorAdapter);
					}
				}
			}
			// method process
			Method[] declaredMethods = MoreReflection.getDeclaredMethods(bean.getClass());
			for (Method method : declaredMethods) {
				ExcludeInterceptor methodExcludeInterceptor = AnnotatedElementUtils.getMergedAnnotation(method,
						ExcludeInterceptor.class);
				if (methodExcludeInterceptor != null) {
					Class<? extends InterceptorAdapter>[] activeInterceptorAdapter = methodExcludeInterceptor.value();
					// 方法级过滤
					String[] paths = beanRequestMapping.value();
					if (paths != null && paths.length > 0) {
						RequestMapping methodRequestMapping = AnnotatedElementUtils.getMergedAnnotation(method,
								RequestMapping.class);
						String[] methodPaths = methodRequestMapping.value();
						if (methodPaths != null && methodPaths.length > 0) {
							// paths and methodPaths combination
							for (String path : paths) {
								for (String methodPath : methodPaths) {
									String exclude = path + methodPath;
									this.excludeInterceptorPaths.put(exclude, activeInterceptorAdapter);
								}
							}
						}
					}
				}
			}
		}
		return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
	}

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
			if (!this.excludeInterceptorPaths.isEmpty()) {
				if (excludePathPatterns == null) {
					excludePathPatterns = Sets.newHashSet();
				}
				for (Map.Entry<String, Class<? extends InterceptorAdapter>[]> entry : this.excludeInterceptorPaths
						.entrySet()) {
					String exclude = entry.getKey();
					Class<? extends InterceptorAdapter>[] excludeInterceptorAdapters = entry.getValue();
					if (excludeInterceptorAdapters == null || excludeInterceptorAdapters.length < 1
							|| ArrayUtil.contains(excludeInterceptorAdapters, interceptorAdapter.getClass())) {
						excludePathPatterns.add(exclude);
					}
				}
			}
			if (excludePathPatterns != null && !excludePathPatterns.isEmpty()) {
				String[] excludes = excludePathPatterns.toArray(EMPTY_STRINGS);
				interceptorRegistration.excludePathPatterns(excludes);
				log.info("interceptor {}, path:{}, exclude:{}", handlerInterceptorAdapter.getClass(), pathPatterns,
						excludes);
			}
			else {
				log.info("interceptor {}, path:{}", handlerInterceptorAdapter.getClass(), pathPatterns);
			}

			interceptorRegistration.order(OrderUtils.getOrder(interceptorAdapter.getClass(), 0));
		}
	}

}
