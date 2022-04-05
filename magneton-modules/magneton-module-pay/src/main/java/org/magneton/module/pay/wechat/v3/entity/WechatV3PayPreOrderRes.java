package org.magneton.module.pay.wechat.v3.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * .
 *
 * @author zhangmsh 28/03/2022
 * @since 2.0.7
 */
@Setter
@Getter
@ToString
public class WechatV3PayPreOrderRes {

	/**
	 * 预支付交易会话标识
	 *
	 * 预支付交易会话标识。用于后续接口调用中使用，该值有效期为2小时 示例值：wx201410272009395522657a690389285100
	 */
	@JsonProperty("prepay_id")
	private String prepayId;

}
