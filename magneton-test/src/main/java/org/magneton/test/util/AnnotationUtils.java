package org.magneton.test.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;

/**
 * .
 *
 * @author zhangmsh 2021/8/2
 * @since 2.0.0
 */
@Slf4j
public class AnnotationUtils {

  private AnnotationUtils() {}

  @Nullable
  public static <A extends Annotation> A findAnnotation(
      @Nullable Class clazz, Class<A> annotationClass) {
    if (clazz == null || clazz == Object.class) {
      return null;
    }
    Preconditions.checkNotNull(annotationClass, "annotationClass must be not null");
    Annotation annotation = clazz.getAnnotation(annotationClass);
    if (annotation == null) {
      annotation = findAnnotation(clazz.getSuperclass(), annotationClass);
    }
    if (annotation == null) {
      Class[] interfaces = clazz.getInterfaces();
      for (Class interfaceClass : interfaces) {
        annotation = findAnnotation(interfaceClass, annotationClass);
        if (annotation != null) {
          return (A) annotation;
        }
      }
    }
    return (A) annotation;
  }

  @Nullable
  public static <A extends Annotation> A findAnnotation(Field field, Class<A> annotationClass) {
    Preconditions.checkNotNull(annotationClass, "annotationClass must be not null");
    Preconditions.checkNotNull(field, "Field class must be not null");
    return field.getAnnotation(annotationClass);
  }

  public static List<Annotation> findAnnotations(@Nullable Object object) {
    if (object == null) {
      return Collections.emptyList();
    }
    if (Field.class.isAssignableFrom(object.getClass())) {
      return Lists.newArrayList(((Field) object).getAnnotations());
    }
    return findAnnotations(object.getClass());
  }

  public static List<Annotation> findAnnotations(@Nullable Class clazz) {
    if (clazz == null || clazz == Object.class) {
      return Collections.emptyList();
    }
    List<Annotation> annotations = Lists.newArrayList();
    return findAnnotations(annotations, clazz);
  }

  private static List<Annotation> findAnnotations(
      List<Annotation> annotations, @Nullable Class clazz) {
    Preconditions.checkNotNull(annotations);
    if (clazz == null || clazz == Object.class) {
      return annotations;
    }
    Annotation[] clazzAnnotations = clazz.getAnnotations();
    for (Annotation clazzAnnotation : clazzAnnotations) {
      annotations.add(clazzAnnotation);
    }
    findAnnotations(annotations, clazz.getSuperclass());
    for (Class interfaceClazz : clazz.getInterfaces()) {
      findAnnotations(annotations, interfaceClazz);
    }
    return annotations;
  }

  public static Map<String, Object> getMetadata(@Nullable Annotation annotation) {
    if (annotation == null) {
      return Collections.emptyMap();
    }
    Method[] methods = annotation.getClass().getMethods();
    if (methods.length < 1) {
      return Collections.emptyMap();
    }
    Map<String, Object> metadata = Maps.newHashMap();
    for (Method method : methods) {
      if (method.getDeclaringClass() == Object.class) {
        continue;
      }
      if (method.getParameters().length > 0) {
        continue;
      }
      try {
        metadata.put(method.getName(), method.invoke(annotation));
      } catch (Throwable e) {
        log.error("error on method {} in {}", method, annotation);
        throw new RuntimeException(e);
      }
    }
    return metadata;
  }
}
