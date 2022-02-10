package org.magneton.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.magneton.core.Response;
import org.magneton.core.ResponseException;
import org.magneton.spring.starter.exception.ControllerExceptionProcessor;
import org.magneton.spring.starter.exception.DuplicateProcessorException;
import org.magneton.spring.starter.exception.ExceptionProcessor;
import org.magneton.spring.starter.exception.ExceptionProcessorRegister;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.core.annotation.Order;

/**
 * .
 *
 * @author zhangmsh
 * @version 1.0.0
 * @since 2020/12/31
 */
@ExtendWith(MockitoExtension.class)
class ControllerExceptionProcessorTest {

	@InjectMocks
	private ControllerExceptionProcessor controllerExceptionProcessor;

	private int i = 1;

	@Test
	void testOrder() {
		this.controllerExceptionProcessor.addExceptionProcessors(new NullPointerExceptionProcessor());
		this.controllerExceptionProcessor.addExceptionProcessors(new BExceptionProcessor());
		this.controllerExceptionProcessor.addExceptionProcessors(new AExceptionProcessor());
		this.controllerExceptionProcessor.afterPropertiesSet();
		Assertions.assertThrows(UnsupportedOperationException.class,
				() -> this.controllerExceptionProcessor.afterPropertiesSet());
		Assertions.assertThrows(DuplicateProcessorException.class, () -> this.controllerExceptionProcessor
				.getExceptionProcessorContext().registerExceptionProcessor(new NullPointerExceptionProcessor()));
		Assertions.assertEquals(3, this.i, "i not 3");
	}

	@Test
	void testResponseException() {
		ResponseException exception = ResponseException.valueOf(Response.exception().message("error"));
		Response response = this.controllerExceptionProcessor.onResponseException(exception);
		Assertions.assertTrue(response.isException(), "response is not exception");
		Assertions.assertEquals("error", response.getMessage(), "response message does not equals");
	}

	@Test
	void testException() throws Exception {
		this.controllerExceptionProcessor.getExceptionProcessorContext()
				.registerExceptionProcessor(new NullPointerExceptionProcessor());
		Response response = (Response) this.controllerExceptionProcessor.onException(new NullPointerException("null"));
		Assertions.assertTrue(response.isException(), "response is not exception");
		Assertions.assertEquals(NullPointerExceptionProcessor.NULL_ERROR, response.getMessage(),
				"response message does not equals");
	}

	@Order(1)
	class AExceptionProcessor implements ExceptionProcessor {

		@Override
		public void registerExceptionProcessor(ExceptionProcessorRegister register) {
			Assertions.assertEquals(1, ControllerExceptionProcessorTest.this.i++, "i not 1");
		}

	}

	@Order(2)
	class BExceptionProcessor implements ExceptionProcessor {

		@Override
		public void registerExceptionProcessor(ExceptionProcessorRegister register) {
			Assertions.assertEquals(2, ControllerExceptionProcessorTest.this.i++, "i not 2");
		}

	}

}
