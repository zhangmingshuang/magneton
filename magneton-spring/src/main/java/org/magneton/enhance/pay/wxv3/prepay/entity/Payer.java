package org.magneton.enhance.pay.wxv3.prepay.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zhangmsh 2022/4/6
 * @since 1.0.0
 */
@Setter
@Getter
@ToString
public class Payer {

	/**
	 * 用户标识 openid string[1,128] 是 用户在直连商户appid下的唯一标识。 示例值：oUpF8uMuAJO_M2pxb1Q9zNjWeS6o
	 **/
	private String openid;

}
