package org.magneton.framework.core;

import com.google.common.base.Preconditions;

import javax.annotation.Nullable;

/**
 * .
 *
 * @author zhangmsh
 * @since 2020/10/19
 */
public interface ResultBody<T> {

	/**
	 * simply to create a {@link ResultBody}.
	 * @param code response code.
	 * @param message response message.
	 * @param <T> T
	 * @return response body.
	 */
	static <T> ResultBody<T> valueOf(String code, String message) {
		return valueOf(code, message, null);
	}

	/**
	 * simply to create a {@link ResultBody}.
	 * @param code response code.
	 * @param message response message.
	 * @param data response data.
	 * @param <T> T
	 * @return response body.
	 */
	static <T> ResultBody<T> valueOf(String code, String message, @Nullable T data) {
		Preconditions.checkNotNull(code, "code must not be null");
		Preconditions.checkNotNull(message, "message must not be null");

		return new ResultBody<T>() {
			@Override
			public String code() {
				return code;
			}

			@Override
			public String message() {
				return message;
			}

			@Override
			public T data() {
				return data;
			}
		};
	}

	/**
	 * response code.
	 * @return code.
	 */
	String code();

	/**
	 * response message.
	 * @return message.
	 */
	String message();

	/**
	 * response data.
	 * @return data.
	 */
	default T data() {
		return null;
	}

	/**
	 * format message.
	 * @param args args
	 * @return response body.
	 */
	default ResultBody<T> format(Object... args) {
		return valueOf(this.code(), String.format(this.message(), args), this.data());
	}

}