package org.magneton.spring.handler;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * exception processor context.
 *
 * @author zhangmsh
 * @since 2020/12/25
 */
public class DefaultExceptionProcessorContext implements ExceptionProcessorContext {

	private final Map<Class<? extends Exception>, Function> handlers = Maps.newConcurrentMap();

	@Override
	public Object handle(Exception exception) throws Exception {
		Preconditions.checkNotNull(exception, "exception must not be null");
		Function handler = this.handlers.get(exception.getClass());
		if (Objects.isNull(handler)) {
			throw exception;
		}
		return handler.apply(exception);
	}

	@Override
	public void registerExceptionProcessor(ExceptionProcessor exceptionProcessor) {
		Preconditions.checkNotNull(exceptionProcessor, "exceptionProcessor must not be null");

		exceptionProcessor.registerExceptionProcessor(this);
	}

	@Override
	public <E extends Exception, R> void addHandler(Class<E> exception, Function<E, R> handler) {
		Preconditions.checkNotNull(exception, "throwable class must not be null");
		Preconditions.checkNotNull(handler, "handler must not be null");

		Function exist = this.handlers.putIfAbsent(exception, handler);
		if (exist != null) {
			throw new DuplicateProcessorException(exception + " duplicate in " + handler + " and " + exist);
		}
	}

}