package org.magneton.test.injector.processor.statement;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.magneton.test.injector.Inject;
import org.magneton.test.util.NumberUtil;

/**
 * .
 *
 * @author zhangmsh 2021/8/3
 * @since 2.0.0
 */
@Setter
@Getter
@ToString
@Slf4j
public class DataStatement extends HashMap<String, Object> {

  private static final long serialVersionUID = -3867248213414675636L;

  private Object value;

  private Inject inject;

  private Map<String, Object> metadata;

  public DataStatement(Inject inject, @Nullable Object value) {
    this.inject = inject;
    this.value = value;
  }

  @Nullable
  public Object getValue() {
    if (NumberUtil.isNumberType(this.inject.getInectType())) {
      return NumberUtil.cast(this.inject.getInectType(), (Number) this.value);
    }
    return this.value;
  }

  public void addMetadata(String key, Object value) {
    Preconditions.checkNotNull(key);
    Preconditions.checkNotNull(value);
    if (this.metadata == null) {
      this.metadata = Maps.newHashMap();
    }
    this.metadata.put(key, value);
  }

  @Nullable
  public Object getMetadata(String key) {
    if (this.metadata == null) {
      return null;
    }
    return this.metadata.get(key);
  }

  public void breakNext(@Nullable Object value) {
    this.put("next", false);
    this.setValue(value);
  }

  public boolean isNext() {
    return Optional.ofNullable((Boolean) this.get("next")).orElse(Boolean.TRUE);
  }
}
