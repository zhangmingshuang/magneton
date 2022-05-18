package org.magneton.module.pay.wechat.v3.prepay;

import javax.annotation.Nullable;
import lombok.SneakyThrows;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.magneton.core.Consequences;
import org.magneton.module.pay.wechat.v3.core.WxPayContext;
import org.magneton.module.pay.wechat.v3.core.WxPayJson;

/**
 *
 * 签名工具：https://pay.weixin.qq.com/wiki/doc/apiv3/wechatpay/wechatpay6_0.shtml
 *
 * @author zhangmsh 2022/4/5
 * @since 1.0.0
 */
public interface WechatBaseV3Pay {

	WxPayContext getPayContext();

	<T> Consequences<T> doPreOrder(String url, Object req, Class<T> type);

	String doSign(String signStr);

	<T> Consequences<T> doRequest(HttpUriRequest httpRequest, Class<T> type);

	@SneakyThrows
	HttpPost newHttpPost(String url, Object object);

	@SneakyThrows
	default HttpGet newHttpGet(String url) {
		return this.newHttpGet(url, null);
	}

	@SneakyThrows
	HttpGet newHttpGet(String url, @Nullable String param);

	static WxPayJson json() {
		return WxPayJson.getInstance();
	}

}
