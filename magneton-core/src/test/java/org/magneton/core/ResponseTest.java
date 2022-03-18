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
class ResponseTest {

	@Test
	void ok() {
		Response ok = Response.ok();
		Assertions.assertTrue(ok.isOk(), "response is not ok");
	}

	@Test
	void testOk() {
		Response<String> ok = Response.ok("ok");
		Assertions.assertEquals("ok", ok.getData(), "response's data is not equals");
	}

	@Test
	void bad() {
		Response bad = Response.bad();
		Assertions.assertFalse(bad.isOk(), "response is not bad");
	}

	@Test
	void testBad() {
		Response<String> bad = Response.bad("bad");
		Assertions.assertEquals("bad", bad.getData(), "response's data is not bad");
	}

	@Test
	void exception() {
		Response exception = Response.exception();
		Assertions.assertTrue(exception.isException(), "response is not exception");
	}

	@Test
	void response() {
		Response response = Response.response(ResponseMessage.valueOf("100", "100bad"));
		Assertions.assertEquals("100", response.getCode(), "response's code does not match");
		Assertions.assertEquals("100bad", response.getMessage(), "response's message does not match");
	}

	@Test
	void testResponse() {
		Response response = Response.response(ResponseMessage.valueOf("100", "100bad"), "data");
		Assertions.assertEquals("100", response.getCode(), "response's code does not match");
		Assertions.assertEquals("100bad", response.getMessage(), "response's message does not match");
		Assertions.assertEquals("data", response.getData(), "response's data does not match");
	}

	@Test
	void addition() {
		Response ok = Response.ok().addition("key", "value");
		Assertions.assertTrue(ok.getAdditions().containsKey("key"), "response's addition does not effectived");
		Assertions.assertEquals("value", ok.getAdditions().get("key"), "response's addition value does not match");
	}

	@Test
	void getMessage() {
		Response message = Response.ok().message("message");
		Assertions.assertEquals("message", message.getMessage(), "response's message does not match");
	}

}
