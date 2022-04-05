package org.magneton.module.pay.wechat.v3.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * @author zhangmsh 2022/4/5
 * @since 1.0.0
 */
public interface WechatV3PayContext {

	ObjectMapper getObjectMapper();

	Verifier getVerifier();

	CloseableHttpClient getHttpClient();

	WechatPayConfig getPayConfig();

}
