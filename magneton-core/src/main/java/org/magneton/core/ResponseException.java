package org.magneton.core;

/**
 * a {@code Response} process in a {@code Exception}.
 *
 * <p>
 * use to response a {@link Response} message with a {@code Expcetion} to interrupt the
 * process.
 *
 * <p>
 * this {@code ResponseException} should processing in a global expcetion advice.
 *
 * @author zhangmsh
 * @version 1.0.0
 * @since 2020/10/29
 */
@SuppressWarnings("rawtypes")
public class ResponseException extends RuntimeException {

	private static final long serialVersionUID = 840737242672355742L;

	@SuppressWarnings("java:S1948")
	private final Response response;

	public ResponseException(Response response) {
		super(response.getMessage());
		this.response = response;
	}

	public ResponseException(ResponseMessage responseMessage) {
		this(Response.response(responseMessage));
	}

	public static ResponseException valueOf(Response response) {
		return new ResponseException(response);
	}

	public static ResponseException valueOf(ResponseMessage responseMessage) {
		return new ResponseException(responseMessage);
	}

	public Response getResponse() {
		return this.response;
	}

}
