package org.magneton.module.wechat.offiaccount.handler;

import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.magneton.module.wechat.offiaccount.pojo.InputTextMsg;
import org.magneton.module.wechat.offiaccount.pojo.OutputTextMsg;

import java.util.Map;

public class TextMessageProcessor implements MessageProcessor {

	@Override
	public WxMpXmlOutMessage doProcess(MessageHandler msgHandler, WxMpXmlMessage inMessage) {
		Map<String, Object> allFieldsMap = inMessage.getAllFieldsMap();

		InputTextMsg inputTextMsg = new InputTextMsg();
		inputTextMsg.setMsgId(inMessage.getMsgId());
		inputTextMsg.setMsgDataId((String) allFieldsMap.get("MsgDataId"));
		inputTextMsg.setIdx((String) allFieldsMap.get("Idx"));
		inputTextMsg.setFromUserName(inMessage.getFromUser());
		inputTextMsg.setToUserName(inMessage.getToUser());
		inputTextMsg.setContent(inMessage.getContent());
		inputTextMsg.setCreateTime(inMessage.getCreateTime());

		OutputTextMsg outputTextMsg = msgHandler.onTextMsg(inputTextMsg);
		if (outputTextMsg == null) {
			return null;
		}
		return WxMpXmlOutMessage.TEXT().content(outputTextMsg.getContent()).fromUser(inMessage.getToUser())
				.toUser(inMessage.getFromUser()).build();
	}

}