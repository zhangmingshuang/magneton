package org.magneton.module.pay.wechat.v3.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.cert.CertificatesManager;
import com.wechat.pay.contrib.apache.httpclient.exception.HttpCodeException;
import com.wechat.pay.contrib.apache.httpclient.exception.NotFoundException;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;
import org.magneton.core.base.Preconditions;
import org.magneton.module.pay.exception.PrivateKeyNotFoundException;

/**
 * @author zhangmsh 2022/4/5
 * @since 1.0.0
 */
@Slf4j
public class DefaultWechatV3PayContext implements WechatV3PayContext {

	private final ObjectMapper objectMapper = new ObjectMapper();

	private final WechatPayConfig wechatPayConfig;

	private Verifier verifier;

	private CloseableHttpClient httpClient;

	public DefaultWechatV3PayContext(WechatPayConfig wechatPayConfig) {
		this.wechatPayConfig = Preconditions.checkNotNull(wechatPayConfig);
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
	public CloseableHttpClient getHttpClient() {
		return this.httpClient;
	}

	@Override
	public WechatPayConfig getPayConfig() {
		return this.wechatPayConfig;
	}

	protected void init() {
		String merchantId = Preconditions.checkNotNull(this.wechatPayConfig.getMerchantId(),
				"wechat pay merchantId is null.");
		String merchantSerialNumber = Preconditions.checkNotNull(this.wechatPayConfig.getMerchantSerialNumber(),
				"wechat pay merchant serial number is null.");
		String merchantPrivateKeyFile = Preconditions.checkNotNull(this.wechatPayConfig.getMerchantPrivateKeyFile(),
				"wechat pay merchant private key file is null.");
		String apiV3Key = Preconditions.checkNotNull(this.wechatPayConfig.getApiV3Key(),
				"wechat pay api v3 key is null");
		PrivateKey merchantPrivateKey = null;
		try {
			merchantPrivateKey = PemUtil.loadPrivateKey(new FileInputStream(merchantPrivateKeyFile));
		}
		catch (FileNotFoundException e) {
			log.error("wechat pay merchant private key not found.", e);
			throw new PrivateKeyNotFoundException(e);
		}
		// 获取证书管理器实例
		CertificatesManager certificatesManager = CertificatesManager.getInstance();

		try {
			// 向证书管理器增加需要自动更新平台证书的商户信息
			// ... 若有多个商户号，可继续调用putMerchant添加商户信息
			certificatesManager.putMerchant(merchantId,
					new WechatPay2Credentials(merchantId,
							new PrivateKeySigner(merchantSerialNumber, merchantPrivateKey)),
					apiV3Key.getBytes(StandardCharsets.UTF_8));
			this.verifier = certificatesManager.getVerifier(merchantId);
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
				.withValidator(new WechatPay2Validator(this.verifier));
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
