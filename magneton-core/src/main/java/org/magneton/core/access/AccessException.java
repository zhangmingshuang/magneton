package org.magneton.core.access;

/**
 * .
 *
 * @author zhangmsh 2021/2/25
 * @since 4.0.0
 */
public class AccessException extends RuntimeException {

	private static final long serialVersionUID = 5491169936860052502L;

	public AccessException(String message, Throwable cause) {
		super(message, cause);
	}

}
