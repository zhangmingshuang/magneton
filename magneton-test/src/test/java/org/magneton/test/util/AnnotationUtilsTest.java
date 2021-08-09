package org.magneton.test.util;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.magneton.test.annotation.TestComponent;
import org.magneton.test.annotation.TestSort;

/**
 * .
 *
 * @author zhangmsh 2021/8/3
 * @since
 */
class AnnotationUtilsTest {

  @Test
  void findAnnotations() {
    List<Annotation> annotations = AnnotationUtils.findAnnotations(T.class);
    Assertions.assertEquals(3, annotations.size());
  }

  @Test
  void test() {
    Map<String, Object> metadata =
        AnnotationUtils.getMetadata(AnnotationUtils.findAnnotation(T.class, TestSort.class));
    Object value = metadata.get("value");
    Assertions.assertNotNull(value);
  }

  @TestComponent
  public static class T extends T2 implements TI {}

  @TestSort
  public static interface TI {}

  public static class T2 {}
}
