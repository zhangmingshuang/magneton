package org.magneton.spring.starter.adapter;

import lombok.extern.slf4j.Slf4j;
import org.magneton.core.Response;
import org.magneton.core.ResponseException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * .
 *
 * @author zhangmsh 18/03/2022
 * @since 2.0.7
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler(ResponseException.class)
	public Response<?> responseException(ResponseException e) {
		return e.getResponse();
	}

}
