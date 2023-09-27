package org.magneton.module.sms.process.aliyun;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.magneton.adaptive.redis.RedissonAdapter;
import org.magneton.core.Result;
import org.magneton.module.sms.SendStatus;
import org.magneton.module.sms.SmsTemplate;
import org.magneton.module.sms.process.SendProcessor;
import org.magneton.module.sms.property.SmsProperty;
import org.magneton.module.sms.redis.RedissonSmsTemplate;

/**
 * .
 *
 * @author zhangmsh 18/03/2022
 * @since 2.0.7
 */
class AbstractAliyunSmsProcessorTestTemplate {

	private static SmsTemplate smsTemplate;

	@BeforeAll
	public static void init() {
		SendProcessor sendProcessor = new MockAliyunSendProcessor();
		SmsProperty smsProperty = new SmsProperty();
		smsTemplate = new RedissonSmsTemplate(RedissonAdapter.createSingleServerClient(), sendProcessor, smsProperty);
	}

	@Test
	void send() {
		String mobile = "13860132592";
		Result<SendStatus> response = smsTemplate.trySend(mobile);
		Assertions.assertTrue(response.isSuccess());

		long ttl = smsTemplate.ttl(mobile);
		Assertions.assertTrue(ttl > 0, String.valueOf(ttl));

		String token = smsTemplate.token(mobile);
		Assertions.assertNotNull(token);

		boolean error = smsTemplate.validate(token, mobile, "error");
		Assertions.assertFalse(error);

		boolean success = smsTemplate.validate(token, mobile, "123456");
		Assertions.assertTrue(success);
	}

}