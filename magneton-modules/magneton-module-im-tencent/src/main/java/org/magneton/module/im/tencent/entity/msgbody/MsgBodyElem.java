package org.magneton.module.im.tencent.entity.msgbody;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.concurrent.ThreadLocalRandom;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * .
 *
 * @author zhangmsh 2022/4/28
 * @since 2.0.8
 */
public interface MsgBodyElem {

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

	default Body body() {
		Body body = new Body();
		body.setMsgType(this.getMsgType());
		body.setMsgContent(this);
		return body;
	}

	static int random() {
		ThreadLocalRandom current = ThreadLocalRandom.current();
		return current.nextInt(0, Integer.MAX_VALUE);
	}

}
