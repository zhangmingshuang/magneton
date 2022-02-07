package org.magneton.exception;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import org.magneton.core.base.Preconditions;
import org.magneton.core.collect.Maps;

/**
 * exception processor context.
 *
 * @author zhangmsh
 * @version 1.0.0
 * @since 2020/12/25
 */
public class DefaultExceptionProcessorContext implements ExceptionProcessorContext {

	private final Map<Class<? extends Exception>, Function> handlers = Maps.newConcurrentMap();

	@Override
	public Object handle(Exception exception) throws Exception {
		Preconditions.checkNotNull(exception, "exception must be not null");
		Function handler = handlers.get(exception.getClass());
		if (Objects.isNull(handler)) {
			throw exception;
		}
		return handler.apply(exception);
	}

	@Override
	public void registerExceptionProcessor(ExceptionProcessor exceptionProcessor) {
		Preconditions.checkNotNull(exceptionProcessor, "exceptionProcessor must be not null");

		exceptionProcessor.registerExceptionProcessor(this);
	}

	@Override
	public <E extends Exception, R> void addHandler(Class<E> exception, Function<E, R> handler) {
		Preconditions.checkNotNull(exception, "throwable class must be not null");
		Preconditions.checkNotNull(handler, "handler must be not null");

		Function exist = handlers.putIfAbsent(exception, handler);
		if (exist != null) {
			throw new DuplicateProcessorException(exception + " duplicate in " + handler + " and " + exist);
		}
	}

}
