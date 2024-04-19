package org.magneton.module.wechat.mp.core.handler;

import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.magneton.module.wechat.mp.core.mode.Mode;
import org.magneton.module.wechat.mp.core.pojo.MpInTextMsg;
import org.magneton.module.wechat.mp.core.pojo.MpOutTextMsg;
import org.magneton.module.wechat.mp.core.router.DispatchProcessor;

import java.util.Map;

/**
 * 文本消息处理
 *
 * @author zhangmsh
 * @since 2024
 */
public class StandardMsgDispatchProcessor implements DispatchProcessor {

	@Override
	public WxMpXmlOutMessage doProcess(Mode mode, MpHandler msgHandler, WxMpXmlMessage inMessage) {
		Map<String, Object> allFieldsMap = inMessage.getAllFieldsMap();

		MpInTextMsg mpInTextMsg = new MpInTextMsg();
		mpInTextMsg.setMsgId(inMessage.getMsgId());
		mpInTextMsg.setMsgDataId((String) allFieldsMap.get("MsgDataId"));
		mpInTextMsg.setIdx((String) allFieldsMap.get("Idx"));
		mpInTextMsg.setFromUser(inMessage.getFromUser());
		mpInTextMsg.setToUser(inMessage.getToUser());
		mpInTextMsg.setContent(inMessage.getContent());
		mpInTextMsg.setCreateTime(inMessage.getCreateTime());

		MpOutTextMsg mpOutTextMsg = ((MpStandardMsgHandler) msgHandler).onTextMsg(mpInTextMsg);
		if (mpOutTextMsg == null) {
			return null;
		}
		return WxMpXmlOutMessage.TEXT().content(mpOutTextMsg.getContent()).fromUser(inMessage.getToUser())
				.toUser(inMessage.getFromUser()).build();
	}

}