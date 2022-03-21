package org.magneton.module.sms;

import lombok.Getter;
import org.magneton.core.Consequences;
import org.magneton.core.base.Preconditions;
import org.magneton.module.sms.entity.SmsToken;
import org.magneton.module.sms.process.SendProcessor;
import org.magneton.module.sms.property.SmsProperty;

/**
 * .
 *
 * @author zhangmsh 16/03/2022
 * @since 2.0.7
 */
public abstract class AbstractSms implements Sms {

	private final SendProcessor sendProcessor;

	@Getter
	private final SmsProperty smsProperty;

	public AbstractSms(SendProcessor sendProcessor, SmsProperty smsProperty) {
		this.sendProcessor = Preconditions.checkNotNull(sendProcessor);
		this.smsProperty = Preconditions.checkNotNull(smsProperty);
	}

	@Override
	public boolean isMobile(String mobile) {
		Preconditions.checkNotNull(mobile);
		return this.smsProperty.getMobileRegex().matcher(mobile).matches();
	}

	@Override
	public Consequences<SendStatus> trySend(String mobile, String group) {
		Preconditions.checkNotNull(mobile);
		Preconditions.checkNotNull(group);
		if (!this.isMobile(mobile)) {
			return Consequences.fail(SendStatus.FAILURE, "手机号正则匹配错误");
		}
		// 是否发送冷却时间内
		if (this.smsProperty.getSendGapSeconds() > 0
				&& !this.sendGapOpinion(mobile, this.smsProperty.getSendGapSeconds())) {
			return Consequences.fail(SendStatus.SEND_GAP, "两次发送时间间隔太短");
		}
		// 分组风控处理
		boolean isGroupRisk = this.smsProperty.isGroupRisk();
		if (isGroupRisk && !this.groupRiskOpinion(group, this.smsProperty.getGroupRiskCount(),
				this.smsProperty.getGroupRiskInSeconds())) {
			return Consequences.fail(SendStatus.RISK, "分组存在风险");
		}
		// 次数上限判断
		if (!this.mobileCountCapsOpinion(mobile, this.smsProperty.getDayCount(), this.smsProperty.getHourCount())) {
			return Consequences.fail(SendStatus.COUNT_CAPS, "发送次数达到上限");
		}
		return this.send(mobile);
	}

	@Override
	public Consequences<SendStatus> send(String mobile) {
		Preconditions.checkNotNull(mobile);
		Consequences<SmsToken> sendResponse = this.getSendProcessor().send(mobile);
		if (sendResponse.isSuccess()) {
			this.mobileSendSuccess(mobile, sendResponse.getData());
			return Consequences.success(SendStatus.SUCCESS, "短信发送成功");
		}
		return Consequences.fail(SendStatus.FAILURE, "短信发送失败");
	}

	/**
	 * 发送统计
	 * @param mobile 手机号
	 * @param smsToken 此次发送成功的对应Token
	 */
	protected abstract void mobileSendSuccess(String mobile, SmsToken smsToken);

	/**
	 * 判断是否在发送冷却时间间隔内
	 * @param mobile 手机号
	 * @param sendGapSeconds 时间间隔
	 * @return 是否在冷却时间间隔内
	 */
	protected abstract boolean sendGapOpinion(String mobile, int sendGapSeconds);

	/**
	 * 判断分组是否有风险
	 * @param group 分组
	 * @param groupRiskCount 分组风控值，即单分组超过该值表示风控
	 * @param groupRiskInSeconds 在多少秒内表示是有风险的统计
	 * @return 是否有风险
	 */
	protected abstract boolean groupRiskOpinion(String group, int groupRiskCount, int groupRiskInSeconds);

	/**
	 * 判断手机号是否达到发送上限
	 * @param mobile 手机号
	 * @param dayCount 一天限制
	 * @param hourCount 一小时限制
	 * @return 是否上限
	 */
	protected abstract boolean mobileCountCapsOpinion(String mobile, int dayCount, int hourCount);

	protected SendProcessor getSendProcessor() {
		return this.sendProcessor;
	}

}
