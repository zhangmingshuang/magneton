package org.magneton.spy.sql;

import com.google.common.base.Preconditions;
import lombok.ToString;

/**
 * @author zhangmsh 2021/9/22
 * @since 1.0.0
 */
@ToString
public class Select implements Executable {

  private final String table;
  private final SqlContext sqlContext;

  public Select(String table, SqlContext sqlContext) {
    this.table = Preconditions.checkNotNull(table, "table");
    this.sqlContext = Preconditions.checkNotNull(sqlContext, "sqlContext");
  }

  @Override
  public ResultSet exec() {
    Object object = this.sqlContext.getSqlExecutor().exec(this);
    return new ResultSet(this.sqlContext.getDataConverter(), object);
  }
}
