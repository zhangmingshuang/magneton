package org.magneton.module.sms.redis;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import org.magneton.core.base.Objects;
import org.magneton.core.base.Preconditions;
import org.magneton.module.sms.AbstractSms;
import org.magneton.module.sms.entity.SmsToken;
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
		return preSendTime + (sendGapSeconds * 1000L) < System.currentTimeMillis();
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
	protected void mobileSendSuccess(String mobile, SmsToken smsToken) {
		String token = Preconditions.checkNotNull(smsToken.getToken());
		CacheToken cacheToken = new CacheToken().setMobile(Preconditions.checkNotNull(mobile))
				.setCode(Preconditions.checkNotNull(smsToken.getCode())).setTime(System.currentTimeMillis());
		int periodSecond = super.getSmsProperty().getPeriodSecond();
		this.redissonClient.getBucket(KEY + ":token:" + token).set(cacheToken, periodSecond, TimeUnit.SECONDS);
		this.redissonClient.getBucket(KEY + ":token:" + mobile).set(token, periodSecond, TimeUnit.SECONDS);
	}

	@Nullable
	@Override
	public String token(String mobile, String group) {
		RBucket<String> mobileTokenBucket = this.redissonClient.getBucket(KEY + ":token:" + mobile);
		return mobileTokenBucket.get();
	}

	@Override
	public boolean validate(String token, String mobile, String code) {
		Preconditions.checkNotNull(token);
		Preconditions.checkNotNull(code);
		Preconditions.checkNotNull(mobile);
		RBucket<CacheToken> tokenBucket = this.redissonClient.getBucket(KEY + ":token:" + token);
		CacheToken cacheToken = tokenBucket.get();
		if (cacheToken == null) {
			return false;
		}
		if (!Objects.equal(cacheToken.getMobile(), mobile)) {
			return false;
		}
		long time = cacheToken.getTime();
		if (time + super.getSmsProperty().getPeriodSecond() * 1000L < System.currentTimeMillis()) {
			// 已过期
			this.redissonClient.getBucket(KEY + ":token:" + mobile).deleteAsync();
			tokenBucket.deleteAsync();
			return false;
		}
		if (!Objects.equal(cacheToken.getCode(), code)) {
			return false;
		}
		this.redissonClient.getBucket(KEY + ":token:" + mobile).deleteAsync();
		tokenBucket.deleteAsync();
		return true;
	}

	@Override
	protected boolean groupRiskOpinion(String group, int groupRiskCount, int groupRiskInSeconds) {
		if (groupRiskCount < 1 || groupRiskInSeconds < 1) {
			return true;
		}
		RAtomicLong groupAtomic = this.redissonClient.getAtomicLong(KEY + ":" + group);
		long currentGroupCount = groupAtomic.incrementAndGet();
		if (currentGroupCount <= 1) {
			groupAtomic.expire(groupRiskInSeconds, TimeUnit.SECONDS);
		}
		return currentGroupCount <= groupRiskCount;
	}

	@Override
	protected boolean mobileCountCapsOpinion(String mobile, int dayCount, int hourCount) {
		if (hourCount > 0) {
			RAtomicLong hourAtomic = this.redissonClient.getAtomicLong(KEY + ":hour:" + mobile);
			long currentHourCount = hourAtomic.incrementAndGet();
			if (currentHourCount <= 1) {
				hourAtomic.expire(1, TimeUnit.HOURS);
			}
			if (currentHourCount > hourCount) {
				return false;
			}
		}
		if (dayCount > 0) {
			RAtomicLong dayAtomic = this.redissonClient.getAtomicLong(KEY + ":day:" + mobile);
			long currentDayCount = dayAtomic.incrementAndGet();
			if (currentDayCount <= 1) {
				LocalDateTime now = LocalDateTime.now();
				LocalDateTime tomorrow = LocalDateTime.of(LocalDate.now(), LocalTime.MIN).plusDays(1);
				long gapSeconds = Duration.between(now, tomorrow).getSeconds();
				dayAtomic.expire(gapSeconds, TimeUnit.SECONDS);
			}
			return currentDayCount <= dayCount;
		}
		return true;
	}

}
