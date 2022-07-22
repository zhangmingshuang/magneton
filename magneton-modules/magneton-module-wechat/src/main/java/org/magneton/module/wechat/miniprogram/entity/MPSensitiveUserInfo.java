package org.magneton.module.wechat.miniprogram.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zhangmsh 2022/7/22
 * @since 1.0.1
 */
@Setter
@Getter
@ToString
public class MPSensitiveUserInfo {

	/**
	 * 用户昵称
	 */
	private String nickName;

	/**
	 * 用户头像图片的 URL。URL 最后一个数值代表正方形头像大小（有 0、46、64、96、132 数值可选，0 代表 640x640 的正方形头像，
	 *
	 * 46 表示 46x46 的正方形头像，剩余数值以此类推。默认132），用户没有头像时该项为空。
	 *
	 * 若用户更换头像，原有头像 URL 将失效。
	 */
	private String avatarUrl;

	/**
	 * 显示 country，province，city 所用的语言。强制返回 “zh_CN”
	 */
	private String language;

	/**
	 * openId string 用户唯一标识
	 */
	private String openId;

	/**
	 * unionid string 用户在开放平台的唯一标识符，若当前小程序已绑定到微信开放平台帐号下会返回，详见 UnionID 机制说明。
	 */
	private String unionId;

	private Watermark watermark;

}
