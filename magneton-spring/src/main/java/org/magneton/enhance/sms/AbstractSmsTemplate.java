package org.magneton.enhance.sms;

import com.google.common.base.Preconditions;
import lombok.Getter;
import org.magneton.enhance.Result;
import org.magneton.enhance.sms.entity.SmsToken;
import org.magneton.enhance.sms.monitor.FailureType;
import org.magneton.enhance.sms.monitor.SmsMonitors;
import org.magneton.enhance.sms.process.SendProcessor;
import org.magneton.enhance.sms.property.SmsProperty;

/**
 * .
 *
 * @author zhangmsh 16/03/2022
 * @since 2.0.7
 */
public abstract class AbstractSmsTemplate implements SmsTemplate {

	private final SendProcessor sendProcessor;

	@Getter
	private final SmsProperty smsProperty;

	public AbstractSmsTemplate(SendProcessor sendProcessor, SmsProperty smsProperty) {
		this.sendProcessor = Preconditions.checkNotNull(sendProcessor, "sendProcessor must not be null");
		this.smsProperty = Preconditions.checkNotNull(smsProperty, "smsProperty must not be null");
	}

	@Override
	public boolean isMobile(String mobile) {
		Preconditions.checkNotNull(mobile, "mobile must not be null");

		return this.smsProperty.getMobileRegex().matcher(mobile).matches();
	}

	@Override
	public Result<SendStatus> trySend(String mobile, String group) {
		Preconditions.checkNotNull(mobile, "mobile must not be null");
		Preconditions.checkNotNull(group, "group must not be null");

		if (!this.isMobile(mobile)) {
			SmsMonitors.notifier().onFailure(mobile, FailureType.MOBILE_REGEX);
			return Result.failWith(SendStatus.FAILURE, "手机号正则匹配错误");
		}
		// 是否发送冷却时间内
		if (this.smsProperty.getSendGapSeconds() > 0
				&& !this.isAllowSendInGap(mobile, this.smsProperty.getSendGapSeconds())) {
			SmsMonitors.notifier().onFailure(mobile, FailureType.SEND_GAP);
			return Result.failWith(SendStatus.SEND_GAP, "两次发送时间间隔太短");
		}

		// 分组风控处理
		boolean isGroupRisk = this.smsProperty.isGroupRisk();
		if (isGroupRisk && !this.isAllowSendByGroup(group, this.smsProperty.getGroupRiskCount(),
				this.smsProperty.getGroupRiskInSeconds())) {
			SmsMonitors.notifier().onFailure(mobile, FailureType.GROUP_RISK);
			return Result.failWith(SendStatus.RISK, "分组存在风险");
		}

		// 手机风控处理
		// boolean isValidErrorCount = this.smsProperty.getValidErrorCount() > 0;
		// if (isValidErrorCount && !this.temporarilyDisableOpinion(mobile,
		// this.smsProperty.getValidErrorCount(),
		// this.smsProperty.getValidErrorInSeconds())) {
		// return Consequences.fail(SendStatus.TEMPORARILY_DISABLE, "手机号存在风险");
		// }

		// 次数上限判断
		if (!this.isAllowSendAtToday(mobile, this.smsProperty.getDayLimit())) {
			SmsMonitors.notifier().onFailure(mobile, FailureType.DAY_COUNT_CAPS);
			return Result.failWith(SendStatus.COUNT_CAPS, "发送次数达到上限");
		}
		if (!this.isAllowSendAtHour(mobile, this.smsProperty.getHourLimit())) {
			SmsMonitors.notifier().onFailure(mobile, FailureType.HOUR_COUNT_CAPS);
			return Result.failWith(SendStatus.COUNT_CAPS, "发送次数达到上限");

		}

		return this.send(mobile);
	}

	@Override
	public Result<SendStatus> send(String mobile) {
		Preconditions.checkNotNull(mobile, "mobile must not be null");

		Result<SmsToken> sendResponse = this.getSendProcessor().send(mobile);
		if (sendResponse.isSuccess()) {
			this.onSendSuccess(mobile, sendResponse.getData());
			SmsMonitors.notifier().onSuccess(mobile);
			return Result.successWith(SendStatus.SUCCESS, "短信发送成功");
		}
		return Result.failWith(SendStatus.FAILURE, "短信发送失败");
	}

	/**
	 * 发送成功后的处理
	 * @param mobile 手机号
	 * @param smsToken 此次发送成功的对应Token
	 */
	protected abstract void onSendSuccess(String mobile, SmsToken smsToken);

	/**
	 * 判断是否在间隔内允许发送
	 * @param mobile 手机号
	 * @param sendGapSeconds 时间间隔
	 * @return 是否允许发送
	 */
	protected abstract boolean isAllowSendInGap(String mobile, int sendGapSeconds);

	/**
	 * 判断分组允许发送
	 * @param group 分组
	 * @param groupRiskCount 分组风控值，即单分组超过该值表示风控
	 * @param groupRiskInSeconds 在多少秒内表示是有风险的统计
	 * @return 是否允许发送
	 */
	protected abstract boolean isAllowSendByGroup(String group, int groupRiskCount, int groupRiskInSeconds);

	/**
	 * 判断手机号在今天是否允许发送
	 * @param mobile 手机号
	 * @param dayCount 一天限制
	 * @return 是否允许发送
	 */
	protected abstract boolean isAllowSendAtToday(String mobile, int dayCount);

	/**
	 * 判断手机号在一小时内是否允许发送
	 * @param mobile 手机号
	 * @param hourCount 一小时限制
	 * @return 是否允许发送
	 */
	protected abstract boolean isAllowSendAtHour(String mobile, int hourCount);

	/**
	 * 获取发送处理器
	 * @return 发送处理器
	 */
	protected SendProcessor getSendProcessor() {
		return this.sendProcessor;
	}

}
