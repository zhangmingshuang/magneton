package org.magneton.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.magneton.core.ResultCodesSupplier.DefaultResultBody;

/**
 * .
 *
 * @author zhangmsh
 * @since 1.0.0
 */
class ResultDefaultResultBodySupplierTest {

	private static final String CODE = "100";

	private static final String BAD = "bad";

	@Test
	void getInstance() {
		ResultCodesSupplier resultCodesSupplier = ResultCodesSupplier.getInstance();
		this.changeresponseCodes();
		ResultCodesSupplier customizedresponseCodesSupplier = ResultCodesSupplier.getInstance();
		Assertions.assertNotEquals(resultCodesSupplier, customizedresponseCodesSupplier,
				"response codes responseCodes change error");
	}

	@Test
	void ok() {
		ResultCodesSupplier resultCodesSupplier = ResultCodesSupplier.getInstance();
		ResultBody ok = resultCodesSupplier.ok();
		Assertions.assertEquals(DefaultResultBody.OK, ok, "responseCodes ok error");
	}

	@Test
	void bad() {
		this.changeresponseCodes();
		ResultCodesSupplier resultCodesSupplier = ResultCodesSupplier.getInstance();
		ResultBody bad = resultCodesSupplier.bad();
		Assertions.assertEquals(CODE, bad.code(), "responseCodes code does not match");
		Assertions.assertEquals(BAD, bad.message(), "responseCodes message does not match");
	}

	@Test
	void exception() {
		ResultCodesSupplier resultCodesSupplier = ResultCodesSupplier.getInstance();
		ResultBody exception = resultCodesSupplier.exception();
		Assertions.assertEquals(DefaultResultBody.EXCEPTION, exception, "responseCodes exception error");
	}

	private void changeresponseCodes() {
		ResultCodesSupplier.Instance.setResponseCodesSupplier(new ResultCodesSupplier() {
			@Override
			public ResultBody bad() {
				return ResultBody.valueOf(CODE, BAD);
			}
		});
	}

}