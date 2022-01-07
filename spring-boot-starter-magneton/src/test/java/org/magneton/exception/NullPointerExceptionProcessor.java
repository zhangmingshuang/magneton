package org.magneton.exception;

import org.magneton.core.Response;

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
