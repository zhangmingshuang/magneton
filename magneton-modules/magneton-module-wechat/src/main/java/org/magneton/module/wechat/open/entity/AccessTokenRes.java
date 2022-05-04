package org.magneton.module.wechat.open.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zhangmsh 2022/4/2
 * @since 1.0.0
 */
@Setter
@Getter
@ToString
public class AccessTokenRes {

	/**
	 * 接口调用凭证
	 */
	private String access_token;

	/**
	 * access_token接口调用凭证超时时间，单位（秒）
	 */
	private int expires_in;

	/**
	 * 用户刷新access_token
	 */
	private String refresh_token;

	/**
	 * 授权用户唯一标识
	 */
	private String openid;

	/**
	 * 用户授权的作用域，使用逗号（,）分隔
	 */
	private String scope;

}
