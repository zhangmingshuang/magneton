package org.magneton.module.sms.process.aliyun;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.dysmsapi20170525.models.SendSmsResponseBody;
import com.aliyun.teaopenapi.models.Config;
import lombok.extern.slf4j.Slf4j;
import org.magneton.core.Consequences;
import org.magneton.core.base.Strings;
import org.magneton.core.base.Verify;
import org.magneton.foundation.exception.InitializationException;
import org.magneton.module.sms.SendStatus;
import org.magneton.module.sms.process.SendProcessor;

/**
 * 阿里云验证码.
 *
 * 详见： {@code https://next.api.aliyun.com/api/Dysmsapi/2017-05-25/SendSms}
 * {@code https://help.aliyun.com/document_detail/101346.html?spm=api-workbench.API%20Explorer.0.0.56e11e0f4Hu8Iw}
 *
 * @author zhangmsh 16/03/2022
 * @since 2.0.7
 */
@Slf4j
public abstract class AbstractAliyunSendProcessor implements SendProcessor {

	private final AliyunProperty aliyunProperty;

	private Client client;

	public AbstractAliyunSendProcessor(AliyunProperty aliyunProperty) {
		this.aliyunProperty = aliyunProperty;
		this.init();
	}

	protected void init() {
		Config config = new Config()
				// 您的AccessKey ID
				.setAccessKeyId(this.aliyunProperty.getAccessKeyId())
				// 您的AccessKey Secret
				.setAccessKeySecret(this.aliyunProperty.getAccessKeySecret())
				.setEndpoint(this.aliyunProperty.getEndpoint());
		try {
			this.client = new Client(config);
		}
		catch (Exception e) {
			throw new InitializationException(e);
		}
	}

	@Override
	public Consequences<SendStatus> send(String mobile, String context) {
		SendSmsRequest sendSmsRequest = this.createSendSmsRequest(mobile, context);
		Verify.verify(sendSmsRequest != null);
		try {
			SendSmsResponse sendSmsResponse = this.client.sendSms(sendSmsRequest);
			return this.doSendResponseProcess(sendSmsResponse);
		}
		catch (Exception e) {
			this.onSendException(e, mobile, context);
		}
		return Consequences.fail(SendStatus.FAILURE, "发送短信失败");
	}

	/**
	 * 短信发送响应处理
	 * @param sendSmsResponse 短信响应信息
	 * @return 处理结果
	 */
	protected Consequences<SendStatus> doSendResponseProcess(SendSmsResponse sendSmsResponse) {
		SendSmsResponseBody body = sendSmsResponse.getBody();
		if ("ok".equalsIgnoreCase(body.getCode())) {
			return Consequences.success(SendStatus.SUCCESS, "发送短信成功");
		}
		return Consequences.fail(SendStatus.FAILURE, body.getMessage());
	}

	/**
	 * 发送短信异常
	 * @param e 异常
	 * @param mobile 手机号
	 * @param context 内容
	 */
	protected void onSendException(Exception e, String mobile, String context) {
		log.error(Strings.lenientFormat("send sms %s, %s error", mobile, context), e);
	}

	protected abstract SendSmsRequest createSendSmsRequest(String mobile, String context);

}
