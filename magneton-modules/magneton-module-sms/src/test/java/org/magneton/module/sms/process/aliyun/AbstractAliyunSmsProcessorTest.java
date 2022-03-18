package org.magneton.module.sms.process.aliyun;

import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.dysmsapi20170525.models.SendSmsResponseBody;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.magneton.adaptive.redis.RedissonAdapter;
import org.magneton.core.Consequences;
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
		AliyunSmsProperty aliyunSmsProperty = new AliyunSmsProperty();
		aliyunSmsProperty.setAccessKeyId("test");
		aliyunSmsProperty.setAccessKeySecret("test");
		aliyunSmsProperty.setSignName("阿里云短信测试");
		aliyunSmsProperty.setTemplateCode("SMS_154950909");

		SendProcessor sendProcessor = new AbstractAliyunSmsProcessor(aliyunSmsProperty) {

			@Override
			protected SendSmsResponse doSend(SendSmsRequest sendSmsRequest) throws Exception {
				SendSmsResponse response = new SendSmsResponse();
				SendSmsResponseBody body = new SendSmsResponseBody();
				body.setCode("ok");
				response.setBody(body);
				return response;
			}
		};
		SmsProperty smsProperty = new SmsProperty();
		sms = new RedissonSms(RedissonAdapter.createSingleServerClient(), sendProcessor, smsProperty);
	}

	@Test
	void send() {
		String mobile = "13860132592";
		Consequences<SendStatus> response = sms.trySend(mobile);
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