package org.magneton.module.safedog.access;

/**
 * Time calculator.
 *
 * @author zhangmsh.
 * @since 2023.1
 */
public class DefaultAccessTimeCalculator implements AccessTimeCalculator {

	@Override
	public long calculate(String name, int wrongs, AccessConfig accessConfig) {
		return System.currentTimeMillis() + accessConfig.getLockTime();
	}

}
