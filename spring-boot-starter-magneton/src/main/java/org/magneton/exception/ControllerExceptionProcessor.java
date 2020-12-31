package org.magneton.exception;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;
import javax.annotation.Nullable;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.magneton.core.Response;
import org.magneton.core.ResponseException;
import org.magneton.core.util.MoreCollections;
import org.magneton.properties.MagnetonProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.OrderUtils;
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

  @Getter
  private final ExceptionProcessorContext exceptionProcessorContext =
      new DefaultExceptionProcessorContext();

  private final AtomicBoolean exceptionProcessorsAddable = new AtomicBoolean(true);
  private final ReentrantLock lock = new ReentrantLock();
  /** the expcetion processor in spring bean context. */
  @Autowired(required = false)
  @Nullable
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

  /**
   * add a exception processor.
   *
   * @param exceptionProcessor the exception processor.
   * @throws UnsupportedOperationException if after {@code afterPropertiesSet} method.
   */
  public void addExceptionProcessors(ExceptionProcessor exceptionProcessor) {
    Preconditions.checkNotNull(exceptionProcessor, "exceptionProcessor must be not null");
    this.lock.lock();
    try {
      if (!this.exceptionProcessorsAddable.get()) {
        throw new UnsupportedOperationException(
            "addExceptionProcessors shoud be before afterPropertiesSet");
      }
      if (this.exceptionProcessors == null) {
        this.exceptionProcessors = new ArrayList<>(4);
      }
      this.exceptionProcessors.add(exceptionProcessor);
    } finally {
      this.lock.unlock();
    }
  }

  @Override
  public void afterPropertiesSet() {
    this.lock.lock();
    try {
      if (this.exceptionProcessorsAddable.compareAndSet(true, false)) {
        if (!MoreCollections.isNullOrEmpty(this.exceptionProcessors)) {
          this.exceptionProcessors.sort(
              Comparator.comparingInt(
                  e -> OrderUtils.getOrder(e.getClass(), Ordered.LOWEST_PRECEDENCE)));
          this.exceptionProcessors.forEach(
              this.exceptionProcessorContext::registerExceptionProcessor);
        }
      } else {
        throw new UnsupportedOperationException(
            "afterPropertiesSet can only be called once or after addExceptionProcessor finished");
      }
    } finally {
      this.lock.unlock();
    }
  }
}
