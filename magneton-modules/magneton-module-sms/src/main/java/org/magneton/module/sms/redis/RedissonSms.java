package org.magneton.module.sms.redis;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import javax.annotations.VisibleForTesting;
import org.magneton.core.base.Objects;
import org.magneton.core.base.Preconditions;
import org.magneton.core.base.Strings;
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
		RBucket<Long> gapBucket = this.getGapCache(mobile);
		if (!gapBucket.isExists()) {
			gapBucket.set(System.currentTimeMillis(), sendGapSeconds, TimeUnit.SECONDS);
			return true;
		}
		long preSendTime = gapBucket.get();
		return preSendTime + (sendGapSeconds * 1000L) < System.currentTimeMillis();
	}

	@Override
	public long ttl(String mobile) {
		RBucket<Long> gapBucket = this.getGapCache(mobile);
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
		this.getTokenTokenCache(token).set(cacheToken, periodSecond, TimeUnit.SECONDS);
		this.getTokenMobileCache(mobile).set(token, periodSecond, TimeUnit.SECONDS);
	}

	@Nullable
	@Override
	public String token(String mobile, String group) {
		return this.getTokenMobileCache(Preconditions.checkNotNull(mobile)).get();
	}

	@Override
	public boolean validate(String token, String mobile, String code) {
		Preconditions.checkNotNull(token);
		Preconditions.checkNotNull(code);
		Preconditions.checkNotNull(mobile);
		RBucket<CacheToken> tokenBucket = this.getTokenTokenCache(token);
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
			this.getTokenMobileCache(mobile).deleteAsync();
			tokenBucket.deleteAsync();
			return false;
		}
		if (!Objects.equal(cacheToken.getCode(), code)) {
			this.rememberError(mobile);
			return false;
		}
		this.getTokenMobileCache(mobile).deleteAsync();
		tokenBucket.deleteAsync();
		return true;
	}

	private void rememberError(String mobile) {
		RAtomicLong atomicLong = this.getMobileErrorCache(mobile);
		long count = atomicLong.incrementAndGet();
		if (count <= 1) {
			atomicLong.expire(this.getSmsProperty().getValidErrorInSeconds(), TimeUnit.SECONDS);
		}
	}

	@Override
	public long remainErrors(String mobile) {
		int validErrorCount = this.getSmsProperty().getValidErrorCount();
		if (validErrorCount < 1) {
			return -1;
		}
		Preconditions.checkNotNull(mobile);
		RAtomicLong atomicLong = this.getMobileErrorCache(mobile);
		long count = atomicLong.get();
		return Math.max(0, validErrorCount - count);
	}

	@Override
	protected boolean groupRiskOpinion(String group, int groupRiskCount, int groupRiskInSeconds) {
		if (groupRiskCount < 1 || groupRiskInSeconds < 1) {
			return true;
		}
		RAtomicLong groupAtomic = this.getGroupCache(group);
		long currentGroupCount = groupAtomic.incrementAndGet();
		if (currentGroupCount <= 1) {
			groupAtomic.expire(groupRiskInSeconds, TimeUnit.SECONDS);
		}
		return currentGroupCount <= groupRiskCount;
	}

	@Override
	protected boolean mobileCountCapsOpinion(String mobile, int dayCount, int hourCount) {
		if (hourCount > 0) {
			RAtomicLong hourAtomic = this.getHourCache(mobile);
			long currentHourCount = hourAtomic.incrementAndGet();
			if (currentHourCount <= 1) {
				hourAtomic.expire(1, TimeUnit.HOURS);
			}
			if (currentHourCount > hourCount) {
				return false;
			}
		}
		if (dayCount > 0) {
			RAtomicLong dayAtomic = this.getDayCache(mobile);
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

	@VisibleForTesting
	public void flushCache(@Nullable String mobile, @Nullable String group, @Nullable String token) {
		if (!Strings.isNullOrEmpty(mobile)) {
			this.getDayCache(mobile).delete();
			this.getHourCache(mobile).delete();
			this.getMobileErrorCache(mobile).delete();
			this.getGapCache(mobile).delete();
			this.getTokenMobileCache(mobile).delete();
		}
		if (!Strings.isNullOrEmpty(group)) {
			this.getGroupCache(group).delete();
		}
		if (!Strings.isNullOrEmpty(token)) {
			this.getTokenTokenCache(token).delete();
		}
	}

	private RAtomicLong getDayCache(String mobile) {
		return this.redissonClient.getAtomicLong(KEY + ":day:" + mobile);
	}

	private RAtomicLong getHourCache(String mobile) {
		return this.redissonClient.getAtomicLong(KEY + ":hour:" + mobile);
	}

	private RAtomicLong getGroupCache(String group) {
		return this.redissonClient.getAtomicLong(KEY + ":" + group);
	}

	private RAtomicLong getMobileErrorCache(String mobile) {
		return this.redissonClient.getAtomicLong(KEY + ":error:" + mobile);
	}

	private RBucket<Long> getGapCache(String mobile) {
		return this.redissonClient.getBucket(KEY + ":gap:" + mobile);
	}

	private RBucket<String> getTokenMobileCache(String mobile) {
		return this.redissonClient.getBucket(KEY + ":token:" + mobile);
	}

	private RBucket<CacheToken> getTokenTokenCache(String token) {
		return this.redissonClient.getBucket(KEY + ":token:" + token);
	}

}
