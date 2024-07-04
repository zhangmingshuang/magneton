package org.magneton.spring.core.exception;

import com.google.common.base.Strings;

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

	public ProcessException(String message, Object... args) {
		super(Strings.lenientFormat(message, args));
	}

}
