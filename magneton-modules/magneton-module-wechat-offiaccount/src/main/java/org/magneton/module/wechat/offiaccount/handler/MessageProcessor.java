package org.magneton.module.wechat.offiaccount.handler;

import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;

/**
 * 消息助手.
 *
 * @author zhangmsh.
 * @since 2024
 */
public interface MessageProcessor {

	/**
	 * 处理消息.
	 * @param msgHandler 消息处理器
	 * @param inMessage 输入消息
	 * @return 输出消息
	 */
	WxMpXmlOutMessage doProcess(MessageHandler msgHandler, WxMpXmlMessage inMessage);

}