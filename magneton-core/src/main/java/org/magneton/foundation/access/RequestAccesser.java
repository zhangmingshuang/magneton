package org.magneton.foundation.access;

import java.util.function.Supplier;

/**
 * Accesser operation simply.
 *
 * <pre>{@code
 * RequestAccesser requestAccesser = AccesserBuilder.of(xx).build();
 * String key = "userName";
 * Accessible access = requestAccesser.access(key, () -> "1".equals(key));
 * if (!access.isAccess()){
 *   if (access.isLocked()){
 *     long ttl = access.getTtl();
 *     return "locked, can operation after " + ttl + "ms";
 *   }
 *   int remainingErrorCount = access.getErrorRemainCount();
 *   return "error, " + remainingErrorCount + " operating opportunities";
 * }
 * }</pre>
 *
 * @author zhangmsh 2021/2/25
 * @since 4.0.0
 */
public interface RequestAccesser<T> extends Accesser {

	/**
	 * request accessible judgement.
	 * @param key the key.
	 * @param supplier supply the operation that is compliance or not. if {@code false} is
	 * returned, it will record a error. and lock if the errors out of error count limit
	 * from configuration.
	 * @return the accessible.
	 */
	Accessible access(String key, Supplier<Boolean> supplier);

}
