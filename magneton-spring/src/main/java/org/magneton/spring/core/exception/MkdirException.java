package org.magneton.spring.core.exception;

/**
 * 创建目录异常.
 *
 * @author zhangmsh
 * @since 1.0.0
 */
public class MkdirException extends RuntimeException {

	private static final long serialVersionUID = -5394020960747655966L;

	public MkdirException(String message) {
		super(message);
	}

}
