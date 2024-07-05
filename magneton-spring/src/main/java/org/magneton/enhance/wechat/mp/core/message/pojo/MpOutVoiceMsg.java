package org.magneton.enhance.wechat.mp.core.message.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.magneton.enhance.wechat.mp.core.message.annotation.OutMsgType;

import javax.annotation.Nullable;

/**
 * 输出语音 消息.
 *
 * @author zhangmsh.
 * @since 2024
 */
@Setter
@Getter
@ToString
@OutMsgType("video")
public class MpOutVoiceMsg extends MpOutMsg {

	/**
	 * 通过素材管理中的接口上传多媒体文件，得到的id
	 */
	private String mediaId;

	public static MpOutVoiceMsg of(String mediaId) {
		MpOutVoiceMsg mpOutVoiceMsg = new MpOutVoiceMsg();
		mpOutVoiceMsg.setMediaId(mediaId);
		return mpOutVoiceMsg;
	}

	@Nullable
	@Override
	public WxMpXmlOutMessage toWxMpXmlOutMessage(WxMpXmlMessage inMessage) {
		return WxMpXmlOutMessage.VOICE().mediaId(this.getMediaId()).fromUser(inMessage.getToUser())
				.toUser(inMessage.getFromUser()).build();
	}

}