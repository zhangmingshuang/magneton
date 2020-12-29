package org.magneton;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.magneton.core.Response;
import org.magneton.exception.DefaultExceptionProcessorContext;
import org.magneton.exception.ExceptionProcessorContext;
import org.magneton.exception.ExceptionProcessor;
import org.magneton.exception.ExceptionProcessorRegister;

/**
 * .
 *
 * @author zhangmsh
 * @version 1.0.0
 * @since 2020/12/28
 */
@SuppressWarnings("CyclicClassDependency")
class ExceptionProcessorTest {

  private static final String NULL_ERROR = "这是空指针";

  @Test
  void test() throws Exception {
    ExceptionProcessorContext exceptionProcessorContext = new DefaultExceptionProcessorContext();
    exceptionProcessorContext.registerExceptionProcessor(new ExceptionProcessorImpl());

    Object test = exceptionProcessorContext.handle(new NullPointerException("test"));
    System.out.println(test);
    Assertions.assertSame(test.getClass(), Response.class, "class not match");
    Response response = (Response) test;
    Assertions.assertEquals(NULL_ERROR, response.getMessage(), "response message not match");
  }

  @Test
  void testException() {
    ExceptionProcessorContext exceptionProcessorContext = new DefaultExceptionProcessorContext();
    exceptionProcessorContext.registerExceptionProcessor(new ExceptionProcessorImpl());
    try {
      Object argsError =
          exceptionProcessorContext.handle(new IllegalArgumentException("args error"));
      Assertions.fail("this should always not touch");
    } catch (Exception e) {
      Assertions.assertSame(e.getClass(), IllegalArgumentException.class, "class not match");
      Assertions.assertEquals("args error", e.getMessage(), "error message not match");
    }
  }

  class ExceptionProcessorImpl implements ExceptionProcessor {

    @Override
    public void registerExceptionProcessor(ExceptionProcessorRegister register) {
      register.addHandler(
          NullPointerException.class, e -> Response.exception().message(NULL_ERROR));
    }
  }
}
