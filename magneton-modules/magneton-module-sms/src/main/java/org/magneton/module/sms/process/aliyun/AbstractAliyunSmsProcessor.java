package org.magneton.module.sms.process.aliyun;

import java.util.Map;
import java.util.UUID;

import com.alibaba.fastjson.JSON;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.dysmsapi20170525.models.SendSmsResponseBody;
import com.aliyun.teaopenapi.models.Config;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.magneton.core.Consequences;
import org.magneton.core.base.Preconditions;
import org.magneton.core.base.Strings;
import org.magneton.core.collect.Maps;
import org.magneton.foundation.exception.InitializationException;
import org.magneton.module.sms.entity.SmsToken;
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
public abstract class AbstractAliyunSmsProcessor implements SendProcessor {

	@Getter
	private final AliyunSmsProperty aliyunSmsProperty;

	@Getter
	private Client client;

	public AbstractAliyunSmsProcessor(AliyunSmsProperty aliyunSmsProperty) {
		this.aliyunSmsProperty = aliyunSmsProperty;
		this.init();
	}

	protected void init() {
		Config config = new Config()
				// 您的AccessKey ID
				.setAccessKeyId(this.aliyunSmsProperty.getAccessKeyId())
				// 您的AccessKey Secret
				.setAccessKeySecret(this.aliyunSmsProperty.getAccessKeySecret())
				.setEndpoint(this.aliyunSmsProperty.getEndpoint());
		try {
			this.client = new Client(config);
		}
		catch (Exception e) {
			throw new InitializationException(e);
		}
	}

	@Override
	public Consequences<SmsToken> send(String mobile) {
		try {
			AliyunSmsTemplate aliyunSmsTemplate = this.createTemplate(mobile);
			SendSmsRequest sendSmsRequest = this.createSendSmsRequest(mobile, aliyunSmsTemplate);
			SendSmsResponse sendSmsResponse = this.doSend(sendSmsRequest);
			if (log.isDebugEnabled()) {
				log.debug("send to {}, response: {}", mobile, sendSmsResponse);
			}
			SendSmsResponseBody body = sendSmsResponse.getBody();
			return this.doSendResponseProcess(aliyunSmsTemplate.getCode(), sendSmsResponse,
					"ok".equalsIgnoreCase(body.getCode()));
		}
		catch (Exception e) {
			this.onSendException(e, mobile);
		}
		return Consequences.fail();
	}

	protected SendSmsResponse doSend(SendSmsRequest sendSmsRequest) throws Exception {
		return this.client.sendSms(sendSmsRequest);
	}

	/**
	 * 短信发送响应处理
	 * @param sendSmsResponse 短信响应信息
	 * @return 处理结果
	 */
	protected Consequences<SmsToken> doSendResponseProcess(String code, SendSmsResponse sendSmsResponse,
			boolean isSuccess) {
		if (isSuccess) {
			return Consequences.success(new SmsToken(UUID.randomUUID().toString(), code));
		}
		return Consequences.fail();
	}

	protected abstract AliyunSmsTemplate createTemplate(String mobile);

	/**
	 * 发送短信异常
	 * @param e 异常
	 * @param mobile 手机号
	 */
	protected void onSendException(Exception e, String mobile) {
		log.error(Strings.lenientFormat("send sms %s error", mobile), e);
	}

	protected SendSmsRequest createSendSmsRequest(String mobile, AliyunSmsTemplate aliyunSmsTemplate) {
		String templateParam = this.templateParamAmend(aliyunSmsTemplate, this.aliyunSmsProperty.getTemplateCode());
		return new SendSmsRequest().setSignName(this.aliyunSmsProperty.getSignName())
				.setTemplateCode(this.aliyunSmsProperty.getTemplateCode()).setPhoneNumbers(mobile)
				.setTemplateParam(templateParam);
	}

	protected String templateParamAmend(AliyunSmsTemplate aliyunSmsTemplate, String templateCode) {
		String code = Preconditions.checkNotNull(aliyunSmsTemplate.getCode());
		Map<String, String> addition = aliyunSmsTemplate.getAddition();
		if (addition == null) {
			addition = Maps.newHashMapWithExpectedSize(1);
		}
		addition.put(code, code);
		return JSON.toJSONString(addition);
	}

}
