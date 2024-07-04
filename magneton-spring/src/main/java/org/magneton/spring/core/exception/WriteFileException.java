package org.magneton.spring.core.exception;

/**
 * 写入文件异常
 *
 * @author zhangmsh
 * @since 1.0.0
 */
public class WriteFileException extends RuntimeException {

	private static final long serialVersionUID = -2149472957686546146L;

	public WriteFileException(String message) {
		super(message);
	}

	public WriteFileException(String message, Throwable cause) {
		super(message, cause);
	}

}
