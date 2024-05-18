package org.magneton.module.wechat.mp.core.message.mode;

import lombok.Getter;
import org.magneton.module.wechat.mp.core.message.handler.EventPushDispatchProcessor;
import org.magneton.module.wechat.mp.core.message.handler.StandardMsgDispatchProcessor;
import org.magneton.module.wechat.mp.core.router.DispatchProcessor;

/**
 * 模式类型.
 * @author zhangmsh.
 * @since 2024
 */
@Getter
public enum ModeType {

	/**
	 * 普通消息
	 */
	STANDARD_MSG(new StandardMsgDispatchProcessor()),
	/**
	 * 事件推送
	 */
	EVENT(new EventPushDispatchProcessor());

	private final DispatchProcessor dispatchProcessor;

	ModeType(DispatchProcessor dispatchProcessor) {
		this.dispatchProcessor = dispatchProcessor;
	}

}