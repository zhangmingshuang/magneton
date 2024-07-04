package org.magneton.enhance.wechat.mp.core.message.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.magneton.enhance.wechat.mp.core.message.annotation.OutMsgType;

/**
 * 输出文本消息.
 *
 * @author zhangmsh.
 * @since 1.0.0
 */
@Setter
@Getter
@ToString
@OutMsgType("text")
public class MpOutTextMsg extends MpOutMsg {

	/**
	 * 文本消息内容
	 */
	private String content;

	public static MpOutTextMsg of(String content) {
		MpOutTextMsg mpOutTextMsg = new MpOutTextMsg();
		mpOutTextMsg.setContent(content);
		return mpOutTextMsg;
	}

	@Override
	public WxMpXmlOutMessage toWxMpXmlOutMessage(WxMpXmlMessage inMessage) {
		return WxMpXmlOutMessage.TEXT()
			.content(this.getContent())
			.fromUser(inMessage.getToUser())
			.toUser(inMessage.getFromUser())
			.build();
	}

}