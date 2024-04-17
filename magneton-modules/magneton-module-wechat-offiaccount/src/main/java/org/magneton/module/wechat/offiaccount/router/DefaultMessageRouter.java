package org.magneton.module.wechat.offiaccount.router;

import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;
import org.magneton.module.wechat.offiaccount.core.OffiaccountContext;
import org.magneton.module.wechat.offiaccount.handler.MessageHandler;
import org.magneton.module.wechat.offiaccount.handler.MessageProcessor;
import org.magneton.module.wechat.offiaccount.handler.MsgType;

/**
 * 消息路由.
 *
 * @author zhangmsh.
 * @since 2024
 */
public class DefaultMessageRouter implements MessageRouter {

	private final OffiaccountContext offiaccountContext;

	public DefaultMessageRouter(OffiaccountContext offiaccountContext) {
		this.offiaccountContext = offiaccountContext;
	}

	@Override
	public WxMpXmlOutMessage route(WxMpXmlMessage inMessage) {
		// 消息类型
		String msgType = inMessage.getMsgType();
		if (!StringUtils.isBlank(msgType)) {
			MessageHandler msgHandler = MessageRouterRegistry.getMsgHandler();
			if (msgHandler != null) {
				MsgType mt = MsgType.valueOf(msgType.toUpperCase());
				MessageProcessor messageProcessor = mt.getMessageProcessor();
				return messageProcessor.doProcess(msgHandler, inMessage);
			}
		}
		return null;
	}

}