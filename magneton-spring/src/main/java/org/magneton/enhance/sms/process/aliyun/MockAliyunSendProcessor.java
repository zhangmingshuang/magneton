package org.magneton.enhance.sms.process.aliyun;

import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.dysmsapi20170525.models.SendSmsResponseBody;

/**
 * @author zhangmsh 2022/3/24
 * @since 1.0.0
 */
public class MockAliyunSendProcessor extends AliyunSmsProcessor {

	public MockAliyunSendProcessor() {
		super(mock());
	}

	private static AliyunSmsProperty mock() {
		AliyunSmsProperty aliyunSmsProperty = new AliyunSmsProperty();
		aliyunSmsProperty.setAccessKeyId("test");
		aliyunSmsProperty.setAccessKeySecret("test");
		aliyunSmsProperty.setSignName("阿里云短信测试");
		aliyunSmsProperty.setTemplateCode("SMS_154950909");
		return aliyunSmsProperty;
	}

	@Override
	protected SendSmsResponse doSend(SendSmsRequest sendSmsRequest) throws Exception {
		SendSmsResponse response = new SendSmsResponse();
		SendSmsResponseBody body = new SendSmsResponseBody();
		body.setCode("ok");
		response.setBody(body);
		return response;
	}

}
