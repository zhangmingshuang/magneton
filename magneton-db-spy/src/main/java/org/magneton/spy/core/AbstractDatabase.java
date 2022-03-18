package org.magneton.spy.core;

import org.magneton.core.base.Verify;
import org.magneton.spy.sql.Select;

/**
 * @author zhangmsh 2021/9/22
 * @since 1.0.0
 */
public abstract class AbstractDatabase implements Database {

	private static final ThreadLocal<Database> THREAD_DATABASES = new ThreadLocal<>();

	private Select select;

	public Database self() {
		Database database = THREAD_DATABASES.get();
		if (database == null) {
			database = this.createDatabase();
			Verify.verifyNotNull(database, "database");
			THREAD_DATABASES.set(database);
		}
		return database;
	}

	@Override
	public Select select() {
		if (this.select == null) {
			this.select = this.createSelect();
		}
		return this.select;
	}

	protected Select createSelect() {
		return null;
	}

	public abstract Database createDatabase();

}
