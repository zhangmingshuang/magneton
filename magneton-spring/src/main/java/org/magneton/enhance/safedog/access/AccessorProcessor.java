package org.magneton.enhance.safedog.access;

/**
 * Accessor builder.
 *
 * @author zhangmsh.
 * @since 2023.1
 */
public interface AccessorProcessor {

	/**
	 * 设置访问配置
	 * @param accessConfig 访问配置
	 */
	void setAccessConfig(AccessConfig accessConfig);

	/**
	 * 创建访问器
	 * @param name 访问器名称
	 * @return 访问器
	 */
	Accessor create(String name);

}
