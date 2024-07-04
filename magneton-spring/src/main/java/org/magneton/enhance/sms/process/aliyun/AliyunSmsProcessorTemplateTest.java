package org.magneton.enhance.sms.process.aliyun;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.magneton.adaptive.redis.RedissonAdapter;
import org.magneton.core.Result;
import org.magneton.enhance.sms.SendStatus;
import org.magneton.enhance.sms.SmsTemplate;
import org.magneton.enhance.sms.process.SendProcessor;
import org.magneton.enhance.sms.property.SmsProperty;
import org.magneton.enhance.sms.redis.RedissonSmsTemplate;
import org.redisson.api.RedissonClient;

/**
 * .
 *
 * @author zhangmsh 18/03/2022
 * @since 2.0.7
 */
class AliyunSmsProcessorTemplateTest {

	private static SmsTemplate smsTemplate;

	@BeforeAll
	public static void init() {
		SendProcessor sendProcessor = new MockAliyunSendProcessor();
		SmsProperty smsProperty = new SmsProperty();

		try {
			RedissonClient redissonClient = RedissonAdapter.createSingleServerClient();
			smsTemplate = new RedissonSmsTemplate(redissonClient, sendProcessor, smsProperty);
		}
		catch (Exception e) {
			System.out.println("Error: RedissonClient not found.");
		}
	}

	@Test
	void send() {
		if (smsTemplate == null) {
			return;
		}
		String mobile = "13860132592";
		Result<SendStatus> response = smsTemplate.trySend(mobile);
		Assertions.assertTrue(response.isSuccess());

		long ttl = smsTemplate.nextTime(mobile);
		Assertions.assertTrue(ttl > 0, String.valueOf(ttl));

		String token = smsTemplate.getToken(mobile);
		Assertions.assertNotNull(token);

		boolean success = smsTemplate.validate(token, mobile, "error");
		Assertions.assertFalse(success);

		success = smsTemplate.validate(token, mobile, "123456");
		Assertions.assertTrue(success);
	}

}