package org.magneton.enhance.sms.property;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.regex.Pattern;

/**
 * .
 *
 * @author zhangmsh 16/03/2022
 * @since 2.0.7
 */
@Setter
@Getter
@ToString
public class SmsProperty {

	/**
	 * 手机号正则表达式
	 */
	private Pattern mobileRegex = Pattern
		.compile("^((13[0-9])|(14[0,1,4-9])|(15[0-3,5-9])|(16[2,5,6,7])|(17[0-8])|(18[0-9])|(19[0-3,5-9]))\\d{8}$");

	/**
	 * 一天一个手机号可以发送几次
	 * <p>
	 * 如果小于1表示不作用
	 */
	private int dayLimit = 10;

	/**
	 * 一小时一个手机号可以发送几次
	 * <p>
	 * 如果小于1表示不作用
	 */
	private int hourLimit = 8;

	/**
	 * 每次发送时间间隔秒数
	 * <p>
	 * 如果小于等于0表示没有限制。
	 */
	private int sendGapSeconds = 60;

	/**
	 * 启动分组风控
	 * @apiNote 分组风控主要用来判断同一个分组的标识是否有风险，比如同一个IP地址，同一个设备号等。
	 */
	private boolean groupRisk = true;

	/**
	 * 分组风控值，即单分组超过该值表示风控
	 * <p>
	 * 如果小于1表示不作用
	 * @apiNote 该参数在{@link #groupRisk}为true时才有效。在同一个分组中，任何一次校验失败都会被累计到对应的分组中的风控值。
	 * 当风控值达到该值时，该分组将被临时禁用。
	 */
	private int groupRiskCount = 20;

	/**
	 * 在多少秒内表示是有风险的统计
	 * @apiNote 该参数在{@link #groupRisk}为true时才有效。在同一个分组中，在该时间内的校验失败都会被累计到对应的分组中的风控值。
	 */
	private int groupRiskInSeconds = 24 * 60 * 60;

	/**
	 * 每条短信的有效期限，单位秒
	 * @apiNote 当短信发送成功后，如果在该时间内没有校验，则短信失效。
	 */
	private int periodSecond = 5 * 60;

	/**
	 * 允许校验错误次数
	 * @apiNote 如果小于1表示不作用。当校验错误次数达到该值时，该手机号将被临时禁用。
	 */
	private int validErrorCount = 5;

	/**
	 * 校验错误次数的统计时间秒数，即在多少秒内需要记住错误次数
	 * @apiNote 当验证码校验错误一次之后，会记住错误次数，在该时间内如果再次校验错误，则错误次数会累加。
	 */
	private int validErrorInSeconds = 60 * 60;

}
