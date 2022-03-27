package org.magneton.spring.starter.exception;

/**
 * @author zhangmsh 2022/3/26
 * @since 1.0.0
 */
public class ResponseException extends RuntimeException {

	private static final long serialVersionUID = -4410956062973785812L;

	public ResponseException(Throwable cause) {
		super(cause);
	}

}
