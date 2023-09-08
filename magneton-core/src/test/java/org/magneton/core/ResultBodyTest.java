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
class ResultBodyTest {

	@Test
	void code() {
		Result result = Result.valueOf(new CustomizedResultBody());
		Assertions.assertEquals("100", result.getCode(), "response code not equals");
	}

	@Test
	void message() {
		Result result = Result.valueOf(new CustomizedResultBody());
		Assertions.assertEquals("customized error", result.getMessage(), "response message not equals");
	}

	class CustomizedResultBody implements ResultBody {

		@Override
		public String code() {
			return "100";
		}

		@Override
		public String message() {
			return "customized error";
		}

	}

}
