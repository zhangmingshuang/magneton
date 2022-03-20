package org.magneton.module.safedog;

import java.util.Map;
import javax.annotation.Nullable;

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
	 * @return 数据标识
	 */
	default String sign(Map<String, String> data) {
		return this.sign(data, null);
	}

	String sign(Map<String, String> data, @Nullable String salt);

	/**
	 * 判断数据标识是否一至
	 * @param sign 数据标识
	 * @param data 数据
	 * @return 是否一至
	 */
	default boolean validate(String sign, Map<String, String> data) {
		return this.validate(sign, 0, data, null);
	}

	/**
	 * 判断数据标识是否一至
	 * @param sign 数据标识
	 * @param signPeriodSeconds 签名周期，在这个周期时间内，同一个签名会认为是无效的。如果小于1表示不处理该逻辑。
	 * @param data 数据
	 * @return 是否一至
	 */
	boolean validate(String sign, int signPeriodSeconds, Map<String, String> data, @Nullable String salt);

}
