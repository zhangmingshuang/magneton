package org.magneton.spy.sql;

import java.util.List;

/**
 * @author zhangmsh 2021/9/22
 * @since 1.0.0
 */
public class JsonDataConverter implements DataConverter {

  public JsonDataConverter(SqlContext sqlContext) {}

  @Override
  public <T> T to(Class clazz) {
    return null;
  }

  @Override
  public <T> List<T> toList(Class<T> clazz) {
    return null;
  }
}
