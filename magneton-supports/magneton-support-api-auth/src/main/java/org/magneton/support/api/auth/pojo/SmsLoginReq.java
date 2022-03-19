package org.magneton.support.api.auth.pojo;

import javax.validation.constraints.NotNull;
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
	 * 短信授权码，长度<256
	 */
	@Size(min = 1, max = 256, message = "授权码长度必须在1~256位")
	@NotNull(message = "授权码不能为空")
	private String smsToken;

	/**
	 * 手机号，长度11
	 */
	@Size(min = 11, max = 11, message = "手机号长度必须为11位")
	@NotNull(message = "手机号不能为空")
	private String mobile;

	/**
	 * 标识，用来处理一些安全校验。一把手机应该只有一个唯一的安全标识。长度1~64
	 */
	@Size(min = 1, max = 64, message = "标识长度必须在1~64位")
	@NotNull(message = "标识不能为空")
	private String identification;

	/**
	 * 短信验证码，长度1~10
	 */
	@Size(min = 1, max = 10, message = "验证码长度必须在1~10位")
	@NotNull(message = "验证码不能为空")
	private String smsCode;

}
