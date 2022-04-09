package org.magneton.module.pay.wechat.v3.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * .
 *
 * @author zhangmsh 09/04/2022
 * @since 2.0.8
 */
@Setter
@Getter
@ToString
public class WxPayJSAPIPrepay {

	/**
	 * 应用ID appId string[1,32] 是 商户申请的公众号对应的appid，由微信支付生成，可在公众号后台查看 示例值：wx8888888888888888
	 */
	private String appId;

	/**
	 * 时间戳 timeStamp string[1,32] 是 时间戳，标准北京时间，时区为东八区，自1970年1月1日
	 * 0点0分0秒以来的秒数。注意：部分系统取到的值为毫秒级，需要转换成秒(10位数字)。 示例值：1414561699
	 */
	private String timeStamp;

	/**
	 * 随机字符串 nonceStr string[1,32] 是 随机字符串，不长于32位。 示例值：5K8264ILTKCH16CQ2502SI8ZNMTM67VS
	 **/
	private String nonceStr;

	/**
	 * 订单详情扩展字符串 package string[1,128] 是 JSAPI下单接口返回的prepay_id参数值，提交格式如：prepay_id=***
	 * 示例值：prepay_id=wx201410272009395522657a690389285100
	 */
	@JsonProperty("package")
	private String packageValue;

	/**
	 * 签名方式 signType string[1,32] 是 签名类型，默认为RSA，仅支持RSA。 示例值：RSA
	 **/
	private String signType;

	/**
	 * 签名 paySign string[1,256] 是 签名，使用字段appId、timeStamp、nonceStr、package计算得出的签名值
	 * 示例值：oR9d8PuhnIc+YZ8cBHFCwfgpaK9gd7vaRvkYD7rthRAZ\/X+QBhcCYL21N7cHCTUxbQ+EAt6Uy+lwSN22f5YZvI45MLko8Pfso0jm46v5hqcVwrk6uddkGuT+Cdvu4WBqDzaDjnNa5UK3GfE1Wfl2gHxIIY5lLdUgWFts17D4WuolLLkiFZV+JSHMvH7eaLdT9N5GBovBwu5yYKUR7skR8Fu+LozcSqQixnlEZUfyE55feLOQTUYzLmR9pNtPbPsu6WVhbNHMS3Ss2+AehHvz+n64GDmXxbX++IOBvm2olHu3PsOUGRwhudhVf7UcGcunXt8cqNjKNqZLhLw4jq\/xDg==
	 **/
	private String paySign;

}
