package org.magneton.module.wechat.miniprogram.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <a href=
 * "https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/access-token/auth.getAccessToken.html">官方文档</a>
 *
 * @author zhangmsh 2022/4/2
 * @since 1.0.0
 */
@Setter
@Getter
@ToString
public class MPAccessToken {

	/**
	 * 接口调用凭证
	 */
	private String access_token;

	/**
	 * access_token接口调用凭证超时时间，单位（秒）
	 */
	private int expires_in;

}
