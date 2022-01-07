package org.magneton.core.exception;

/**
 * 文件写入异常
 *
 * @author zhangmsh
 * @since 2021/09/07
 */
public class WriteFileException extends RuntimeException {

	public WriteFileException(String message, Throwable cause) {
		super(message, cause);
	}

}
