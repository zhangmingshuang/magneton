package org.magneton.enhance.wechat.mp.core.router;

import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.magneton.enhance.wechat.mp.core.MpContext;
import org.magneton.enhance.wechat.mp.core.message.handler.MpHandler;
import org.magneton.enhance.wechat.mp.core.message.mode.Mode;

/**
 * 消息助手.
 *
 * @author zhangmsh.
 * @since 2024
 */
public interface DispatchProcessor {

	/**
	 * 处理消息.
	 * @param mode 消息模式
	 * @param msgHandler 消息处理器
	 * @param inMessage 输入消息
	 * @return 输出消息
	 */
	WxMpXmlOutMessage doProcess(String appid, Mode mode, MpHandler msgHandler, WxMpXmlMessage inMessage);

	default WxMpXmlOutMessage process(Mode msgMode, MpHandler msgHandler, WxMpXmlMessage inMessage) {
		return this.doProcess(MpContext.currentAppid(), msgMode, msgHandler, inMessage);
	}

}