package cn.nascent.framework.test.util;

import cn.nascent.framework.test.injector.Inject;
import com.google.common.reflect.TypeToken;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import javax.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

/**
 * .
 *
 * @author zhangmsh 2021/8/3
 * @since 2.0.0
 */
@Slf4j
public class GenericUtil {

  private GenericUtil() {}

  @Nullable
  public static Inject getClass(Class<?> clazz) {
    return getClass(clazz, 0);
  }

  @Nullable
  public static Inject getClass(Class<?> clazz, int index) {
    try {
      if (clazz.isInterface()) {
        Type[] genericInterfaces = clazz.getGenericInterfaces();
        if (genericInterfaces.length <= index) {
          return null;
        }
        Type[] actualTypeArguments =
            ((ParameterizedTypeImpl) (genericInterfaces[index])).getActualTypeArguments();
        if (actualTypeArguments.length < 1) {
          return null;
        }
        return getActualClass(actualTypeArguments[0]);
      }
      Type type = clazz.getGenericSuperclass();
      ParameterizedType parameterizedType = (ParameterizedType) type;
      Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
      return getActualClass(actualTypeArguments[0]);

    } catch (Throwable e) {
      // ignore
    }
    return null;
  }

  @Nullable
  public static Inject getClass(Type type) {
    try {
      Type[] actualTypeArguments =
          ((ParameterizedTypeImpl) TypeToken.of(type).getType()).getActualTypeArguments();
      if (actualTypeArguments.length < 1) {
        return null;
      }
      return getActualClass(actualTypeArguments[0]);
    } catch (Throwable e) {
      // ignore
    }
    return null;
  }

  @Nullable
  public static Inject getClass(Object object) {
    return getClass(object, 0);
  }

  @Nullable
  public static Inject getClass(Object object, int index) {
    try {
      if (Field.class.isAssignableFrom(object.getClass())) {
        Type[] actualTypeArguments =
            ((ParameterizedTypeImpl) ((Field) object).getGenericType()).getActualTypeArguments();
        if (actualTypeArguments.length <= index) {
          return null;
        }
        Type actualTypeArgument = actualTypeArguments[index];
        return getActualClass(actualTypeArgument);
      }
    } catch (Throwable e) {
      // ignore
      log.warn("get generic type error:{}", e.getMessage());
    }
    return null;
  }

  private static Inject getActualClass(Type actualTypeArgument) {
    if (ParameterizedType.class.isAssignableFrom(actualTypeArgument.getClass())) {
      return Inject.of(
          (Class) ((ParameterizedType) actualTypeArgument).getRawType(), actualTypeArgument);
    }
    return Inject.of((Class) actualTypeArgument);
  }
}
