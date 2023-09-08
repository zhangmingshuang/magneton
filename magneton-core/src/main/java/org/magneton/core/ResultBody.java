package org.magneton.core;

import com.google.common.base.Preconditions;

import javax.annotation.Nullable;

/**
 * .
 *
 * @author zhangmsh
 * @version 1.0.0
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
		Preconditions.checkNotNull(code, "code must be not null");
		Preconditions.checkNotNull(message, "message must be not null");

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

}
