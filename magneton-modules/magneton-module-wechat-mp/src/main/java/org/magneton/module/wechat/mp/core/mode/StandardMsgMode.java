package org.magneton.module.wechat.mp.core.mode;

import lombok.Getter;
import org.magneton.module.wechat.mp.core.handler.MpHandler;
import org.magneton.module.wechat.mp.core.handler.MpStandardMsgHandler;

import javax.annotation.Nullable;

/**
 * 普通消息
 * @author zhangmsh.
 * @since 2024
 */
@Getter
public enum StandardMsgMode implements Mode {

	/**
	 * 普通文本消息
	 */
	TEXT("text"),
	/**
	 * 图片消息
	 */
	IMAGE("image"),
	/**
	 * 语音消息
	 */
	VOICE("voice"),
	/**
	 * 视频消息
	 */
	VIDEO("video"),
	/**
	 * 小视频消息
	 */
	SHORTVIDEO("shortvideo"),
	/**
	 * 地理位置消息
	 */
	LOCATION("location"),
	/**
	 * 链接消息
	 */
	LINK("link");

	private final String mode;

	StandardMsgMode(String mode) {
		this.mode = mode;
	}

	@Override
	public ModeType modeType() {
		return ModeType.STANDARD_MSG;
	}

	@Override
	public Class<? extends MpHandler> handlerType() {
		return MpStandardMsgHandler.class;
	}

	@Nullable
	public static StandardMsgMode of(@Nullable String mode) {
		for (StandardMsgMode value : values()) {
			if (value.getMode().equalsIgnoreCase(mode)) {
				return value;
			}
		}
		return null;
	}

}