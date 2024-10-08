package org.magneton.enhance.wechat.mp.core.message.mode;

import org.magneton.enhance.wechat.mp.core.message.handler.MpHandler;

/**
 * 模式.
 *
 * @author zhangmsh.
 * @since 2024
 */
public interface Mode {

	/**
	 * 获取模式类型.
	 * @return 模式类型
	 */
	ModeType modeType();

	/**
	 * 获取处理器类型.
	 * @return 处理器类型
	 */
	Class<? extends MpHandler> handlerType();

}