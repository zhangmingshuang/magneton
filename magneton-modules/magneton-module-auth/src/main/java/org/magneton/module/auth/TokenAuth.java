package org.magneton.module.auth;

import org.magneton.core.Response;

/**
 * .
 *
 * @author zhangmsh 15/03/2022
 * @since 2.0.7
 */
public class TokenAuth<A> extends AbstractAuth<A, String> {

	protected TokenAuth(Auth<A, String> auth) {
		super(auth);
	}

	@Override
	public Response<String> accredit(A accredit) {
		return null;
	}

}
