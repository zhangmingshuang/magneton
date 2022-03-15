package org.magneton.support.basic.service;

import javax.annotation.Nullable;

/**
 * .
 *
 * @author zhangmsh 2022/2/16
 * @since 1.2.0
 */
public interface BasicSysConfigService {

	/**
	 * 获取某个配置的值
	 * @param key 配置Key
	 * @return 配置Key对应的值，如果没有找到则返回 {@code null}
	 */
	@Nullable
	String getValue(String key);

}
