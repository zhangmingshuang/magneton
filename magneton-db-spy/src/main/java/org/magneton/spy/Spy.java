package org.magneton.spy;

import com.google.common.base.Preconditions;
import com.google.common.base.Verify;
import com.google.common.collect.Maps;
import java.util.Locale;
import java.util.Map;
import java.util.ServiceLoader;
import org.magneton.spy.constant.DB;
import org.magneton.spy.core.Database;
import org.magneton.spy.core.DuplicateException;
import org.magneton.spy.core.Protocol;

/**
 * @author zhangmsh 2021/7/28
 * @since 1.0.0
 */
public class Spy {

	private static final Map<String, Protocol> protocols = Maps.newConcurrentMap();

	private static final Map<DB, Database> databases = Maps.newConcurrentMap();

	static {
		ServiceLoader<Protocol> loader = ServiceLoader.load(Protocol.class);
		loader.forEach(protocol -> {
			String name = protocol.name().toUpperCase(Locale.ROOT);
			Protocol exist = protocols.put(name, protocol);
			if (exist != null) {
				throw new DuplicateException(name, protocol.getClass(), exist.getClass());
			}
		});

		ServiceLoader<Database> databaseLoader = ServiceLoader.load(Database.class);
		databaseLoader.forEach(database -> {
			DB db = database.db();
			Database exist = databases.put(db, database);
			if (exist != null) {
				throw new DuplicateException(db.name(), database.getClass(), exist.getClass());
			}
		});
	}

	private Spy() {
	}

	public static Database use(DB db) {
		Database database = databases.get(Preconditions.checkNotNull(db, "db"));
		Verify.verifyNotNull(database, "%s database not found", db.name());
		return database;
	}

}
