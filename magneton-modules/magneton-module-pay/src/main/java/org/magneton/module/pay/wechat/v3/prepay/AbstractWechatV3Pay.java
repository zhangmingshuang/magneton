package org.magneton.module.pay.wechat.v3.prepay;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wechat.pay.contrib.apache.httpclient.util.RsaCryptoUtil;
import java.io.IOException;
import java.security.cert.X509Certificate;
import javax.crypto.IllegalBlockSizeException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.magneton.core.Consequences;
import org.magneton.core.base.Preconditions;
import org.magneton.core.base.Strings;
import org.magneton.foundation.exception.ProcessException;
import org.magneton.module.pay.wechat.v3.core.WechatPayConfig;
import org.magneton.module.pay.wechat.v3.core.WechatV3PayContext;
import org.magneton.module.pay.wechat.v3.entity.WechatV3PayPreOrderReq;
import org.magneton.module.pay.wechat.v3.entity.WechatV3PayPreOrderRes;

/**
 * @author zhangmsh 2022/4/5
 * @since 1.0.0
 */
@Slf4j
public class AbstractWechatV3Pay implements WechatBaseV3Pay {

	private static final ObjectMapper JSON = new ObjectMapper();

	private final WechatV3PayContext payContext;

	public AbstractWechatV3Pay(WechatV3PayContext payContext) {
		this.payContext = payContext;
	}

	public WechatV3PayContext getPayContext() {
		return this.payContext;
	}

	// 详见：https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_2_1.shtml
	@Override
	public Consequences<WechatV3PayPreOrderRes> doPreOrder(WechatV3PayPreOrderReq wechatApiPreOrderReq) {
		Preconditions.checkNotNull(wechatApiPreOrderReq);

		HttpPost httpPost = this.newHttpPost("https://api.mch.weixin.qq.com/v3/pay/transactions/app",
				wechatApiPreOrderReq);
		// 预支付交易会话标识 prepay_id string[1,64] 预支付交易会话标识。用于后续接口调用中使用，该值有效期为2小时
		// 示例值：wx201410272009395522657a690389285100
		Consequences<WechatV3PayPreOrderRes> res = this.doRequest(httpPost, WechatV3PayPreOrderRes.class);
		if (!res.isSuccess()) {
			return Consequences.failMessageOnly(res.getMessage());
		}
		WechatV3PayPreOrderRes wechatApiPreOrderRes = res.getData();
		return Consequences.success(wechatApiPreOrderRes);
	}

	@Override
	public WechatPayConfig getPayConfig() {
		return this.getPayContext().getPayConfig();
	}

	@Override
	public String doSign(String signStr) {
		Preconditions.checkNotNull(signStr);

		X509Certificate validCertificate = this.getPayContext().getVerifier().getValidCertificate();
		try {
			return RsaCryptoUtil.encryptOAEP(signStr, validCertificate);
		}
		catch (IllegalBlockSizeException e) {
			throw new ProcessException(e);
		}
	}

	@Override
	public <T> Consequences<T> doRequest(HttpUriRequest httpRequest, Class<T> type) {
		Preconditions.checkNotNull(httpRequest);
		Preconditions.checkNotNull(type);

		// 完成签名并执行请求
		try (CloseableHttpResponse response = this.getPayContext().getHttpClient().execute(httpRequest)) {
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 200) { // 处理成功
				byte[] responseBytes = EntityUtils.toByteArray(response.getEntity());
				return Consequences.success(JSON.readValue(responseBytes, type));
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
	@Override
	public HttpPost newHttpPost(String url, Object object) {
		Preconditions.checkNotNull(url);
		Preconditions.checkNotNull(object);
		if (object instanceof BasePayIdData) {
			BasePayIdData basePayIdData = (BasePayIdData) object;
			basePayIdData.setAppid(this.getPayContext().getPayConfig().getAppId());
			basePayIdData.setMchid(this.getPayContext().getPayConfig().getMerchantId());
		}
		String reqData = JSON.writeValueAsString(object);
		HttpPost httpPost = new HttpPost(url);
		StringEntity entity = new StringEntity(reqData, "utf-8");
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		httpPost.setHeader("Accept", "application/json");
		return httpPost;
	}

	@SneakyThrows
	@Override
	public HttpGet newHttpGet(String url, String param) {
		Preconditions.checkNotNull(url);
		if (!Strings.isNullOrEmpty(param)) {
			url += "?" + param;
		}
		return new HttpGet(url);
	}

}
