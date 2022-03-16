package org.magneton.module.auth;

/**
 * .
 *
 * @author zhangmsh 15/03/2022
 * @since 2.0.7
 */
public abstract class AbstractAuth<A, R> implements Auth<A, R> {

	private final Auth<A, R> auth;

	protected AbstractAuth(Auth<A, R> auth) {
		this.auth = auth;
	}

	protected Auth<A, R> getAuth() {
		return this.auth;
	}

}
