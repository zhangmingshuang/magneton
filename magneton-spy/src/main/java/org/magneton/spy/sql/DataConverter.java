package org.magneton.spy.sql;

import java.util.List;

/**
 * @author zhangmsh 2021/9/22
 * @since 1.0.0
 */
public interface DataConverter {

  <T> T to(Class clazz);

  <T> List<T> toList(Class<T> clazz);
}
