package org.magneton.enhance.wechat.mp.core.message.handler;

import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.magneton.enhance.wechat.mp.core.message.mode.Mode;
import org.magneton.enhance.wechat.mp.core.message.pojo.MpInTextMsg;
import org.magneton.enhance.wechat.mp.core.message.pojo.MpOutMsg;
import org.magneton.enhance.wechat.mp.core.router.DispatchProcessor;

import java.util.Map;

/**
 * 文本消息处理
 *
 * @author zhangmsh
 * @since 2024
 */
public class StandardMsgDispatchProcessor implements DispatchProcessor {

	@Override
	public WxMpXmlOutMessage doProcess(String appid, Mode mode, MpHandler msgHandler, WxMpXmlMessage inMessage) {
		Map<String, Object> allFieldsMap = inMessage.getAllFieldsMap();

		MpInTextMsg mpInTextMsg = new MpInTextMsg();
		mpInTextMsg.setAppId(appid);
		mpInTextMsg.setMsgId(inMessage.getMsgId());
		mpInTextMsg.setMsgDataId((String) allFieldsMap.get("MsgDataId"));
		mpInTextMsg.setIdx((String) allFieldsMap.get("Idx"));
		mpInTextMsg.setFromUser(inMessage.getFromUser());
		mpInTextMsg.setToUser(inMessage.getToUser());
		mpInTextMsg.setContent(inMessage.getContent());
		mpInTextMsg.setCreateTime(inMessage.getCreateTime());

		MpOutMsg mpOutMsg = ((MpStandardMsgHandler) msgHandler).onTextMsg(mpInTextMsg);
		if (mpOutMsg == null) {
			return null;
		}
		return mpOutMsg.toWxMpXmlOutMessage(inMessage);
	}

}