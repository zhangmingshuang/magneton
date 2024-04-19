package org.magneton.module.wechat.mp.core.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 输出文本消息.
 *
 * @author zhangmsh.
 * @since 1.0.0
 */
@Setter
@Getter
@ToString
public class MpOutTextMsg {

	/**
	 * 文本消息内容
	 */
	private String content;

	public static MpOutTextMsg of(String content) {
		MpOutTextMsg mpOutTextMsg = new MpOutTextMsg();
		mpOutTextMsg.setContent(content);
		return mpOutTextMsg;
	}

}