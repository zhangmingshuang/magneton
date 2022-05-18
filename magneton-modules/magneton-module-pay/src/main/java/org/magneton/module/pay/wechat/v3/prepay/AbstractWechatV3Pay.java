package org.magneton.module.pay.wechat.v3.prepay;

import com.wechat.pay.contrib.apache.httpclient.auth.Signer.SignatureResult;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
import org.magneton.module.pay.wechat.v3.core.WxPayContext;

/**
 * @author zhangmsh 2022/4/5
 * @since 1.0.0
 */
@Slf4j
public class AbstractWechatV3Pay implements WechatBaseV3Pay {

	private final WxPayContext payContext;

	public AbstractWechatV3Pay(WxPayContext payContext) {
		this.payContext = payContext;
	}

	@Override
	public WxPayContext getPayContext() {
		return this.payContext;
	}

	// 详见：https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_2_1.shtml
	@Override
	public <T> Consequences<T> doPreOrder(String url, Object req, Class<T> clazz) {
		Preconditions.checkNotNull(req);

		HttpPost httpPost = this.newHttpPost(url, req);
		// 预支付交易会话标识 prepay_id string[1,64] 预支付交易会话标识。用于后续接口调用中使用，该值有效期为2小时
		// 示例值：wx201410272009395522657a690389285100
		Consequences<T> res = this.doRequest(httpPost, clazz);
		if (!res.isSuccess()) {
			return Consequences.failMessageOnly(res.getMessage());
		}
		T prepayData = res.getData();
		return Consequences.success(prepayData);
	}

	@Override
	public String doSign(String signStr) {
		Preconditions.checkNotNull(signStr);
		SignatureResult sign = this.getPayContext().getPrivateKeySigner()
				.sign(signStr.getBytes(StandardCharsets.UTF_8));
		return sign.getSign();
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
				return Consequences.success(WechatBaseV3Pay.json().readValue(responseBytes, type));
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
		String reqData = WechatBaseV3Pay.json().writeValueAsString(object);
		if (log.isDebugEnabled()) {
			log.debug("post url :{}\ndata:{}\n", url, reqData);
		}
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
