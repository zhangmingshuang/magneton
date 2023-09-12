package org.magneton.module.safedog;

import org.magneton.core.Result;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * 签名安全狗
 *
 * @author zhangmsh 2022/3/20
 * @since 1.0.0
 */
public interface SignSafeDog {

	/**
	 * 生成数据标识
	 * @param data 数据
	 * @return 数据签名标识
	 */
	default String sign(Map<String, String> data) {
		return this.sign(data, null);
	}

	/**
	 * 生成数据标识
	 * @param data 数据
	 * @param salt 盐
	 * @return 数据签名标识
	 */
	String sign(Map<String, String> data, @Nullable String salt);

	/**
	 * 判断数据标识是否一至
	 * @param sign 数据标识
	 * @param data 数据
	 * @return 是否一至， {@code true} 一至，{@code false} 不一至
	 */
	default Result<Boolean> validate(String sign, Map<String, String> data) {
		return this.validate(sign, data, null);
	}

	/**
	 * 判断数据标识是否一至
	 * @param sign 数据标识
	 * @param data 数据
	 * @return 是否一至， {@code true} 一至，{@code false} 不一至
	 */
	Result<Boolean> validate(String sign, Map<String, String> data, @Nullable String salt);

}
