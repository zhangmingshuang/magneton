package org.magneton.module.wechat.offiaccount.handler;

import lombok.Getter;

/**
 * 消息类型.
 * @author zhangmsh.
 * @since 1.0.0
 */
@Getter
public enum MsgType {

	/**
	 * 普通消息.
	 */
	TEXT("wechat_msg_text", new MessageProcessor.TextMessageProcessor());

	private final String code;

	private final MessageProcessor messageProcessor;

	MsgType(String code, MessageProcessor messageProcessor) {
		this.code = code;
		this.messageProcessor = messageProcessor;
	}

}