package org.magneton.spring.starter.exception;

/**
 * 处理异常
 *
 * @author zhangmsh 2022/4/5
 * @since 1.0.0
 */
public class ProcessException extends RuntimeException {

	private static final long serialVersionUID = 5775793916708506273L;

	public ProcessException(Throwable cause) {
		super(cause);
	}

}
