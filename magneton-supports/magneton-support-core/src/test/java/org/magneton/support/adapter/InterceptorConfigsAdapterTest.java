package org.magneton.support.adapter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.Sets;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.handler.MappedInterceptor;

class InterceptorConfigsAdapterTest {

	@Test
	void addInterceptors() {
		TestInterceptorRegistry registry = new TestInterceptorRegistry();
		List<InterceptorAdapter> interceptorAdapters = new ArrayList<>();
		interceptorAdapters.add(new Order2InterceptorAdapter());
		interceptorAdapters.add(new Order1InterceptorAdapter());

		InterceptorConfigsAdapter configs = new InterceptorConfigsAdapter(interceptorAdapters);
		configs.addInterceptors(registry);

		List<Object> interceptors = registry.getInterceptors();
		for (int i = 0; i < interceptors.size(); i++) {
			Object interceptor = interceptors.get(i);
			HandlerInterceptor hi = ((MappedInterceptor) interceptor).getInterceptor();
			Assertions.assertTrue(hi.getClass().getName().contains(String.format("Order%sInterceptorAdapter", i + 1)));
		}

	}

	@Order(1)
	static class Order1InterceptorAdapter extends Abs {

		@Nullable
		@Override
		public HandlerInterceptorAdapter handlerInterceptorAdapter() {
			return new HandlerInterceptorAdapter() {

			};
		}

	}

	@Order(2)
	static class Order2InterceptorAdapter extends Abs {

		@Nullable
		@Override
		public HandlerInterceptorAdapter handlerInterceptorAdapter() {
			return new HandlerInterceptorAdapter() {

			};
		}

	}

	static abstract class Abs implements InterceptorAdapter {

		@Override
		public Set<String> pathPatterns() {
			return Sets.newHashSet("/**");
		}

	}

	static class TestInterceptorRegistry extends InterceptorRegistry {

		@SneakyThrows
		public List<InterceptorRegistration> getInterceptorRegistrations() {
			Field field = InterceptorRegistry.class.getDeclaredField("registrations");
			field.setAccessible(true);
			return (List<InterceptorRegistration>) field.get(this);
		}

		@Override
		protected List<Object> getInterceptors() {
			return super.getInterceptors();
		}

	}

}