package org.magneton.module.wechat.open.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zhangmsh 2022/4/3
 * @since 1.0.0
 */
@Setter
@Getter
@ToString
public class MobileAccessTokenReq {

	/**
	 * ERR_OK = 0(用户同意) ERR_AUTH_DENIED = -4（用户拒绝授权） ERR_USER_CANCEL = -2（用户取消）
	 */
	private int ErrCode;

	/**
	 * 用户换取 access_token 的 code，仅在 ErrCode 为 0 时有效
	 */
	private String code;

	/**
	 * 第三方程序发送时用来标识其请求的唯一性的标志，由第三方程序调用 sendReq 时传入，由微信终端回传，state 字符串长度不能超过 1K
	 */
	private String state;

	/**
	 * 微信客户端当前语言
	 */
	private String lang;

	/**
	 * 微信用户当前国家信息
	 */
	private String country;

}
