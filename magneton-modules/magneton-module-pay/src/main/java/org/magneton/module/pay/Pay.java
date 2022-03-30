package org.magneton.module.pay;

import org.magneton.core.Consequences;
import org.magneton.core.base.Preconditions;

/**
 * .
 *
 * @author zhangmsh 28/03/2022
 * @since 2.0.7
 */
public interface Pay<T> {

	T actualPay();

	default boolean isType(Class<?> clazz) {
		return this.getClass().isAssignableFrom(Preconditions.checkNotNull(clazz));
	}

	Consequences<PreOrderRes> preOrder(PreOrderReq req);

}
