package org.magneton.framework;

import org.magneton.foundation.spi.SPI;

/**
 * 框架应用.
 *
 * @author zhangmsh.
 * @since 2024
 */
@SPI
public interface FrameworkApplication extends Ordered {

	/**
	 * 获取排序.
	 * @return 排序
	 */
	@Override
	default int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE + 1000;
	}

	/**
	 * 启动中
	 */
	void starting();

}