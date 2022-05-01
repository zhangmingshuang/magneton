package org.magneton.module.im.tencent.entity.msgbody;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 表情消息元素.
 *
 * 当接收方为 iOS 或 Android，且应用处在后台时，中文版离线推送文本为“[表情]”，英文版离线推送文本为“[Face]”。
 *
 * 说明 当消息中只有一个 TIMCustomElem 自定义消息元素时，如果 Desc 字段和 OfflinePushInfo.Desc
 * 字段都不填写，将收不到该条消息的离线推送，需要填写 OfflinePushInfo.Desc 字段才能收到该消息的离线推送。
 *
 * @author zhangmsh 2022/4/30
 * @since 2.0.8
 */
@Setter
@Getter
@ToString
@Accessors(chain = true)
public class FaceElem implements MsgBodyElem {

	/** Index Number 表情索引，用户自定义。 */
	@JsonProperty("Index")
	private Integer index;

	/** Data String 额外数据。 */
	@JsonProperty("Data")
	private String data;

	@Override
	public String getMsgType() {
		return "TIMFaceElem";
	}

}
