package org.magneton.spring.launcher;

import org.magneton.foundation.annotations.SpringComponentSupport;
import org.magneton.foundation.spi.SPI;

/**
 * 启动执行器.
 *
 * 用来在项目启动时执行一些动作。
 *
 * 该启动执行器分为两种阶段执行。
 * <ul>
 * <li>Spring启动时通过SPI直接加载执行，该阶段的执行时期比Spring优先</li>
 * <li>Spring启动完成之后加载Bean执行，该阶段的执行时期需要等待Spring启动完成之后</li>
 * </ul>
 *
 * @author zhangmsh 2022/2/7
 * @since 1.2.0
 */
@SPI
@SpringComponentSupport
public interface RunLauncher {

	void launch();

}
