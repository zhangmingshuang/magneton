package org.magneton.core.access;

import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * .
 *
 * @author zhangmsh 2021/2/25
 * @since 4.0.0
 */
class InMemoryRequestAccesserTest {

	private static final int lockErrorCount = 4;

	private static final int lockTime = 2000;

	private static final int lockSize = 1;

	private static final int errorRecordTime = 200;

	private static final int errorRecordSize = 1;

	private static RequestAccesser requestAccesser = null;

	@BeforeAll
	static void beforeAll() {
		requestAccesser = (RequestAccesser) AccesserBuilder.of(new InMemoryRequestAccesser<>()).lockSize(lockSize)
				.lockErrorCount(lockErrorCount).lockTime(lockTime).lockErrorCount(lockErrorCount)
				.errorRecordTime(errorRecordTime).errorRecordSize(errorRecordSize).build();
	}

	@Test
	void locked() {
		String key = UUID.randomUUID().toString();
		this.recordError(key);
		boolean locked = requestAccesser.locked(key);
		Assertions.assertTrue(locked, "must be locker.");
	}

	@Test
	void ttl() {
		Assertions.assertTrue(lockTime > 1000, "lockTime must be more then 1000");
		String key = UUID.randomUUID().toString();
		this.recordError(key);
		long ttl = requestAccesser.ttl(key);
		Assertions.assertTrue(ttl <= lockTime, "lock time error. ttl :" + ttl + ", lockTime: " + lockTime);
	}

	@Test
	void recordError() {
		String key = UUID.randomUUID().toString();
		this.recordError(key);
	}

	@Test
	void access() {
		Assertions.assertTrue(lockErrorCount >= 2, "lockErrorCount must be more then 2.");
		String key = UUID.randomUUID().toString();
		Accessible access = requestAccesser.access(key, () -> "1".equals(key));
		Assertions.assertFalse(access.isAccess(), "must be false");
		Assertions.assertFalse(access.isLocked(), "must be unlocked.");
		for (int i = 1; i < lockErrorCount; i++) {
			access = requestAccesser.access(key, () -> "1".equals(key));
			if (i + 1 < lockErrorCount) {
				Assertions.assertFalse(access.isLocked(), "must be unlocked.");
			}
			else {
				Assertions.assertTrue(access.isLocked(), "must be locked.");
			}
		}
		Assertions.assertTrue(requestAccesser.locked(key), "must be locked");
	}

	private void recordError(String key) {
		for (int i = 0; i < lockErrorCount; i++) {
			int remainingErrorCount = requestAccesser.recordError(key);
			Assertions.assertEquals(remainingErrorCount, lockErrorCount - i - 1, "remaining errors not match.");
		}
	}

}
