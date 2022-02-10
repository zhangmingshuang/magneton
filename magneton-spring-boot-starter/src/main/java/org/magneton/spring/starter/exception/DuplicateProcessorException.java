package org.magneton.spring.starter.exception;

/**
 * .
 *
 * @author zhangmsh
 * @version 1.0.0
 * @since 2020/12/31
 */
public class DuplicateProcessorException extends RuntimeException {

	private static final long serialVersionUID = 6203092421191431208L;

	public DuplicateProcessorException(String message) {
		super(message);
	}

}
