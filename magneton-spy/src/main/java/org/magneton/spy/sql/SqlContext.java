package org.magneton.spy.sql;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zhangmsh 2021/9/22
 * @since 1.0.0
 */
@Setter
@Getter
@ToString
public class SqlContext {
  private SqlExecutor sqlExecutor = new JdbcTemplateSqlExecutor(this);
  private DataConverter dataConverter = new JsonDataConverter(this);
}
