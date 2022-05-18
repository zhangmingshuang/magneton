package org.magneton.foundation.exception;

import cn.hutool.core.exceptions.ExceptionUtil;
import org.magneton.core.base.Strings;

/**
 * 工具类异常
 *
 * @author xiaoleilu
 */
@SuppressWarnings("ClassWithTooManyConstructors")
public class UtilException extends RuntimeException {

	private static final long serialVersionUID = 8247610319171014183L;

	public UtilException(Throwable e) {
		super(ExceptionUtil.getMessage(e), e);
	}

	public UtilException(String message) {
		super(message);
	}

	public UtilException(String messageTemplate, Object... params) {
		super(Strings.format(messageTemplate, params));
	}

	public UtilException(String message, Throwable cause) {
		super(message, cause);
	}

	public UtilException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public UtilException(Throwable throwable, String messageTemplate, Object... params) {
		super(Strings.format(messageTemplate, params), throwable);
	}

}
