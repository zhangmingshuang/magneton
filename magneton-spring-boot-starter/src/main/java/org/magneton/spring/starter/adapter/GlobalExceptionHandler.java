package org.magneton.spring.starter.adapter;

import lombok.extern.slf4j.Slf4j;
import org.magneton.core.Response;
import org.magneton.core.ResponseException;
import org.magneton.foundation.exception.ProcessException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * .
 *
 * @author zhangmsh 18/03/2022
 * @since 2.0.7
 */
@Slf4j
@ControllerAdvice
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResponseException.class)
	public Response<?> responseException(ResponseException e) {
		log.error("response", e);
		return e.getResponse();
	}

	@ExceptionHandler(ProcessException.class)
	public Response<?> processException(ProcessException e) {
		log.error("process", e);
		return Response.bad().message("处理异常");
	}

}
