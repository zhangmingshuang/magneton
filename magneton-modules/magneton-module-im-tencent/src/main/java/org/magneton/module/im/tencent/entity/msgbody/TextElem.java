package org.magneton.module.im.tencent.entity.msgbody;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 文本消息元素.
 *
 * 当接收方为 iOS 或 Android，且应用处在后台时，JSON 请求包体中的 Text 字段作为离线推送的文本展示。
 *
 * @author zhangmsh 2022/4/28
 * @since 2.0.8
 */
@Setter
@Getter
@ToString
public class TextElem implements MsgBodyElem {

	/**
	 * Text String 消息内容。当接收方为 iOS 或 Android 后台在线时，作为离线推送的文本展示。
	 */
	@JsonProperty("Text")
	private String text;

	@Override
	public String getMsgType() {
		return "TIMTextElem";
	}

}
