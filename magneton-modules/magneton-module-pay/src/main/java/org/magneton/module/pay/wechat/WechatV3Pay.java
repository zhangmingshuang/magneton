package org.magneton.module.pay.wechat;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;

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
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.magneton.core.Consequences;
import org.magneton.core.base.Preconditions;
import org.magneton.core.base.Strings;
import org.magneton.module.pay.Pay;
import org.magneton.module.pay.PreOrderReq;
import org.magneton.module.pay.PreOrderRes;
import org.magneton.module.pay.PrivateKeyNotFoundException;
import org.magneton.module.pay.wechat.entity.WechatPreOrderReq;
import org.magneton.module.pay.wechat.entity.WechatPreOrderRes;

/**
 * 微信支付.
 *
 * 文档地址：{@code https://pay.weixin.qq.com/wiki/doc/apiv3_partner/open/pay/chapter2_5_2.shtml}
 *
 * @author zhangmsh 28/03/2022
 * @since 2.0.7
 */
@Slf4j
public class WechatV3Pay implements Pay<WechatV3Pay> {

	private final ObjectMapper objectMapper = new ObjectMapper();

	private final WechatPayConfig wechatPayConfig;

	private CloseableHttpClient httpClient;

	public WechatV3Pay(WechatPayConfig wechatPayConfig) {
		this.wechatPayConfig = wechatPayConfig;
		this.init();
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

		Verifier verifier = null;
		try {
			verifier = certificatesManager.getVerifier(merchantId);
			// 向证书管理器增加需要自动更新平台证书的商户信息
			// ... 若有多个商户号，可继续调用putMerchant添加商户信息
			certificatesManager.putMerchant(merchantId,
					new WechatPay2Credentials(merchantId,
							new PrivateKeySigner(merchantSerialNumber, merchantPrivateKey)),
					apiV3Key.getBytes(StandardCharsets.UTF_8));
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
				.withValidator(new WechatPay2Validator(verifier));
		// 通过WechatPayHttpClientBuilder构造的HttpClient，会自动的处理签名和验签
		this.httpClient = builder.build();
	}

	@Override
	public WechatV3Pay actualPay() {
		return this;
	}

	// 详见：https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_2_1.shtml
	public Consequences<PreOrderRes> preOrder(WechatPreOrderReq wechatPreOrderReq) {
		Preconditions.checkNotNull(wechatPreOrderReq);

		HttpPost httpPost = this.newHttpPost(WechatV3Url.PRE_ORDER, wechatPreOrderReq);
		// 预支付交易会话标识 prepay_id string[1,64] 预支付交易会话标识。用于后续接口调用中使用，该值有效期为2小时
		// 示例值：wx201410272009395522657a690389285100
		Consequences<WechatPreOrderRes> res = this.doRequest(httpPost, WechatPreOrderRes.class);
		if (!res.isSuccess()) {
			return Consequences.failMessageOnly(res.getMessage());
		}
		WechatPreOrderRes wechatPreOrderRes = res.getData();
		return Consequences
				.success(new PreOrderRes().setPrepayId(Preconditions.checkNotNull(wechatPreOrderRes).getPrepay_id()));
	}

	private <T> Consequences<T> doRequest(HttpPost httpPost, Class<T> type) {
		Preconditions.checkNotNull(httpPost);
		Preconditions.checkNotNull(type);

		// 完成签名并执行请求
		try (CloseableHttpResponse response = this.httpClient.execute(httpPost)) {
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 200) { // 处理成功
				byte[] responseBytes = EntityUtils.toByteArray(response.getEntity());
				return Consequences.success(this.objectMapper.readValue(responseBytes, type));
			}
			else if (statusCode == 204) { // 处理成功，无返回Body
				return Consequences.success(null);
			}
			else {
				// noinspection unchecked
				return Consequences.failMessageOnly(EntityUtils.toString(response.getEntity()));
			}
		}
		catch (IOException e) {
			log.error(Strings.lenientFormat("request %s error", httpPost), e);
		}
		return Consequences.fail();
	}

	@SneakyThrows
	private HttpPost newHttpPost(String url, Object object) {
		String reqData = this.objectMapper.writeValueAsString(object);
		HttpPost httpPost = new HttpPost(url);
		StringEntity entity = new StringEntity(reqData, "utf-8");
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		httpPost.setHeader("Accept", "application/json");
		return httpPost;
	}

	@Override
	public Consequences<PreOrderRes> preOrder(PreOrderReq req) {
		Preconditions.checkNotNull(req);
		WechatPreOrderReq wechatPreOrderReq = new WechatPreOrderReq().setMchid(this.wechatPayConfig.getMerchantId())
				.setAppid(this.wechatPayConfig.getAppId()).setNotify_url(this.wechatPayConfig.getNotifyUrl())
				.setOut_trade_no(req.getOutTradeNo()).setDescription(req.getDescription());

		wechatPreOrderReq.setAmount(new WechatPreOrderReq.Amount().setTotal(req.getAmount()));
		return this.preOrder(wechatPreOrderReq);
	}

}
