package org.magneton.spring.starter.condiation;

import lombok.Getter;

/**
 * 模块条件.
 * @author zhangmsh.
 * @since 2023.9
 */
@Getter
public enum Modules {

	/**
	 * Redisson能力模块
	 */
	REDISSON("org.magneont.module.redisson");

	private final String module;

	Modules(String module) {
		this.module = module;
	}

}
