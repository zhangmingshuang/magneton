package org.magneton.module.auth;

import org.magneton.core.Response;

/**
 * .
 *
 * @author zhangmsh 15/03/2022
 * @since 2.0.7
 */
public interface Auth<A, R> {

	/**
	 * 授权
	 * @param accredit 授权信息
	 * @return 授权数据
	 */
	Response<R> accredit(A accredit);

}
