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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.magneton.core.Consequences;
import org.magneton.core.base.Preconditions;
import org.magneton.core.base.Strings;
import org.magneton.module.pay.exception.AmountException;
import org.magneton.module.pay.exception.PrivateKeyNotFoundException;
import org.magneton.module.pay.wechat.api._WechatApiPayQueryRes;
import org.magneton.module.pay.wechat.api._WechatApiPreOrderReq;
import org.magneton.module.pay.wechat.api._WechatApiPreOrderRes;
import org.magneton.module.pay.wechat.pojo.WechatOrderQueryReq;
import org.magneton.module.pay.wechat.pojo.WechatOrderQueryRes;
import org.magneton.module.pay.wechat.pojo.WechatPreOrderReq;
import org.magneton.module.pay.wechat.pojo.WechatPreOrderRes;

/**
 * 微信支付.
 *
 * 文档地址：{@code https://pay.weixin.qq.com/wiki/doc/apiv3_partner/open/pay/chapter2_5_2.shtml}
 *
 * @author zhangmsh 28/03/2022
 * @since 2.0.7
 */
@Slf4j
public class WechatV3Pay implements WechatPay {

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
	public Consequences<WechatPreOrderRes> preOrder(WechatPreOrderReq req) {
		Preconditions.checkNotNull(req);
		String outTradeNo = Preconditions.checkNotNull(req.getOutTradeNo());
		String description = Preconditions.checkNotNull(req.getDescription());
		int amount = req.getAmount();
		if (amount < 1) {
			throw new AmountException(Strings.lenientFormat("amount %s less then 1", amount));
		}
		_WechatApiPreOrderReq wechatApiPreOrderReq = new _WechatApiPreOrderReq()
				.setMchid(this.wechatPayConfig.getMerchantId()).setAppid(this.wechatPayConfig.getAppId())
				.setNotify_url(this.wechatPayConfig.getNotifyUrl()).setOut_trade_no(outTradeNo)
				.setDescription(description).setAmount(new _WechatApiPreOrderReq.Amount().setTotal(amount));
		return this.preOrder(wechatApiPreOrderReq);
	}

	@Override
	public Consequences<WechatOrderQueryRes> queryOrder(WechatOrderQueryReq req) {
		// 商户订单号 string[6,32] 是 path 商户系统内部订单号，只能是数字、大小写字母_-*且在同一个商户号下唯一。 特殊规则：最小字符长度为6
		String outTradeNo = Preconditions.checkNotNull(req.getOutTradeNo());
		/**
		 * 服务商户号 sp_mchid string[1,32] 是 query 服务商户号，由微信支付生成并下发 示例值：1230000109 子商户号
		 * sub_mchid string[1,32] 是 query 子商户的商户号，由微信支付生成并下发。 示例值：1900000109
		 **/
		String params = Strings.lenientFormat("sp_mchid=%s&sub_mchid=%s", Preconditions.checkNotNull(req.getSpMchId()),
				Preconditions.checkNotNull(req.getSubMchId()));
		String url = Strings.lenientFormat(WechatV3Url.QUERY_ORDER_OUT_TRADE_NO, outTradeNo);
		HttpGet httpGet = this.newHttpGet(url, params);
		Consequences<_WechatApiPayQueryRes> res = this.doRequest(httpGet, _WechatApiPayQueryRes.class);
		if (!res.isSuccess()) {
			return Consequences.failMessageOnly(res.getMessage());
		}

		return null;
	}

	// 详见：https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_2_1.shtml
	public Consequences<WechatPreOrderRes> preOrder(_WechatApiPreOrderReq wechatApiPreOrderReq) {
		Preconditions.checkNotNull(wechatApiPreOrderReq);

		HttpPost httpPost = this.newHttpPost(WechatV3Url.PRE_ORDER, wechatApiPreOrderReq);
		// 预支付交易会话标识 prepay_id string[1,64] 预支付交易会话标识。用于后续接口调用中使用，该值有效期为2小时
		// 示例值：wx201410272009395522657a690389285100
		Consequences<_WechatApiPreOrderRes> res = this.doRequest(httpPost, _WechatApiPreOrderRes.class);
		if (!res.isSuccess()) {
			return Consequences.failMessageOnly(res.getMessage());
		}
		_WechatApiPreOrderRes wechatApiPreOrderRes = res.getData();
		return Consequences.success(
				new WechatPreOrderRes().setPrepayId(Preconditions.checkNotNull(wechatApiPreOrderRes).getPrepay_id()));
	}

	private <T> Consequences<T> doRequest(HttpUriRequest httpRequest, Class<T> type) {
		Preconditions.checkNotNull(httpRequest);
		Preconditions.checkNotNull(type);

		// 完成签名并执行请求
		try (CloseableHttpResponse response = this.httpClient.execute(httpRequest)) {
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
			log.error(Strings.lenientFormat("request %s error", httpRequest), e);
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

	@SneakyThrows
	private HttpGet newHttpGet(String url, String param) {
		return new HttpGet(url + "?" + param);
	}

}
