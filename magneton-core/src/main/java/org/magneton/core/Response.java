package org.magneton.core;

import java.util.Map;
import java.util.Objects;

import javax.annotation.Nullable;

import lombok.Getter;
import lombok.ToString;
import org.magneton.core.base.Strings;
import org.magneton.core.collect.Maps;

/**
 * .
 *
 * @author zhangmsh
 * @version 1.0.0
 * @since 2020/10/19
 */
@Getter
@ToString
public class Response<T> {

	private String code;

	private T data;

	private String message;

	private long timestamp;

	private Map<String, String> additions;

	private Response() {
		// private
	}

	public static Response ok() {
		return ok(null);
	}

	public static <T> Response<T> ok(@Nullable T t) {
		return response(ResponseCodesSupplier.getInstance().ok(), t);
	}

	public static Response bad() {
		return bad(null);
	}

	public static <T> Response<T> bad(T t) {
		return response(ResponseCodesSupplier.getInstance().bad(), t);
	}

	public static Response exception() {
		return response(ResponseCodesSupplier.getInstance().exception(), null);
	}

	public static Response response(ResponseMessage responseMessage) {
		return response(responseMessage, null);
	}

	/**
	 * response with body.
	 * @param responseMessage {@code ResponseBody}
	 * @param data response data.
	 * @param <T> T
	 * @return {@code Response} of response.
	 */
	public static <T> Response<T> response(ResponseMessage responseMessage, T data) {
		Response<T> response = new Response<>();
		String message = responseMessage.message();
		return response.code(responseMessage.code()).message(message).data(data).timestamp(System.currentTimeMillis());
	}

	public Response<T> code(String code) {
		this.code = code;
		return this;
	}

	public Response<T> message(String message) {
		this.message = message;
		return this;
	}

	public Response<T> messageFormat(Object... args) {
		if (Strings.isNullOrEmpty(message)) {
			message = Strings.lenientFormat(message, args);
		}
		return this;
	}

	public Response<T> data(T data) {
		this.data = data;
		return this;
	}

	public Response<T> timestamp(long timestamp) {
		this.timestamp = timestamp;
		return this;
	}

	/**
	 * add a message entry.
	 * @param key addition message key.
	 * @param value addition message value.
	 * @return {@code Response} of response.
	 */
	public Response<T> addition(String key, String value) {
		if (Objects.isNull(additions)) {
			additions = Maps.newHashMapWithExpectedSize(2);
		}
		additions.put(key, value);
		return this;
	}

	public boolean isOk() {
		return ResponseCodesSupplier.getInstance().ok().code().equals(code);
	}

	public boolean isException() {
		return ResponseCodesSupplier.getInstance().exception().code().equals(code);
	}

	/**
	 * get reponse's message.
	 * @return message. if response's data is not-null, and instalce of
	 * {@link EgoResponseMessage}, the ego message will be the reply message. otherwise,
	 * return the set message info.
	 */
	public String getMessage() {
		if (Objects.nonNull(data) && data instanceof EgoResponseMessage) {
			return ((EgoResponseMessage) data).message();
		}
		return message;
	}

}
