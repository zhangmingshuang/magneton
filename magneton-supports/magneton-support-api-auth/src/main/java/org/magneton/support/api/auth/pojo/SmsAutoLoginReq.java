package org.magneton.support.api.auth.pojo;

import javax.annotation.Nonnull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * .
 *
 * @author zhangmsh 18/03/2022
 * @since 2.0.7
 */
@Setter
@Getter
@ToString
public class SmsAutoLoginReq {

	/**
	 * 自动登录授权
	 */
	@Nonnull
	@Size(min = 1, max = 256, message = "自动登录授权长度必须在1~256位")
	private String autoLoginToken;

	/**
	 * 标识，用来处理一些安全校验。一把手机应该只有一个唯一的安全标识。长度1~64
	 */
	@Nonnull
	@Size(min = 1, max = 64, message = "标识长度必须在1~64位")
	private String identification;

}
