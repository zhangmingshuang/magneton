package org.magneton.test.util;

import java.lang.annotation.Annotation;
import java.util.Map;
import javax.validation.constraints.AssertTrue;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * .
 *
 * @author zhangmsh 2021/8/24
 * @since
 */
class AnnotationUtilTest {

  public static class TestA {
    @AssertTrue private boolean bool;
  }

  @SneakyThrows
  @Test
  void testA() {
    Map<Class, Annotation> bool =
        AnnotationUtil.findAnnotations(TestA.class.getDeclaredField("bool"));
    Assertions.assertTrue(bool.containsKey(AssertTrue.class));
  }
}
