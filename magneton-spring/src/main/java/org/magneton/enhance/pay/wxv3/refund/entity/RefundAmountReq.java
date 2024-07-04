package org.magneton.enhance.pay.wxv3.refund.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author zhangmsh 2022/7/7
 * @since 1.0.0
 */
@Setter
@Getter
@ToString
public class RefundAmountReq {

	/**
	 * 退款金额 refund int 是 退款金额，单位为分，只能为整数，不能超过原订单支付金额。 示例值：888
	 */
	private int refund;

	/**
	 * 退款需要从指定账户出资时，传递此参数指定出资金额（币种的最小单位，只能为整数）。 同时指定多个账户出资退款的使用场景需要满足以下条件：
	 *
	 * 1、未开通退款支出分离产品功能；
	 *
	 * 2、订单属于分账订单，且分账处于待分账或分账中状态。
	 *
	 * 参数传递需要满足条件：
	 *
	 * 1、基本账户可用余额出资金额与基本账户不可用余额出资金额之和等于退款金额；
	 *
	 * 2、账户类型不能重复。
	 *
	 * 上述任一条件不满足将返回错误
	 */
	@Nullable
	private List<From> from;

	/**
	 * 原订单金额 total int 是 原支付交易的订单总金额，单位为分，只能为整数。 示例值：888
	 */
	private int total;

	/**
	 * 退款币种 currency string[1, 16] 是 符合ISO 4217标准的三位字母代码，目前只支持人民币：CNY。 示例值：CNY
	 */
	private String currency = "CNY";

	@Setter
	@Getter
	@ToString
	public static class From {

		/**
		 * 出资账户类型 account string[1, 32] 是 下面枚举值多选一。 枚举值： AVAILABLE : 可用余额 UNAVAILABLE :
		 * 不可用余额 示例值：AVAILABLE
		 **/
		private String account;

		/**
		 * 出资金额 amount int 是 对应账户出资金额 示例值：444
		 */
		private int amount;

	}

}
