package org.magneton.enhance.wechat.mp.core.message.mode;

import com.google.auto.service.AutoService;
import com.google.common.base.Strings;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;

/**
 * 普通消息解析器.
 *
 * @author zhangmsh.
 * @since 2024
 */
@AutoService(ModeParser.class)
public class MpModeParser implements ModeParser {

	@Override
	public <T extends Mode> T parse(WxMpXmlMessage inMessage) {
		T msgMode = null;
		if (!Strings.isNullOrEmpty(inMessage.getMsgType())) {
			msgMode = this.parseMsgType(inMessage);
		}
		return msgMode;
	}

	@SuppressWarnings("unchecked")
	private <T extends Mode> T parseMsgType(WxMpXmlMessage inMessage) {
		if (EventPushMode.MSG_TYPE.equalsIgnoreCase(inMessage.getMsgType())) {
			EventPushMode eventPushMode = EventPushMode.of(inMessage.getEvent(), inMessage.getEventKey());
			if (eventPushMode != null) {
				return (T) eventPushMode;
			}
		}
		StandardMsgMode standardMsgMode = StandardMsgMode.of(inMessage.getMsgType());
		if (standardMsgMode != null) {
			return (T) standardMsgMode;
		}
		return null;
	}

}