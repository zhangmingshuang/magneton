package org.magneton.module.wechat.mp.constant;

import lombok.Getter;

import javax.annotation.Nullable;

/**
 * 订阅场景模式.
 *
 * @author zhangmsh.
 * @since 2024
 */
@Getter
public enum MpSubscribeSceneMode {

	/**
	 * 从公众号直接搜索的关注
	 */
	NORMAL(1, "订阅");

	private final int type;

	private final String desc;

	MpSubscribeSceneMode(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	@Nullable
	public static MpSubscribeSceneMode of(int type) {
		for (MpSubscribeSceneMode mode : values()) {
			if (mode.getType() == type) {
				return mode;
			}
		}
		return null;
	}

}