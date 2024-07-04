package org.magneton.enhance.sms.redis;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.magneton.adaptive.redis.RedissonAdapter;
import org.magneton.enhance.sms.process.aliyun.MockAliyunSendProcessor;
import org.magneton.enhance.sms.property.SmsProperty;
import org.redisson.api.RedissonClient;

/**
 * @author zhangmsh 2022/3/24
 * @since 1.0.0
 */
class RedissonSmsTestTemplate {

	private static RedissonSmsTemplate sms;

	private static final String MOBILE = "13860132592";

	private static final String GROUP = "test-group";

	private static final SmsProperty SMS_PROPERTY = new SmsProperty();
	static {
		try {
			RedissonClient redissonClient = RedissonAdapter.createSingleServerClient();
			sms = new RedissonSmsTemplate(redissonClient, new MockAliyunSendProcessor(), SMS_PROPERTY);
		}
		catch (Exception e) {
			System.out.println("Error: RedissonClient not found.");
		}
	}

	@BeforeEach
	void each() {
		if (sms != null) {
			sms.flushCache(MOBILE, GROUP, null);
		}
	}

	@Test
	void remainErrors() {
		if (sms == null) {
			return;
		}
		String token = null;
		try {
			System.out.println("可错误次数：" + SMS_PROPERTY.getValidErrorCount());
			long remainError = sms.remainErrors(MOBILE);
			Assertions.assertEquals(SMS_PROPERTY.getValidErrorCount(), remainError);
			sms.send(MOBILE);
			token = sms.getToken(MOBILE);
			sms.validate(token, MOBILE, "errorCode");
			remainError = sms.remainErrors(MOBILE);
			Assertions.assertEquals(4, remainError);
		}
		finally {
			sms.flushCache(MOBILE, GROUP, token);
		}
	}

}