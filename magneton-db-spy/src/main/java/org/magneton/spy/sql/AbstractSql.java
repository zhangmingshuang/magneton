package org.magneton.spy.sql;

/**
 * @author zhangmsh 2021/9/22
 * @since 1.0.0
 */
public abstract class AbstractSql implements Sql {

	private SqlContext sqlContext = new SqlContext();

	public void setSqlContext(SqlContext sqlContext) {
		this.sqlContext = sqlContext;
	}

	@Override
	public Select select(String table) {
		return new Select(table, this.sqlContext);
	}

}
