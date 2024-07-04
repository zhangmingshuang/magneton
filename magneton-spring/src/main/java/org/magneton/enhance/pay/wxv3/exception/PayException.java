package org.magneton.enhance.pay.wxv3.exception;

/**
 * 支付异常.
 *
 * @author zhangmsh
 * @since 2024
 */
public class PayException extends RuntimeException {

	public PayException(String message, Object... args) {
		super(String.format(message, args));
	}

}
