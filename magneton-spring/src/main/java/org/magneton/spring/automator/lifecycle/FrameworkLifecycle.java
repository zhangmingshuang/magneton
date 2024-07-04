package org.magneton.spring.automator.lifecycle;

import org.magneton.spring.core.foundation.spi.SPI;
import org.springframework.core.Ordered;

/**
 * 框架应用级的生命周期.
 * <p>
 * 用来处理如启动中、启动完成、关闭中、关闭完成等生命周期事件.
 *
 * @author zhangmsh.
 * @since 2024
 */
@SPI
public interface FrameworkLifecycle extends Ordered {

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