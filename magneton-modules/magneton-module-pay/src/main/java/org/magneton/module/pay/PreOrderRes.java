package org.magneton.module.pay;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

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
public class PreOrderRes {

	/**
	 * 预支付交易会话标识
	 */
	private String prepayId;

}
