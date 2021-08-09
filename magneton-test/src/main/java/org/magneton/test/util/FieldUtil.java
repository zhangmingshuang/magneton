package org.magneton.test.util;

import com.google.common.collect.Sets;
import java.lang.reflect.Field;
import java.util.Set;
import javax.annotation.Nullable;

/**
 * .
 *
 * @author zhangmsh 2021/8/6
 * @since 2.0.0
 */
public class FieldUtil {

  private FieldUtil() {}

  public static Set<Field> getFields(@Nullable Class clazz) {
    Set<Field> fields = Sets.newHashSet();
    getFields(fields, clazz);
    return fields;
  }

  private static void getFields(Set<Field> fields, Class clazz) {
    if (clazz == null || clazz == Object.class) {
      return;
    }
    Field[] declaredFields = clazz.getDeclaredFields();
    for (Field declaredField : declaredFields) {
      if (fields.contains(declaredField)) {
        continue;
      }
      fields.add(declaredField);
    }
    getFields(fields, clazz.getSuperclass());
  }
}
