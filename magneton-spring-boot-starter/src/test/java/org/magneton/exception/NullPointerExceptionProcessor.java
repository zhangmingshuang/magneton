package org.magneton.exception;

import org.magneton.core.Response;
import org.magneton.spring.starter.exception.ExceptionProcessor;
import org.magneton.spring.starter.exception.ExceptionProcessorRegister;

/**
 * expcetion impl.
 *
 * @author zhangmsh
 */
class NullPointerExceptionProcessor implements ExceptionProcessor {

	public static final String NULL_ERROR = "这是空指针";

	@Override
	public void registerExceptionProcessor(ExceptionProcessorRegister register) {
		register.addHandler(NullPointerException.class, e -> Response.exception().message(NULL_ERROR));
	}

}
