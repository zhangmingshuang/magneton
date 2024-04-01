package org.magneton.module.safedog;

import org.magneton.core.Result;

/**
 * 行为验证器.
 *
 * @author zhangmsh 09/04/2022
 * @since 2.0.8
 */
public interface BehaviorValidator<T> {

	/**
	 * 验证行为.
	 * @param body 行为数据
	 * @return true: 验证通过，false: 验证失败
	 */
	Result<Void> validate(T body);

}