package org.magneton.module.safedog.sign;

import org.magneton.core.Result;

/**
 * 签名校验.
 *
 * @author zhangmsh.
 * @since 2023.9
 */
public interface SignValidator {

	/**
	 * 签名校验
	 * @param expectedSign 签名，预期的签名
	 * @param actualSign 实际的签名
	 * @param signPeriod 签名有效期，单位秒。如果设置的值小于 {@code 1}，则不需要处理该逻辑。
	 * @return 校验结果
	 */
	Result<Void> validate(String expectedSign, String actualSign, int signPeriod);

}