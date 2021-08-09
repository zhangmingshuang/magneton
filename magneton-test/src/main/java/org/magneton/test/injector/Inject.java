package org.magneton.test.injector;

import com.google.common.base.Preconditions;
import java.lang.reflect.Field;
import java.util.Set;
import javax.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.magneton.test.util.FieldUtil;

/**
 * .
 *
 * @author zhangmsh 2021/8/3
 * @since 2.0.0
 */
@Setter
@Getter
@ToString
public class Inject<T> {

  private Class<T> inectType;

  @Nullable private Object object;

  public static <T> Inject<T> of(Class<T> type) {
    return new Inject(type, null);
  }

  public static <T> Inject<T> of(Class<T> inectType, Object object) {
    return new Inject(Preconditions.checkNotNull(inectType), object);
  }

  private Inject(Class inectType, Object object) {
    this.inectType = inectType;
    this.object = object;
  }

  public String getName() {
    return this.inectType.getName();
  }

  public boolean isArray() {
    return this.inectType.isArray();
  }

  public boolean isPrimitive() {
    return this.inectType.isPrimitive();
  }

  public Set<Field> getFields() {
    return FieldUtil.getFields(this.inectType);
  }

  public boolean isInterface() {
    return this.inectType.isInterface();
  }
}
