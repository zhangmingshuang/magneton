package org.magneton.module.pay.wechat.v3.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author zhangmsh 2022/4/5
 * @since 1.0.0
 */
@Setter
@Getter
@ToString
@Accessors(chain = true)
public class WechatAppV3PayRes {

	/**
	 * 应用ID string[1,32] 是 微信开放平台审核通过的移动应用appid 。 示例值：wx8888888888888888
	 **/
	private String appId;

	/**
	 * 商户号 string[1,32] 是 请填写商户号mchid对应的值。 示例值：1900000109
	 **/
	private String partnerId;

	/**
	 * 预支付交易会话ID string[1,64] 是 微信返回的支付交易会话ID，该值有效期为2小时。 示例值：
	 * WX1217752501201407033233368018
	 **/
	private String prepayId;

	/**
	 * 订单详情扩展字符串 string[1,128] 是 暂填写固定值Sign=WXPay 示例值：Sign=WXPay
	 */
	private String packageValue;

	/**
	 * 随机字符串 noncestr string[1,32] 是 随机字符串，不长于32位。推荐随机数生成算法。\ 示例值：
	 * 5K8264ILTKCH16CQ2502SI8ZNMTM67VS
	 **/
	private String nonceStr;

	/**
	 * 时间戳 timestamp string[1,10] 是 时间戳，标准北京时间，时区为东八区，自1970年1月1日
	 * 0点0分0秒以来的秒数。注意：部分系统取到的值为毫秒级，需要转换成秒(10位数字)。 示例值：1412000000
	 **/
	private String timeStamp;

	/**
	 * 签名 sign string[1,256] 是 签名，使用字段appId、timeStamp、nonceStr、prepayid计算得出的签名值 注意：取值RSA格式
	 * 示例值：oR9d8PuhnIc+YZ8cBHFCwfgpaK9gd7vaRvkYD7rthRAZ\/X+QBhcCYL21N7cHCTUxbQ+EAt6Uy+lwSN22f5YZvI45MLko8Pfso0jm46v5hqcVwrk6uddkGuT+Cdvu4WBqDzaDjnNa5UK3GfE1Wfl2gHxIIY5lLdUgWFts17D4WuolLLkiFZV+JSHMvH7eaLdT9N5GBovBwu5yYKUR7skR8Fu+LozcSqQixnlEZUfyE55feLOQTUYzLmR9pNtPbPsu6WVhbNHMS3Ss2+AehHvz+n64GDmXxbX++IOBvm2olHu3PsOUGRwhudhVf7UcGcunXt8cqNjKNqZLhLw4jq\/xDg==
	 **/
	private String sign;

}
