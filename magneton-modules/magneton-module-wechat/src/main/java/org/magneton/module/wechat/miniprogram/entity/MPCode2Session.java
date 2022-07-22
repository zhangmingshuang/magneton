package org.magneton.module.wechat.miniprogram.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zhangmsh 2022/5/1
 * @since 1.0.0
 */
@Setter
@Getter
@ToString
public class MPCode2Session {

	/**
	 * openid string 用户唯一标识
	 */
	private String openid;

	/**
	 * session_key string 会话密钥
	 */
	@JsonProperty("session_key")
	private String sessionKey;

	/**
	 * unionid string 用户在开放平台的唯一标识符，若当前小程序已绑定到微信开放平台帐号下会返回，详见 UnionID 机制说明。
	 */
	private String unionid;

}
