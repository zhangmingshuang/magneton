package org.magneton.support.api.auth.pojo;

import javax.annotation.Nonnull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 短信发送请求.
 *
 * @author zhangmsh 15/03/2022
 * @since 2.0.7
 */
@Setter
@Getter
@ToString
public class SmsSendReq {

	/**
	 * 手机号
	 */
	@Size(min = 11, max = 11, message = "手机号长度必须为11位")
	@Nonnull
	private String mobile;

	/**
	 * 风控类型，可选值：code
	 */
	private String riskType;

	/**
	 * 风控值
	 */
	private String riskValue;

}
