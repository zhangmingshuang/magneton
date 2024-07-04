package org.magneton.enhance.wechat.mp.core.router;

import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;

import javax.annotation.Nullable;

/**
 * 消息路由.
 *
 * @author zhangmsh.
 * @since 2024
 */
public interface DispatchRouter {

	/**
	 * 路由消息.
	 * @param appid 公众号appid
	 * @param inMessage 输入消息
	 * @return 输出消息
	 */
	@Nullable
	WxMpXmlOutMessage dispatch(WxMpXmlMessage inMessage);

}