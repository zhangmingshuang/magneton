package org.magneton.module.pay.wechat.v3.core;

import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * @author zhangmsh 2022/7/15
 * @since 1.0.1
 */
@Getter
@AllArgsConstructor
public class WxPaySession {

	private PrivateKeySigner privateKeySigner;

	private Verifier verifier;

	private WechatPay2Validator wechatPay2Validator;

	private CloseableHttpClient httpClient;

}
