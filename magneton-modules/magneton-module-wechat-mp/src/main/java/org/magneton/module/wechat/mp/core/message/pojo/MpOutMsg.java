package org.magneton.module.wechat.mp.core.message.pojo;

import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;

import javax.annotation.Nullable;

/**
 * 微信公众号输出消息.
 *
 * @author zhangmsh.
 * @since 2024
 */
public abstract class MpOutMsg {

	@Nullable
	public abstract WxMpXmlOutMessage toWxMpXmlOutMessage(WxMpXmlMessage inMessage);

}