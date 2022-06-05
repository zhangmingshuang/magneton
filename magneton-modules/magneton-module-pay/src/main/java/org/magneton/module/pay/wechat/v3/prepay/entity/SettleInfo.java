package org.magneton.module.pay.wechat.v3.prepay.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Setter
@Getter
@ToString
@Accessors(chain = true)
// 结算信息
public class SettleInfo {

	/**
	 * 是否指定分账 是否指定分账 示例值：false
	 **/
	@JsonProperty("profit_sharing")
	private boolean profitSharing;

}