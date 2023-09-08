package org.magneton.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * .
 *
 * @author zhangmsh
 * @version 1.0.0
 * @since 2020/12/29
 */
class ResultTest {

	@Test
	void ok() {
		Result ok = Result.ok();
		Assertions.assertTrue(ok.isOk(), "response is not ok");
	}

	@Test
	void testOk() {
		Result<String> ok = Result.ok("ok");
		Assertions.assertEquals("ok", ok.getData(), "response's data is not equals");
	}

	@Test
	void bad() {
		Result bad = Result.bad();
		Assertions.assertFalse(bad.isOk(), "response is not bad");
	}

	@Test
	void testBad() {
		Result<String> bad = Result.bad("bad");
		Assertions.assertEquals("bad", bad.getData(), "response's data is not bad");
	}

	@Test
	void exception() {
		Result exception = Result.exception();
		Assertions.assertTrue(exception.isException(), "response is not exception");
	}

	@Test
	void response() {
		Result result = Result.valueOf(ResultBody.valueOf("100", "100bad"));
		Assertions.assertEquals("100", result.getCode(), "response's code does not match");
		Assertions.assertEquals("100bad", result.getMessage(), "response's message does not match");
	}

	@Test
	void testResponse() {
		Result result = Result.valueOf(ResultBody.valueOf("100", "100bad"), "data");
		Assertions.assertEquals("100", result.getCode(), "response's code does not match");
		Assertions.assertEquals("100bad", result.getMessage(), "response's message does not match");
		Assertions.assertEquals("data", result.getData(), "response's data does not match");
	}

	@Test
	void addition() {
		Result ok = Result.ok().addition("key", "value");
		Assertions.assertTrue(ok.getAdditions().containsKey("key"), "response's addition does not effectived");
		Assertions.assertEquals("value", ok.getAdditions().get("key"), "response's addition value does not match");
	}

	@Test
	void getMessage() {
		Result message = Result.ok().message("message");
		Assertions.assertEquals("message", message.getMessage(), "response's message does not match");
	}

}
