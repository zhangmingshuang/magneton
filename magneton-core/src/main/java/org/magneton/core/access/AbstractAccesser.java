package org.magneton.core.access;

/**
 * .
 *
 * @author zhangmsh 2021/2/25
 * @since 4.0.0
 */
public abstract class AbstractAccesser implements Accesser {

	protected AccessConfig accessConfig;

	public void afterConfigSet(AccessConfig config) {
		this.accessConfig = config;
		this.initialiazation();
	}

	/** initialization. */
	protected abstract void initialiazation();

}
