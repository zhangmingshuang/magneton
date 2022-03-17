package org.magneton.module.sms.redis;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

import org.magneton.core.base.Preconditions;
import org.magneton.module.sms.AbstractSms;
import org.magneton.module.sms.process.SendProcessor;
import org.magneton.module.sms.property.SmsProperty;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

/**
 * .
 *
 * @author zhangmsh 16/03/2022
 * @since 2.0.7
 */
public class RedissonSms extends AbstractSms {

	private static final String KEY = "magneton:m:sms";

	private final RedissonClient redissonClient;

	public RedissonSms(RedissonClient redissonClient, SendProcessor sendProcessor, SmsProperty smsProperty) {
		super(sendProcessor, smsProperty);
		this.redissonClient = Preconditions.checkNotNull(redissonClient);
	}

	@Override
	protected boolean sendGapOpinion(String mobile, int sendGapSeconds) {
		if (sendGapSeconds < 1) {
			return true;
		}
		RBucket<Long> gapBucket = this.redissonClient.getBucket(KEY + ":gap:" + mobile);
		if (!gapBucket.isExists()) {
			gapBucket.set(System.currentTimeMillis(), sendGapSeconds, TimeUnit.SECONDS);
			return true;
		}
		long preSendTime = gapBucket.get();
		return preSendTime + (sendGapSeconds * 1000L) >= System.currentTimeMillis();
	}

	@Override
	public long ttl(String mobile) {
		RBucket<Object> gapBucket = this.redissonClient.getBucket(KEY + ":gap:" + mobile);
		long remainTimeToLive = gapBucket.remainTimeToLive();
		if (remainTimeToLive == -1) {
			// 不存在
			return 0;
		}
		if (remainTimeToLive == -2) {
			// 存在，但永不过期
			gapBucket.unlink();
			return 0;
		}
		return remainTimeToLive / 1000;
	}

	@Override
	protected boolean groupRiskOpinion(String group, int groupRiskCount, int groupRiskInSeconds) {
		RAtomicLong groupAtomic = this.redissonClient.getAtomicLong(KEY + ":" + group);
		long currentGroupCount = groupAtomic.incrementAndGet();
		if (currentGroupCount <= 1) {
			groupAtomic.expire(groupRiskInSeconds, TimeUnit.SECONDS);
		}
		return currentGroupCount <= groupRiskCount;
	}

	@Override
	protected boolean mobileCountCapsOpinion(String mobile, int dayCount, int hourCount) {
		RAtomicLong hourAtomic = this.redissonClient.getAtomicLong(KEY + ":hour:" + mobile);
		long currentHourCount = hourAtomic.incrementAndGet();
		if (currentHourCount <= 1) {
			hourAtomic.expire(1, TimeUnit.HOURS);
		}
		if (currentHourCount > hourCount) {
			return false;
		}
		RAtomicLong dayAtomic = this.redissonClient.getAtomicLong(KEY + ":day:" + mobile);
		long currentDayCount = dayAtomic.incrementAndGet();
		if (currentDayCount <= 1) {
			LocalDateTime now = LocalDateTime.now();
			LocalDateTime tomorrow = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
			long gapSeconds = Duration.between(tomorrow, now).getSeconds();
			dayAtomic.expire(gapSeconds, TimeUnit.SECONDS);
		}
		return currentDayCount <= dayCount;
	}

	@Override
	protected void mobileSendSuccess(String mobile) {

	}

}
