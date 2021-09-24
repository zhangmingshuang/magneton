package org.magneton.spy.core;

/**
 * 求合协议 sum:table_name
 *
 * @author zhangmsh 2021/7/28
 * @since 1.0.0
 */
public interface SumProtocol extends Protocol {

  @Override
  default String name() {
    return "sum";
  }
}
