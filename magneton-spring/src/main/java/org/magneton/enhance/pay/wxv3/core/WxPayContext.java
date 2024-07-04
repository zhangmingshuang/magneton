package org.magneton.enhance.pay.wxv3.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * @author zhangmsh 2022/4/5
 * @since 1.0.0
 */
public interface WxPayContext {

	ObjectMapper getObjectMapper();

	Verifier getVerifier();

	PrivateKeySigner getPrivateKeySigner();

	WechatPay2Validator getWechatPay2Validator();

	CloseableHttpClient getHttpClient();

	WxPayConfig getPayConfig();

}
