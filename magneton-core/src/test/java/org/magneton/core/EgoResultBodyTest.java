package org.magneton.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * .
 *
 * @author zhangmsh
 * @since 1.0.0
 */
class EgoResultBodyTest {

	@Test
	void message() {
		Result<Data> ok = Result.successWith(new Data()).message("message");
		Assertions.assertEquals("egoMessage", ok.getMessage(), "response's message not rewrite with ego");
	}

	class Data implements EgoResultMessage {

		@Override
		public String message() {
			return "egoMessage";
		}

	}

}