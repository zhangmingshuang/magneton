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
public class SmsLoginReq {

	/**
	 * 短信授权码
	 */
	@Size(max = 1024)
	@Nonnull
	private String smsToken;

	/**
	 * 手机号
	 */
	@Size(min = 11, max = 11, message = "手机号长度必须为11位")
	@Nonnull
	private String mobile;

	/**
	 * 短信验证码
	 */
	@Size(max = 10)
	@Nonnull
	private String smsCode;

}
