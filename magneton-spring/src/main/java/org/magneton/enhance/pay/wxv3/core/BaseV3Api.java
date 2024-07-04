package org.magneton.enhance.pay.wxv3.core;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.wechat.pay.contrib.apache.httpclient.auth.Signer.SignatureResult;
import lombok.SneakyThrows;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.magneton.enhance.Result;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author zhangmsh 2022/6/5
 * @since 1.0.0
 */
public interface BaseV3Api {

	@Nullable
	default Logger getLogger() {
		return null;
	}

	WxPayContext getPayContext();

	default WxPayConfig getPayConfig() {
		return this.getPayContext().getPayConfig();
	}

	default String doSign(String signStr) {
		Preconditions.checkNotNull(signStr);
		SignatureResult sign = this.getPayContext()
			.getPrivateKeySigner()
			.sign(signStr.getBytes(StandardCharsets.UTF_8));
		return sign.getSign();
	}

	default <T> Result<T> doRequest(HttpUriRequest httpRequest, Class<T> type) {
		Preconditions.checkNotNull(httpRequest);
		Preconditions.checkNotNull(type);

		// 完成签名并执行请求
		Logger logger = this.getLogger();
		try (CloseableHttpResponse response = this.getPayContext().getHttpClient().execute(httpRequest)) {
			int statusCode = response.getStatusLine().getStatusCode();
			if (logger != null && logger.isDebugEnabled()) {
				logger.debug("request url: {}, status code: {}", httpRequest.getURI(), statusCode);
			}
			if (statusCode == 200) { // 处理成功
				byte[] responseBytes = EntityUtils.toByteArray(response.getEntity());
				return Result.successWith(BaseV3Api.json().readValue(responseBytes, type));
			}
			else if (statusCode == 204) { // 处理成功，无返回Body
				return Result.success();
			}
			else {
				// noinspection unchecked
				return Result.failBy(EntityUtils.toString(response.getEntity()));
			}
		}
		catch (IOException e) {
			if (logger != null) {
				logger.error(Strings.lenientFormat("request %s error", httpRequest), e);
			}
			else {
				throw new RuntimeException(e);
			}
		}
		return Result.fail();
	}

	@SneakyThrows
	default HttpPost newHttpPost(String url, Object object) {
		Preconditions.checkNotNull(url);
		Preconditions.checkNotNull(object);
		if (object instanceof BaseV3PayIdData) {
			BaseV3PayIdData basePayIdData = (BaseV3PayIdData) object;
			basePayIdData.setAppid(this.getPayContext().getPayConfig().getAppId());
			basePayIdData.setMchid(this.getPayContext().getPayConfig().getMerchantId());
		}
		else if (object instanceof BaseV3Data) {
			BaseV3Data baseV3Data = (BaseV3Data) object;
			baseV3Data.setAppid(this.getPayContext().getPayConfig().getAppId());
		}
		String reqData = BaseV3Api.json().writeValueAsString(object);
		Logger logger = this.getLogger();
		if (logger != null && logger.isDebugEnabled()) {
			logger.debug("post url :{}\ndata:{}\n", url, reqData);
		}
		HttpPost httpPost = new HttpPost(url);
		StringEntity entity = new StringEntity(reqData, "utf-8");
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		httpPost.setHeader("Accept", "application/json");
		return httpPost;
	}

	@SneakyThrows
	default HttpGet newHttpGet(String url) {
		return this.newHttpGet(url, null);
	}

	@SneakyThrows
	default HttpGet newHttpGet(String url, @Nullable String param) {
		Preconditions.checkNotNull(url);
		if (!Strings.isNullOrEmpty(param)) {
			url += "?" + param;
		}
		return new HttpGet(url);
	}

	static WxPayJson json() {
		return WxPayJson.getInstance();
	}

}
