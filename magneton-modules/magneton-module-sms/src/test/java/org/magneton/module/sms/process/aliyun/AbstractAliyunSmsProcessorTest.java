package org.magneton.module.sms.process.aliyun;

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
		aliyunSmsProperty.setAccessKeyId("LTAI5tPDtZRbxzNiJNgtmugQ");
		aliyunSmsProperty.setAccessKeySecret("iLNTjQeryeXpw3LGFy1EeUQVWnC0QL");
		aliyunSmsProperty.setSignName("阿里云短信测试");
		aliyunSmsProperty.setTemplateCode("SMS_154950909");

		SendProcessor sendProcessor = new AbstractAliyunSmsProcessor(aliyunSmsProperty) {
			@Override
			protected AliyunSmsTemplate createTemplate(String mobile) {
				return new AliyunSmsTemplate("123456");
			}
		};
		SmsProperty smsProperty = new SmsProperty();
		sms = new RedissonSms(RedissonAdapter.createSingleServerClient(), sendProcessor, smsProperty);
	}

	@Test
	void send() {
		Consequences<SendStatus> response = sms.trySend("13860132592");
		System.out.println(response);
	}

}