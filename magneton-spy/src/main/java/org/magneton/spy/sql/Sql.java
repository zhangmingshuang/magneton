package org.magneton.spy.sql;

/**
 * @author zhangmsh 2021/9/22
 * @since 1.0.0
 */
public interface Sql {

  Select select(String table);
}
