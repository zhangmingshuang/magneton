package org.magneton.module.sms.entity;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class SmsToken {

	/**
	 * 短信发送Token
	 */
	private String token;

	/**
	 * Token对应的验证码
	 */
	private String code;

}
