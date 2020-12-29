package org.magneton.spring.exception;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.magneton.Response;
import org.magneton.ResponseException;
import org.magneton.spring.properties.MagnetonProperties;
import org.magneton.util.MoreCollections;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * global exception processor.
 *
 * <p>this use to process all the {@code Exception}s if throwed. but, in default is only {@link
 * ResponseException} processed . if want process other {@code Exception}s, using {@link
 * ExceptionProcessor} to register a processor to process a specify {@code Exception}.
 *
 * <p>simultaneously, customize a new {@code RestControllerAdvice} extend other exception processor
 * is also feasable.
 *
 * @author zhangmsh
 * @version 1.0.0
 * @since 2020/12/25
 * @see ExceptionProcessor
 */
@RestControllerAdvice
@Slf4j
@ConditionalOnProperty(
    prefix = MagnetonProperties.PREFIX,
    value = "exception.advice.enable",
    havingValue = "true",
    matchIfMissing = true)
public class ControllerExceptionProcessor implements InitializingBean {

  private final ExceptionProcessorContext exceptionProcessorContext =
      new DefaultExceptionProcessorContext();

  /** the expcetion processor in spring bean context. */
  @Autowired(required = false)
  private List<ExceptionProcessor> exceptionProcessors;

  /**
   * the default processor.
   *
   * @param exception {@code ResponseException}
   * @return {@code Response}
   * @see ResponseException
   */
  @ExceptionHandler(ResponseException.class)
  public Response onResponseException(ResponseException exception) {
    Response response = exception.getResponse();
    log.error(response.getMessage(), exception);
    return response;
  }

  @SuppressWarnings("ProhibitedExceptionDeclared")
  @ExceptionHandler(Exception.class)
  public Object onException(Exception exception) throws Exception {
    return this.exceptionProcessorContext.handle(exception);
  }

  @Override
  public void afterPropertiesSet() {
    if (!MoreCollections.isNullOrEmpty(this.exceptionProcessors)) {
      this.exceptionProcessors.forEach(this.exceptionProcessorContext::registerExceptionProcessor);
    }
  }
}
