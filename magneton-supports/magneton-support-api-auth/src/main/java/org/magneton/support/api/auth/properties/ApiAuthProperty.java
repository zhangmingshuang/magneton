package org.magneton.support.api.auth.properties;

import java.util.regex.Pattern;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * .
 *
 * @author zhangmsh 15/03/2022
 * @since 2.0.7
 */
@Setter
@Getter
@ToString
public class ApiAuthProperty {

	private Sms sms;

	@Setter
	@Getter
	@ToString
	public static class Sms {

		/**
		 * 手机正则
		 */
		private Pattern mobileRegex = Pattern.compile(
				"^((13[0-9])|(14[0,1,4-9])|(15[0-3,5-9])|(16[2,5,6,7])|(17[0-8])|(18[0-9])|(19[0-3,5-9]))\\d{8}$");

		/**
		 * 一天一个手机号可以发送几次
		 */
		private int dayCount = 10;

		/**
		 * 一小时一个手机号可以发送几次
		 */
		private int hourCount = 8;

		/**
		 * 每次发送时间间隔
		 */
		private int sendGapSeconds = 60;

		/**
		 * 启动IP风控
		 */
		private boolean ipRisk = true;

		/**
		 * IP风控值，即单IP超过该值表示风控
		 */
		private int ipRiskCount = 20;

		/**
		 * 在多少秒内表示是有风险的统计
		 */
		private int riskInSeconds = 24 * 60 * 60;

	}

}
