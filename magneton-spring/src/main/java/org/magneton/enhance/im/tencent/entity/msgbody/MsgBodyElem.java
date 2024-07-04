package org.magneton.enhance.im.tencent.entity.msgbody;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.concurrent.ThreadLocalRandom;

/**
 * .
 *
 * @author zhangmsh 2022/4/28
 * @since 2.0.8
 */
public interface MsgBodyElem {

	/**
	 * 消息类型.
	 * @return 消息类型
	 */
	@JsonIgnore
	String getMsgType();

	@Setter
	@Getter
	@ToString
	public static class Body {

		@JsonProperty("MsgType")
		protected String msgType;

		@JsonProperty("MsgContent")
		protected Object msgContent;

	}

	/**
	 * 消息体.
	 * @return 消息体
	 */
	default Body body() {
		Body body = new Body();
		body.setMsgType(this.getMsgType());
		body.setMsgContent(this);
		return body;
	}

	/**
	 * 随机数.
	 * @return 随机数
	 */
	static int random() {
		ThreadLocalRandom current = ThreadLocalRandom.current();
		return current.nextInt(0, Integer.MAX_VALUE);
	}

}