package org.magneton.module.pay.wechat.v3.core;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;

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
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;
import org.magneton.module.pay.exception.PrivateKeyNotFoundException;

/**
 * @author zhangmsh 2022/4/5
 * @since 1.0.0
 */
@Slf4j
public class DefaultWxPayContext implements WxPayContext {

	private final ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * 默认的微信支付属性配置
	 */
	private final WxPayConfig wxPayConfig;

	private WxPaySession wxPaySession;

	public DefaultWxPayContext(WxPayConfig wxPayConfig) {
		this.wxPayConfig = Preconditions.checkNotNull(wxPayConfig);
		this.validAndInit();
		Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
	}

	@SneakyThrows
	// private void parseConfigs(WxPayConfig wxPayConfig) {
	// Field[] fields = wxPayConfig.getClass().getDeclaredFields();
	// if (fields.length < 1) {
	// return;
	// }
	// for (Field field : fields) {
	// WxPayConfigIdStatement wxPayConfigIdStatement =
	// field.getAnnotation(WxPayConfigIdStatement.class);
	// if (wxPayConfigIdStatement == null) {
	// continue;
	// }
	// field.setAccessible(true);
	// wxPayConfig config = (wxPayConfig) field.get(wxPayConfig);
	// if (config == null) {
	// continue;
	// }
	// WxPayType id = wxPayConfigIdStatement.value();
	// if (id == null) {
	// throw new IllegalArgumentException("@WxPayConfigId#id不能为空");
	// }
	// wxPayConfig exist = this.wxPayConfigs.put(id, config);
	// if (exist != null) {
	// throw new DuplicateFoundException(config, exist);
	// }
	// }
	// }

	@Override
	public ObjectMapper getObjectMapper() {
		return this.objectMapper;
	}

	@Override
	public Verifier getVerifier() {
		return this.wxPaySession.getVerifier();
	}

	@Override
	public PrivateKeySigner getPrivateKeySigner() {
		return this.wxPaySession.getPrivateKeySigner();
	}

	@Override
	public WechatPay2Validator getWechatPay2Validator() {
		return this.wxPaySession.getWechatPay2Validator();
	}

	@Override
	public CloseableHttpClient getHttpClient() {
		return this.wxPaySession.getHttpClient();
	}

	@Override
	public WxPayConfig getPayConfig() {
		return this.wxPayConfig;
	}

	protected void validAndInit() {
		// 获取证书管理器实例,它会定时下载和更新商户对应的微信支付平台证书 （默认下载间隔为UPDATE_INTERVAL_MINUTE）。
		CertificatesManager certificatesManager = CertificatesManager.getInstance();

		String merchantId = this.wxPayConfig.getMerchantId();

		try {
			if (certificatesManager.getVerifier(merchantId) != null) {
				// merchantId已经初始化，不需要重复初始化
				return;
			}
		}
		catch (NotFoundException e) {
			// ignore
		}

		String merchantSerialNumber = this.wxPayConfig.getMerchantSerialNumber();
		String merchantPrivateKeyFile = this.wxPayConfig.getMerchantPrivateKeyFile();
		String apiV3Key = this.wxPayConfig.getApiV3Key();

		Preconditions.checkNotNull(merchantId, "wechat pay merchantId must not be null");
		Preconditions.checkNotNull(merchantSerialNumber, "wechat pay merchantSerialNumber must not be null");
		Preconditions.checkNotNull(merchantPrivateKeyFile, "wechat pay merchantPrivateKeyFile must not be null");
		Preconditions.checkNotNull(apiV3Key, "wechat pay apiV3Key must not be null");

		PrivateKey merchantPrivateKey = null;
		try {
			merchantPrivateKey = PemUtil.loadPrivateKey(new FileInputStream(merchantPrivateKeyFile));
		}
		catch (FileNotFoundException e) {
			log.error("wechat pay merchant private key not found.", e);
			throw new PrivateKeyNotFoundException(e);
		}
		// 向证书管理器增加需要自动更新平台证书的商户信息
		// ... 若有多个商户号，可继续调用putMerchant添加商户信息
		PrivateKeySigner privateKeySigner = new PrivateKeySigner(merchantSerialNumber, merchantPrivateKey);
		try {
			certificatesManager.putMerchant(merchantId, new WechatPay2Credentials(merchantId, privateKeySigner),
					apiV3Key.getBytes(StandardCharsets.UTF_8));
		}
		catch (IOException | GeneralSecurityException | HttpCodeException e) {
			throw new RuntimeException(e);
		}

		try {
			// 从证书管理器中获取verifier
			Verifier verifier = certificatesManager.getVerifier(merchantId);
			WechatPay2Validator wechatPay2Validator = new WechatPay2Validator(verifier);
			WechatPayHttpClientBuilder builder = WechatPayHttpClientBuilder.create()
					.withMerchant(merchantId, merchantSerialNumber, merchantPrivateKey)
					.withValidator(wechatPay2Validator);
			// 通过WechatPayHttpClientBuilder构造的HttpClient，会自动的处理签名和验签
			this.wxPaySession = new WxPaySession(privateKeySigner, verifier, wechatPay2Validator, builder.build());
		}
		catch (NotFoundException e) {
			log.error("wechat pay merchant get verifier error.", e);
			throw new PrivateKeyNotFoundException(e);
		}
	}

	protected void shutdown() {
		CloseableHttpClient httpClient = this.wxPaySession.getHttpClient();
		if (httpClient != null) {
			try {
				httpClient.close();
			}
			catch (IOException e) {
				// ignore
			}
		}
	}

}
