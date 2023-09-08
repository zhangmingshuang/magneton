package org.magneton.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.magneton.core.Result;
import org.magneton.spring.starter.exception.DefaultExceptionProcessorContext;
import org.magneton.spring.starter.exception.ExceptionProcessorContext;

/**
 * .
 *
 * @author zhangmsh
 * @version 1.0.0
 * @since 2020/12/28
 */
class ExceptionProcessorTest {

	@Test
	void test() throws Exception {
		ExceptionProcessorContext exceptionProcessorContext = new DefaultExceptionProcessorContext();
		exceptionProcessorContext.registerExceptionProcessor(new NullPointerExceptionProcessor());

		Object test = exceptionProcessorContext.handle(new NullPointerException("test"));
		System.out.println(test);
		Assertions.assertSame(test.getClass(), Result.class, "class not match");
		Result result = (Result) test;
		Assertions.assertEquals(NullPointerExceptionProcessor.NULL_ERROR, result.getMessage(),
				"response message not match");
	}

	@Test
	void testException() {
		ExceptionProcessorContext exceptionProcessorContext = new DefaultExceptionProcessorContext();
		exceptionProcessorContext.registerExceptionProcessor(new NullPointerExceptionProcessor());
		try {
			Object argsError = exceptionProcessorContext.handle(new IllegalArgumentException("args error"));
			Assertions.fail("this should always not touch");
		}
		catch (Exception e) {
			Assertions.assertSame(e.getClass(), IllegalArgumentException.class, "class not match");
			Assertions.assertEquals("args error", e.getMessage(), "error message not match");
		}
	}

}
