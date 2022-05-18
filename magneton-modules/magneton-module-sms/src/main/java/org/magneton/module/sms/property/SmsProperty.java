package org.magneton.module.sms.property;

import java.util.regex.Pattern;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
	 * 手机正则
	 */
	private Pattern mobileRegex = Pattern
			.compile("^((13[0-9])|(14[0,1,4-9])|(15[0-3,5-9])|(16[2,5,6,7])|(17[0-8])|(18[0-9])|(19[0-3,5-9]))\\d{8}$");

	/**
	 * 一天一个手机号可以发送几次
	 *
	 * 如果小于1表示不作用
	 */
	private int dayCount = 10;

	/**
	 * 一小时一个手机号可以发送几次
	 *
	 * 如果小于1表示不作用
	 */
	private int hourCount = 8;

	/**
	 * 每次发送时间间隔，如果小于等于0表示没有限制。
	 */
	private int sendGapSeconds = 60;

	/**
	 * 启动分组风控
	 */
	private boolean groupRisk = true;

	/**
	 * 分组风控值，即单分组超过该值表示风控
	 *
	 * 如果小于1表示不作用
	 */
	private int groupRiskCount = 20;

	/**
	 * 在多少秒内表示是有风险的统计
	 */
	private int groupRiskInSeconds = 24 * 60 * 60;

	/**
	 * 每条短信的有效期限
	 */
	private int periodSecond = 5 * 60;

	/**
	 * 允许校验错误次数
	 */
	private int validErrorCount = 5;

	/**
	 * 校验错误次数的统计时间秒数，即在多少秒内需要记住错误次数
	 */
	private int validErrorInSeconds = 60 * 60;

}
