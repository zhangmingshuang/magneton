package org.magneton.module.wechat.mp.constant;

import lombok.Getter;

/**
 * 素材类型.
 * @author zhangmsh.
 * @since 2024
 */
@Getter
public enum MpMaterialType {

	/**
	 * 图文
	 */
	NEWS(1, "图文"),
	/**
	 * 图片
	 */
	IMAGE(2, "图片"),
	/**
	 * 语音
	 */
	VOICE(3, "语音"),
	/**
	 * 视频
	 */
	VIDEO(4, "视频");

	private final int type;

	private final String desc;

	MpMaterialType(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}

}