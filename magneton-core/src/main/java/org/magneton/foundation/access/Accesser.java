package org.magneton.foundation.access;

/**
 * .
 *
 * @author zhangmsh 2021/2/25
 * @since 4.0.0
 */
public interface Accesser {

	/**
	 * locked judgment.
	 * @param key the key.
	 * @return {@code true} if locked, or otherwise {@code false}.
	 */
	boolean locked(String key);

	/**
	 * the time to live if locked.
	 * @param key the key.
	 * @return time to live that millisecond. {@code -1} if unlocked.
	 */
	long ttl(String key);

	/**
	 * record a error.
	 * @param key the key.
	 * @return remaining error count. {@code 0} if there are no remaing errors after
	 * locked.
	 */
	int recordError(String key);

}
