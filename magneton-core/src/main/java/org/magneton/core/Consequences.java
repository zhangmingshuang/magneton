package org.magneton.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.magneton.core.base.Preconditions;

/**
 * 内部使用的响应结果.
 *
 * @author zhangmsh 16/03/2022
 * @since 2.0.7
 * @see Response
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
public class Consequences<T> {

	private boolean success;

	private T data;

	private String message;

	public static <T> Consequences<T> success(T data) {
		return state(true, data, "success");
	}

	public static <T> Consequences<T> success(T data, String message) {
		return state(true, data, message);
	}

	public static <T> Consequences<T> fail(T data) {
		return state(false, data, "failure");
	}

	public static <T> Consequences<T> fail(T data, String message) {
		return state(false, data, message);
	}

	private static <T> Consequences<T> state(boolean state, T data, String message) {
		return new Consequences<>(state, Preconditions.checkNotNull(data), Preconditions.checkNotNull(message));
	}

}
