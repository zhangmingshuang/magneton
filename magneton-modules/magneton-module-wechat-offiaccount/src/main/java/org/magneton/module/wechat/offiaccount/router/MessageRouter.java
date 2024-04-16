package org.magneton.module.wechat.offiaccount.router;

import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;

import javax.annotation.Nullable;

/**
 * 消息路由.
 *
 * @author zhangmsh.
 * @since 2024
 */
public interface MessageRouter {

	/**
	 * 路由消息.
	 * @param inMessage 输入消息
	 * @return 输出消息
	 */
	@Nullable
	WxMpXmlOutMessage route(WxMpXmlMessage inMessage);

}