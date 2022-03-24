package org.magneton.module.sms.redis;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.magneton.adaptive.redis.RedissonAdapter;
import org.magneton.module.sms.process.aliyun.MockAliyunSendProcessor;
import org.magneton.module.sms.property.SmsProperty;

/**
 * @author zhangmsh 2022/3/24
 * @since 1.0.0
 */
class RedissonSmsTest {

	private static final RedissonSms SMS;

	private static final String MOBILE = "13860132592";

	private static final String GROUP = "test-group";

	private static final SmsProperty SMS_PROPERTY = new SmsProperty();
	static {
		SMS = new RedissonSms(RedissonAdapter.createSingleServerClient(), new MockAliyunSendProcessor(), SMS_PROPERTY);
	}

	@BeforeEach
	void each() {
		SMS.flushCache(MOBILE, GROUP, null);
	}

	@Test
	void remainErrors() {
		String token = null;
		try {
			System.out.println("可错误次数：" + SMS_PROPERTY.getValidErrorCount());
			long remainError = SMS.remainErrors(MOBILE);
			Assertions.assertEquals(SMS_PROPERTY.getValidErrorCount(), remainError);
			SMS.send(MOBILE);
			token = SMS.token(MOBILE);
			SMS.validate(token, MOBILE, "errorCode");
			remainError = SMS.remainErrors(MOBILE);
			Assertions.assertEquals(4, remainError);
		}
		finally {
			SMS.flushCache(MOBILE, GROUP, token);
		}
	}

}