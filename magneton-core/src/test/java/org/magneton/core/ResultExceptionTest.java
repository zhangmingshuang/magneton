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
class ResultExceptionTest {

	@Test
	void valueOf() {
		ResultException resultException = ResultException.valueOf(Result.exception().message("error"));
		Assertions.assertEquals("error", resultException.getMessage(), "exception message does not equals");
		Result exception = Result.exception();
		Assertions.assertThrows(ResultException.class, () -> {
			throw new ResultException(exception);
		});
	}

	@Test
	void getResponse() {
		Result exception = Result.exception();
		ResultException resultException = ResultException.valueOf(exception);
		Assertions.assertEquals(exception, resultException.getResponse(), "exception's response does not equals");
	}

}
