package org.magneton.foundation.exception;

/**
 * @author zhangmsh 2022/5/18
 * @since 1.0.0
 */
public class WriteFileException extends RuntimeException {

	private static final long serialVersionUID = -2149472957686546146L;

	public WriteFileException(String message, Throwable cause) {
		super(message, cause);
	}

}