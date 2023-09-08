package org.magneton.module.sms.process.aliyun;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.magneton.adaptive.redis.RedissonAdapter;
import org.magneton.module.sms.SendStatus;
import org.magneton.module.sms.Sms;
import org.magneton.module.sms.process.SendProcessor;
import org.magneton.module.sms.property.SmsProperty;
import org.magneton.module.sms.redis.RedissonSms;

/**
 * .
 *
 * @author zhangmsh 18/03/2022
 * @since 2.0.7
 */
class AbstractAliyunSmsProcessorTest {

	private static Sms sms;

	@BeforeAll
	public static void init() {
		SendProcessor sendProcessor = new MockAliyunSendProcessor();
		SmsProperty smsProperty = new SmsProperty();
		sms = new RedissonSms(RedissonAdapter.createSingleServerClient(), sendProcessor, smsProperty);
	}

	@Test
	void send() {
		String mobile = "13860132592";
		Reply<SendStatus> response = sms.trySend(mobile);
		Assertions.assertTrue(response.isSuccess());

		long ttl = sms.ttl(mobile);
		Assertions.assertTrue(ttl > 0, String.valueOf(ttl));

		String token = sms.token(mobile);
		Assertions.assertNotNull(token);

		boolean error = sms.validate(token, mobile, "error");
		Assertions.assertFalse(error);

		boolean success = sms.validate(token, mobile, "123456");
		Assertions.assertTrue(success);
	}

}