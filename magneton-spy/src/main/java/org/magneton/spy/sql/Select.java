package org.magneton.spy.sql;

import lombok.ToString;
import org.magneton.core.base.Preconditions;

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
		Object object = sqlContext.getSqlExecutor().exec(this);
		return new ResultSet(sqlContext.getDataConverter(), object);
	}

}
