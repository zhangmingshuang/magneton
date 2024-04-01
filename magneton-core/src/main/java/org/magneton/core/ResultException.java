package org.magneton.core;

/**
 * a {@code Response} process in a {@code Exception}.
 *
 * <p>
 * use to response a {@link Result} message with a {@code Exception} to interrupt the
 * process.
 *
 * <p>
 * this {@code ResponseException} should be processing in a global exception advice.
 *
 * @author zhangmsh
 * @since 2020/10/29
 */
@SuppressWarnings("rawtypes")
public class ResultException extends RuntimeException {

	private static final long serialVersionUID = 840737242672355742L;

	@SuppressWarnings("java:S1948")
	private final Result result;

	public ResultException(Result result) {
		super(result.getMessage());
		this.result = result;
	}

	public ResultException(ResultBody<?> resultBody) {
		this(Result.valueOf(resultBody));
	}

	public static ResultException valueOf(Result result) {
		return new ResultException(result);
	}

	public static ResultException valueOf(ResultBody resultBody) {
		return new ResultException(resultBody);
	}

	public Result getResponse() {
		return this.result;
	}

}