package org.magneton.core;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.ToString;

import javax.annotation.Nullable;
import java.beans.Transient;
import java.util.Map;
import java.util.Objects;

/**
 * 统一的响应.
 *
 * @author zhangmsh
 * @version 1.0.0
 * @since 2020/10/19
 */
@SuppressWarnings("rawtypes")
@Getter
@ToString
public class Result<T> {

	private boolean ok;

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

	public Result conver() {
		return this;
	}

	public static Result ok() {
		return ok(null);
	}

	public static <T> Result<T> ok(@Nullable T t) {
		return valueOf(ResultCodesSupplier.getInstance().ok(), t);
	}

	public static Result bad() {
		return bad(null);
	}

	public static <T> Result<T> bad(T t) {
		return valueOf(ResultCodesSupplier.getInstance().bad(), t);
	}

	public static Result error(String message) {
		return bad().message(message);
	}

	public static Result exception() {
		return valueOf(ResultCodesSupplier.getInstance().exception(), null);
	}

	/**
	 * response with body.
	 * @param resultBody {@code ResultBody}
	 * @param data response data.
	 * @param <T> T
	 * @return {@code Result} of response.
	 */
	public static <T> Result<T> valueOf(ResultBody<T> resultBody) {
		Preconditions.checkNotNull(resultBody, "resultBody must be not null");

		T data = resultBody.data();
		return valueOf(resultBody, data);
	}

	private static <T> Result<T> valueOf(ResultBody<T> resultBody, T data) {
		Result<T> result = new Result<>();
		String message = resultBody.message();
		return result.code(resultBody.code()).message(message).data(data).timestamp(System.currentTimeMillis());
	}

	public Result<T> code(String code) {
		Preconditions.checkNotNull(code, "code must be not null");

		this.code = code;
		this.ok = this.isOkCode(code);
		return this;
	}

	public Result<T> message(String message) {
		Preconditions.checkNotNull(message, "message must be not null");
		this.message = message;
		return this;
	}

	public Result<T> message(String message, @Nullable Object... args) {
		Preconditions.checkNotNull(message, "message must be not null");
		this.message = String.format(message, args);
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

}
