package org.magneton.enhance.sms.process.aliyun;

import com.alibaba.fastjson2.JSON;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.dysmsapi20170525.models.SendSmsResponseBody;
import com.aliyun.teaopenapi.models.Config;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.magneton.enhance.Result;
import org.magneton.enhance.sms.entity.SmsInitException;
import org.magneton.enhance.sms.entity.SmsToken;
import org.magneton.enhance.sms.process.SendProcessor;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 阿里云验证码发送处理器.
 * <p>
 * 详见： {@code https://next.api.aliyun.com/api/Dysmsapi/2017-05-25/SendSms}
 * {@code https://help.aliyun.com/document_detail/101346.html?spm=api-workbench.API%20Explorer.0.0.56e11e0f4Hu8Iw}
 *
 * @author zhangmsh 16/03/2022
 * @since 2.0.7
 */
@Slf4j
@Getter
public class AliyunSmsProcessor implements SendProcessor {

	private final AliyunSmsProperty aliyunSmsProperty;

	private Client client;

	public AliyunSmsProcessor(AliyunSmsProperty aliyunSmsProperty) {
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
			throw new SmsInitException(e);
		}
	}

	@Override
	public Result<SmsToken> send(String mobile) {
		try {
			AliyunSms aliyunSms = this.createTemplate(mobile, this.getAliyunSmsProperty().getTemplateCode());
			SendSmsRequest sendSmsRequest = this.createSendSmsRequest(mobile, aliyunSms);
			SendSmsResponse sendSmsResponse = this.doSend(sendSmsRequest);
			if (log.isDebugEnabled()) {
				log.debug("send to {}, response: {}", mobile, sendSmsResponse);
			}
			SendSmsResponseBody body = sendSmsResponse.getBody();
			return this.doSendResponseProcess(aliyunSms.getCode(), sendSmsResponse,
					"ok".equalsIgnoreCase(body.getCode()));
		}
		catch (Exception e) {
			this.onSendException(e, mobile);
		}
		return Result.fail();
	}

	protected SendSmsResponse doSend(SendSmsRequest sendSmsRequest) throws Exception {
		return this.client.sendSms(sendSmsRequest);
	}

	/**
	 * 短信发送响应处理
	 * @param sendSmsResponse 短信响应信息
	 * @return 处理结果
	 */
	protected Result<SmsToken> doSendResponseProcess(String code, SendSmsResponse sendSmsResponse, boolean isSuccess) {
		if (isSuccess) {
			return Result.successWith(new SmsToken(UUID.randomUUID().toString(), code));
		}
		SendSmsResponseBody body = sendSmsResponse.getBody();
		log.error("send sms error: {} -- {} ", body.getCode(), body.getMessage());
		return Result.fail();
	}

	protected AliyunSms createTemplate(String mobile, String templateCode) {
		return new AliyunSms(String.valueOf(ThreadLocalRandom.current().nextInt(1000, 9999)));
	}

	/**
	 * 发送短信异常
	 * @param e 异常
	 * @param mobile 手机号
	 */
	protected void onSendException(Exception e, String mobile) {
		log.error(Strings.lenientFormat("send sms %s error", mobile), e);
	}

	protected SendSmsRequest createSendSmsRequest(String mobile, AliyunSms aliyunSms) {
		String templateParam = this.templateParamAmend(aliyunSms, this.aliyunSmsProperty.getTemplateCode());
		return new SendSmsRequest().setSignName(this.aliyunSmsProperty.getSignName())
				.setTemplateCode(this.aliyunSmsProperty.getTemplateCode()).setPhoneNumbers(mobile)
				.setTemplateParam(templateParam);
	}

	protected String templateParamAmend(AliyunSms aliyunSms, String templateCode) {
		String code = Preconditions.checkNotNull(aliyunSms.getCode());
		Map<String, String> addition = aliyunSms.getAddition();
		if (addition == null) {
			addition = Maps.newHashMapWithExpectedSize(1);
		}
		addition.put("code", code);
		return JSON.toJSONString(addition);
	}

}
