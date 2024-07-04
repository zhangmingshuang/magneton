package org.magneton.enhance.pay.wxv3.profitsharing.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 分账
 *
 * @author zhangmsh 2022/6/5
 * @since 1.0.0
 */
@Setter
@Getter
@ToString
public class ResponseReceiver {

	/**
	 * 分账金额 amount int 是 分账金额，单位为分，只能为整数，不能超过原订单支付金额及最大分账比例金额 示例值：100
	 */
	private int amount;

	/**
	 * 分账描述 description string[1, 80] 是 分账的原因描述，分账账单中需要体现 示例值：分给商户1900000110
	 */
	private String description;

	/**
	 * 接收方类型 type string[1, 32] 是 1、MERCHANT_ID：商户号
	 * 2、PERSONAL_OPENID：个人openid（由父商户APPID转换得到） 示例值：MERCHANT_ID
	 */
	private String type;

	/**
	 * 接收方账号 account string[1, 64] 是 1、分账接收方类型为MERCHANT_ID时，分账接收方账号为商户号
	 * 2、分账接收方类型为PERSONAL_OPENID时，分账接收方账号为个人openid 示例值：1900000109
	 */
	private String account;

	/**
	 * 分账结果 result string[1, 32] 是 枚举值： 1、PENDING：待分账 2、SUCCESS：分账成功 3、CLOSED：已关闭
	 * 示例值：SUCCESS
	 */
	private String result;

	/**
	 * 分账失败原因 fail_reason string[1, 64] 是 分账失败原因，当分账结果result为CLOSED（已关闭）时，返回该字段 枚举值：
	 * 1、ACCOUNT_ABNORMAL：分账接收账户异常 2、NO_RELATION：分账关系已解除 3、RECEIVER_HIGH_RISK：高风险接收方
	 * 4、RECEIVER_REAL_NAME_NOT_VERIFIED：接收方未实名 5、NO_AUTH：分账权限已解除
	 * 6、RECEIVER_RECEIPT_LIMIT：接收方已达收款限额 7、PAYER_ACCOUNT_ABNORMAL：分出方账户异常
	 * 示例值：ACCOUNT_ABNORMAL
	 */
	@JsonProperty("fail_reason")
	private String failReason;

	/**
	 * 分账创建时间 create_time string[1, 64] 是
	 * 分账创建时间，遵循rfc3339标准格式，格式为yyyy-MM-DDTHH:mm:ss+TIMEZONE，yyyy-MM-DD表示年月日，T出现在字符串中，表示time元素的开头，HH:mm:ss表示时分秒，TIMEZONE表示时区（+08:00表示东八区时间，领先UTC
	 * 8小时，即北京时间）。例如：2015-05-20T13:29:35+08:00表示，北京时间2015年5月20日 13点29分35秒。
	 * 示例值：2015-05-20T13:29:35+08:00
	 */
	@JsonProperty("create_time")
	private String createTime;

	/**
	 * 分账完成时间 finish_time string[1, 64] 是
	 * 分账完成时间，遵循rfc3339标准格式，格式为yyyy-MM-DDTHH:mm:ss+TIMEZONE，yyyy-MM-DD表示年月日，T出现在字符串中，表示time元素的开头，HH:mm:ss表示时分秒，TIMEZONE表示时区（+08:00表示东八区时间，领先UTC
	 * 8小时，即北京时间）。例如：2015-05-20T13:29:35+08:00表示，北京时间2015年5月20日 13点29分35秒。
	 * 示例值：2015-05-20T13:29:35+08:00
	 */
	@JsonProperty("finish_time")
	private String finishTime;

	/**
	 * 分账明细单号 detail_id string[1,64] 是 微信分账明细单号，每笔分账业务执行的明细单号，可与资金账单对账使用
	 * 示例值：36011111111111111111111
	 */
	@JsonProperty("detail_id")
	private String detailId;

}
