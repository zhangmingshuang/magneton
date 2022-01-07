package org.magneton.spy.mysql;

import com.google.auto.service.AutoService;
import org.magneton.spy.constant.DB;
import org.magneton.spy.core.AbstractDatabase;
import org.magneton.spy.core.Database;

/**
 * @author zhangmsh 2021/9/22
 * @since 1.0.0
 */
@AutoService(Database.class)
public class MySqlDatabase extends AbstractDatabase {

	@Override
	public DB db() {
		return DB.MY_SQL;
	}

	@Override
	public Database createDatabase() {
		return new MySqlDatabase();
	}

}
