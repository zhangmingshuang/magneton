package org.magneton.framework.core;

import lombok.ToString;

/**
 * .
 *
 * @author zhangmsh
 * @since 1.0.0
 */
@SuppressWarnings({ "MissingJavadocType", "unchecked" })
public interface ResultCodesSupplier {

	/**
	 * response code supplier instance.
	 * @return instance
	 */
	static ResultCodesSupplier getInstance() {
		return Instance.resultCodesSupplier;
	}

	/**
	 * get ok code.
	 * @return ok code
	 */
	default <T> ResultBody<T> ok() {
		return (ResultBody<T>) DefaultResultBody.OK;
	}

	/**
	 * get bad code.
	 * @return bad code
	 */
	default <T> ResultBody<T> bad() {
		return (ResultBody<T>) DefaultResultBody.BAD;
	}

	/**
	 * get exception code.
	 * @return exception code
	 */
	default <T> ResultBody<T> exception() {
		return (ResultBody<T>) DefaultResultBody.EXCEPTION;
	}

	/** default response message defined. */
	@ToString
	enum DefaultResultBody implements ResultBody<Object> {

		/** success. */
		OK("0", "操作成功"),
		/** fail. */
		BAD("1", "操作失败"),
		/** exception. */
		EXCEPTION("2", "系统异常");

		private final String code;

		private final String message;

		DefaultResultBody(String code, String message) {
			this.code = code;
			this.message = message;
		}

		@Override
		public String code() {
			return this.code;
		}

		@Override
		public String message() {
			return this.message;
		}

	}

	/**
	 * Response codes real supplier.
	 *
	 * <p>
	 * In default, the global response use the default code to reply. but it may change
	 * the default rule at possible. {@code Instance} exposed an entrance to change the
	 * response's code supplier.
	 */
	class Instance {

		private static ResultCodesSupplier resultCodesSupplier = new ResultCodesSupplier() {
		};

		private Instance() {
		}

		public static void setResponseCodesSupplier(ResultCodesSupplier resultCodesSupplier) {
			Instance.resultCodesSupplier = resultCodesSupplier;
		}

	}

}