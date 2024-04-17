package org.magneton.module.wechat.offiaccount.handler;

import lombok.Getter;

/**
 * 基础消息能
 * @author zhangmsh.
 * @since 1.0.0
 */
@Getter
public enum MsgType {

	/**
	 * 普通消息.
	 */
	TEXT("wechat_msg_text", new TextMessageProcessor()),

	/**
	 * 接收事件推送
	 */
	EVENT("wechat_msg_event", new EventMessageProcessor());

	private final String code;

	private final MessageProcessor messageProcessor;

	MsgType(String code, MessageProcessor messageProcessor) {
		this.code = code;
		this.messageProcessor = messageProcessor;
	}

}