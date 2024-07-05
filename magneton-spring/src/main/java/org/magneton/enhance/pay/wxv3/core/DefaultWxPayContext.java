package org.magneton.enhance.pay.wxv3.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.cert.CertificatesManager;
import com.wechat.pay.contrib.apache.httpclient.exception.HttpCodeException;
import com.wechat.pay.contrib.apache.httpclient.exception.NotFoundException;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;
import org.magneton.enhance.pay.exception.PrivateKeyNotFoundException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;

/**
 * @author zhangmsh 2022/4/5
 * @since 1.0.0
 */
@Slf4j
public class DefaultWxPayContext implements WxPayContext {

	private final ObjectMapper objectMapper = new ObjectMapper();

	private final WxPayConfig wxPayConfig;

	private Verifier verifier;

	private PrivateKeySigner privateKeySigner;

	private WechatPay2Validator wechatPay2Validator;

	private CloseableHttpClient httpClient;

	public DefaultWxPayContext(WxPayConfig wxPayConfig) {
		this.wxPayConfig = Preconditions.checkNotNull(wxPayConfig);
		this.init();
		Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
	}

	@Override
	public ObjectMapper getObjectMapper() {
		return this.objectMapper;
	}

	@Override
	public Verifier getVerifier() {
		return this.verifier;
	}

	@Override
	public PrivateKeySigner getPrivateKeySigner() {
		return this.privateKeySigner;
	}

	@Override
	public WechatPay2Validator getWechatPay2Validator() {
		return this.wechatPay2Validator;
	}

	@Override
	public CloseableHttpClient getHttpClient() {
		return this.httpClient;
	}

	@Override
	public WxPayConfig getPayConfig() {
		return this.wxPayConfig;
	}

	protected void init() {
		Preconditions.checkNotNull(this.wxPayConfig.getAppId(), "wechat pay appId must not be null");

		String merchantId = Preconditions.checkNotNull(this.wxPayConfig.getMerchantId(),
				"wechat pay merchantId is null.");
		String merchantSerialNumber = Preconditions.checkNotNull(this.wxPayConfig.getMerchantSerialNumber(),
				"wechat pay merchant serial number is null.");
		String merchantPrivateKeyFile = Preconditions.checkNotNull(this.wxPayConfig.getMerchantPrivateKeyFile(),
				"wechat pay merchant private key file is null.");
		String apiV3Key = Preconditions.checkNotNull(this.wxPayConfig.getApiV3Key(), "wechat pay api v3 key is null");
		PrivateKey merchantPrivateKey = null;
		try {
			merchantPrivateKey = PemUtil.loadPrivateKey(new FileInputStream(merchantPrivateKeyFile));
		}
		catch (FileNotFoundException e) {
			log.error("wechat pay merchant private key not found.", e);
			throw new PrivateKeyNotFoundException(e);
		}
		// 获取证书管理器实例,它会定时下载和更新商户对应的微信支付平台证书 （默认下载间隔为UPDATE_INTERVAL_MINUTE）。
		CertificatesManager certificatesManager = CertificatesManager.getInstance();

		try {
			// 向证书管理器增加需要自动更新平台证书的商户信息
			// ... 若有多个商户号，可继续调用putMerchant添加商户信息
			this.privateKeySigner = new PrivateKeySigner(merchantSerialNumber, merchantPrivateKey);
			certificatesManager.putMerchant(merchantId, new WechatPay2Credentials(merchantId, this.privateKeySigner),
					apiV3Key.getBytes(StandardCharsets.UTF_8));
			// 从证书管理器中获取verifier
			this.verifier = certificatesManager.getVerifier(merchantId);
			this.wechatPay2Validator = new WechatPay2Validator(this.verifier);
		}
		catch (NotFoundException e) {
			log.error("wechat pay merchant get verifier error.", e);
			throw new PrivateKeyNotFoundException(e);
		}
		catch (GeneralSecurityException | HttpCodeException | IOException e) {
			log.error("wechat pay general security error.", e);
			throw new PrivateKeyNotFoundException(e);
		}
		WechatPayHttpClientBuilder builder = WechatPayHttpClientBuilder.create()
				.withMerchant(merchantId, merchantSerialNumber, merchantPrivateKey)
				.withValidator(this.wechatPay2Validator);
		// 通过WechatPayHttpClientBuilder构造的HttpClient，会自动的处理签名和验签
		this.httpClient = builder.build();
	}

	protected void shutdown() {
		if (this.httpClient != null) {
			try {
				this.httpClient.close();
			}
			catch (IOException e) {
				// ignore
			}
		}
	}

}
