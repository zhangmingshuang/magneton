package org.magneton.module.pay.wechat.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.magneton.module.pay.wechat.api._WechatApiPreOrderRes;

/**
 * .
 *
 * @author zhangmsh 28/03/2022
 * @since 2.0.7
 */
@Setter
@Getter
@ToString
@Accessors(chain = true)
public class WechatPreOrderRes {

	private _WechatApiPreOrderRes target;

	/**
	 * 预支付交易会话标识
	 */
	private String prepayId;

}
