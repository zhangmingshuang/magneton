package org.magneton.module.im.tencent.entity.msgbody;

/**
 * .
 *
 * @author zhangmsh 2022/4/28
 * @since 2.0.8
 */
public enum MsgType {

	/**
	 * 文本消息。
	 */
	TIMTextElem,
	/**
	 * 地理位置消息。
	 */
	TIMLocationElem,
	/**
	 * 表情消息。
	 */
	TIMFaceElem,
	/**
	 * 自定义消息，当接收方为 iOS 系统且应用处在后台时，此消息类型可携带除文本以外的字段到 APNs。一条组合消息中只能包含一个 TIMCustomElem
	 * 自定义消息元素。
	 */
	TIMCustomElem,
	/**
	 * 语音消息。
	 */
	TIMSoundElem,
	/**
	 * 图像消息。
	 */
	TIMImageElem,
	/**
	 * 文件消息。
	 */
	TIMFileElem,
	/**
	 * 视频消息。
	 */
	TIMVideoFileElem;

}
