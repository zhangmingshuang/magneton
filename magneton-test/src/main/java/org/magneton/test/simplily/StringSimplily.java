package org.magneton.test.simplily;

import java.lang.reflect.Field;
import java.util.Set;
import javax.annotation.Nullable;
import lombok.SneakyThrows;
import org.magneton.test.util.FieldUtil;

/**
 * @author zhangmsh 2021/8/9
 * @since 1.0.0
 */
public class StringSimplily implements Simplily {
  private static final StringSimplily STRING_SIMPLY = new StringSimplily();

  public static StringSimplily getInstance() {
    return STRING_SIMPLY;
  }

  @SneakyThrows
  @Nullable
  public String string(@Nullable Object obj) {
    if (obj == null) {
      return null;
    }
    StringBuilder builder = new StringBuilder(128);
    builder.append(obj.getClass().getName());
    Set<Field> fields = FieldUtil.getFields(obj.getClass());
    if (!fields.isEmpty()) {
      builder.append("[");

      for (Field field : fields) {
        field.setAccessible(true);
        Object value = field.get(obj);
        builder.append(field.getName()).append("=").append(value).append(",");
      }
      builder.setLength(builder.length() - 1);
      builder.append("]");
    }
    return builder.toString();
  }
}
