package org.magneton.spring.starter.adapter;

import lombok.extern.slf4j.Slf4j;
import org.magneton.core.Result;
import org.magneton.core.ResultException;
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

	@ExceptionHandler(ResultException.class)
	public Result<?> responseException(ResultException e) {
		log.error("response", e);
		return e.getResponse();
	}

	@ExceptionHandler(ProcessException.class)
	public Result<?> processException(ProcessException e) {
		log.error("process", e);
		return Result.fail().message("处理异常");
	}

}
