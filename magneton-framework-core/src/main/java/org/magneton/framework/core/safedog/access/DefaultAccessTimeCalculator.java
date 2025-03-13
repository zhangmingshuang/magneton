package org.magneton.framework.core.safedog.access;

/**
 * Time calculator.
 *
 * @author zhangmsh.
 * @since 2023.1
 */
public class DefaultAccessTimeCalculator implements AccessTimeCalculator {

	@Override
	public long calculate(String name, long wrongs, AccessConfig accessConfig) {
		return accessConfig.getLockTime();
	}

}
