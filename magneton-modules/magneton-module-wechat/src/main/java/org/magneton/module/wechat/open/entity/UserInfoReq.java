package org.magneton.module.wechat.open.entity;

import javax.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author zhangmsh 2022/4/3
 * @since 1.0.0
 */
@Setter
@Getter
@ToString
@Accessors(chain = true)
public class UserInfoReq {

	/**
	 * 调用凭证
	 */
	private String access_token;

	/**
	 * 普通用户的标识，对当前开发者帐号唯一
	 */
	private String openid;

	/**
	 * 国家地区语言版本，zh_CN 简体，zh_TW 繁体，en 英语，默认为en
	 */
	@Nullable
	private String lang;

}
