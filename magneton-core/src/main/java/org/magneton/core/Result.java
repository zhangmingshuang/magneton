package org.magneton.core;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import java.beans.Transient;
import java.util.Map;
import java.util.Objects;

/**
 * 统一响应对象.
 *
 * @author zhangmsh
 * @since 1.0.0
 */
@Getter
@Setter(AccessLevel.PROTECTED)
@ToString
@SuppressWarnings("unchecked")
public class Result<T> {

	private boolean success;

	/**
	 * 响应码
	 */
	private String code;

	/**
	 * 响应数据
	 */
	@Nullable
	private T data;

	/**
	 * 响应消息
	 */
	private String message;

	/**
	 * 响应时间
	 */
	private long timestamp;

	/**
	 * 附加信息
	 */
	private Map<String, String> additions;

	private Result() {
		// private
	}

	public static <E> Result<E> success() {
		return successWith(null);
	}

	public static <E> Result<E> successWith(@Nullable E t) {
		return valueOf(ResultCodesSupplier.getInstance().ok(), t);
	}

	public static <E> Result<E> successWith(@Nullable E data, String message, @Nullable Object... args) {
		return successWith(data).message(message);
	}

	public static <E> Result<E> okBy(String message, @Nullable Object... args) {
		return (Result<E>) success().message(message, args);
	}

	public static <E> Result<E> fail() {
		return failWith(null);
	}

	public static <E> Result<E> failWith(E data) {
		return valueOf(ResultCodesSupplier.getInstance().bad(), data);
	}

	public static <E> Result<E> failWith(E data, String message, @Nullable Object... args) {
		return failWith(data).message(message, args);
	}

	public static <E> Result<E> failBy(String message, @Nullable Object... args) {
		return (Result<E>) fail().message(message, args);
	}

	public static <E> Result<E> exception() {
		return valueOf(ResultCodesSupplier.getInstance().exception(), null);
	}

	public static <E> Result<E> exceptionBy(String message, @Nullable Object... args) {
		return (Result<E>) exception().message(message, args);
	}

	/**
	 * response with body.
	 * @param resultBody {@code ResultBody}
	 * @param <E> E
	 * @return {@code Result} of response.
	 */
	public static <E> Result<E> valueOf(ResultBody<E> resultBody) {
		Preconditions.checkNotNull(resultBody, "resultBody must be not null");

		E data = resultBody.data();
		return valueOf(resultBody, data);
	}

	private static <E> Result<E> valueOf(ResultBody<E> resultBody, E data) {
		Result<E> result = new Result<>();
		String message = resultBody.message();
		return result.code(resultBody.code()).message(message).data(data).timestamp(System.currentTimeMillis());
	}

	public Result<T> code(String code) {
		Preconditions.checkNotNull(code, "code must be not null");

		this.code = code;
		this.success = this.isOkCode(code);
		return this;
	}

	public Result<T> message(String message) {
		Preconditions.checkNotNull(message, "message must be not null");
		this.message = message;
		return this;
	}

	public Result<T> message(String message, @Nullable Object... args) {
		Preconditions.checkNotNull(message, "message must be not null");
		if (args == null || args.length < 1) {
			this.message = message;
		}
		else {
			this.message = String.format(message, args);
		}
		return this;
	}

	public Result<T> data(@Nullable T data) {
		this.data = data;
		return this;
	}

	public Result<T> timestamp(long timestamp) {
		this.timestamp = timestamp;
		return this;
	}

	/**
	 * add a message entry.
	 * @param key addition message key.
	 * @param value addition message value.
	 * @return {@code Result} of response.
	 */
	public Result<T> addition(String key, String value) {
		if (Objects.isNull(this.additions)) {
			this.additions = Maps.newHashMapWithExpectedSize(2);
		}
		this.additions.put(key, value);
		return this;
	}

	/**
	 * add a message entry if absent.
	 * @param key addition message key.
	 * @param value addition message value.
	 * @return {@code Result} of response.
	 */
	public Result<T> additionIfAbsent(String key, String value) {
		if (Objects.isNull(this.additions)) {
			this.additions = Maps.newHashMapWithExpectedSize(2);
		}
		this.additions.putIfAbsent(key, value);
		return this;
	}

	/**
	 * add a message entry.
	 * @param additions addition message.
	 * @return {@code Result} of response.
	 */
	public Result<T> addition(Map<String, String> additions) {
		if (Objects.isNull(this.additions)) {
			this.additions = Maps.newHashMapWithExpectedSize(2);
		}
		this.additions.putAll(additions);
		return this;
	}

	/**
	 * add a message entry if absent.
	 * @param additions addition message.
	 * @return {@code Result} of response.
	 */
	public Result<T> additionIfAbsent(Map<String, String> additions) {
		if (Objects.isNull(this.additions)) {
			this.additions = Maps.newHashMapWithExpectedSize(2);
		}
		additions.forEach(this.additions::putIfAbsent);
		return this;
	}

	@Transient
	public boolean isOkCode(String codeValue) {
		return ResultCodesSupplier.getInstance().ok().code().equals(codeValue);
	}

	@Transient
	public boolean isException() {
		return ResultCodesSupplier.getInstance().exception().code().equals(this.code);
	}

	/**
	 * get response's message.
	 * @return message. if response's data is not-null, and instance of
	 * {@link EgoResultMessage}, the ego message will be the reply message. otherwise,
	 * return the set message info.
	 */
	public String getMessage() {
		if (Objects.nonNull(this.data) && this.data instanceof EgoResultMessage) {
			return ((EgoResultMessage) this.data).message();
		}
		return this.message;
	}

	public <E> Result<E> convert(Class<E> clazz) {
		return (Result<E>) this;
	}

	public <E> Result<E> convert() {
		return (Result<E>) this;
	}

}
