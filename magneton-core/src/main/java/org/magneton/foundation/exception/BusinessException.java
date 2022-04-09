package org.magneton.foundation.exception;

/**
 * @author zhangmsh 2022/4/8
 * @since 1.0.0
 */
public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = -8684924386944261539L;

	public BusinessException() {
		super();
	}

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(String message, Throwable cause) {
		super(message, cause);
	}

	public BusinessException(Throwable cause) {
		super(cause);
	}

}
