package org.magneton.core;

import javax.annotation.Nullable;

import com.google.common.base.Preconditions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 回复.
 *
 * @author zhangmsh 16/03/2022
 * @since 2.0.7
 * @see Response
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
public class Reply<T> {

	private boolean success;

	private T data;

	private String message;

	public Reply coverage() {
		return this;
	}

	public static <T> Reply<T> success(T data) {
		return state(true, Preconditions.checkNotNull(data, "data"), "success");
	}

	public static <T> Reply<T> success(T data, String message) {
		return state(true, Preconditions.checkNotNull(data, "data"), message);
	}

	public static <T> Reply<T> fail() {
		return state(false, null, "failure");
	}

	public static <T> Reply<T> fail(T data) {
		return state(false, data, "failure");
	}

	public static <T> Reply<T> fail(T data, String message) {
		return state(false, data, message);
	}

	public static <T> Reply<T> failMsg(String message) {
		return state(false, null, message);
	}

	private static <T> Reply<T> state(boolean state, @Nullable T data, String message) {
		return new Reply<>(state, state ? Preconditions.checkNotNull(data, "data") : data,
				Preconditions.checkNotNull(message));
	}

}
