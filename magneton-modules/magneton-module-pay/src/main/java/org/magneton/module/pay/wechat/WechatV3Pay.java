package org.magneton.module.pay.wechat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.PrivateKey;

import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.AutoUpdateCertificatesVerifier;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.magneton.core.base.Preconditions;
import org.magneton.module.pay.Pay;
import org.magneton.module.pay.PreOrderReq;
import org.magneton.module.pay.PreOrderRes;
import org.magneton.module.pay.wechat.entity.WechatPreOrderReq;

/**
 * .
 *
 * @author zhangmsh 28/03/2022
 * @since 2.0.7
 */
public class WechatV3Pay implements Pay<WechatV3Pay> {

	private final WechatPayConfig wechatPayConfig;

	private CloseableHttpClient httpClient;

	public WechatV3Pay(WechatPayConfig wechatPayConfig) {
		this.wechatPayConfig = wechatPayConfig;
		this.init();
	}

	protected void init() {
		// 加载商户私钥（privateKey：私钥字符串）
		PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(new ByteArrayInputStream(privateKey.getBytes("utf-8")));

		// 加载平台证书（mchId：商户号,mchSerialNo：商户证书序列号,apiV3Key：V3密钥）
		AutoUpdateCertificatesVerifier verifier = new AutoUpdateCertificatesVerifier(
				new WechatPay2Credentials(mchId, new PrivateKeySigner(mchSerialNo, merchantPrivateKey)),
				apiV3Key.getBytes("utf-8"));

		// 初始化httpClient
		this.httpClient = WechatPayHttpClientBuilder.create().withMerchant(mchId, mchSerialNo, merchantPrivateKey)
				.withValidator(new WechatPay2Validator(verifier)).build();
	}

	@Override
	public WechatV3Pay actualPay() {
		return this;
	}

	public PreOrderRes preOrder(WechatPreOrderReq wechatPreOrderReq) {
		Preconditions.checkNotNull(wechatPreOrderReq);
		HttpPost httpPost = new HttpPost(WechatV3Url.PRE_ORDER);
		StringEntity entity = new StringEntity(reqdata, "utf-8");
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		httpPost.setHeader("Accept", "application/json");

		// 完成签名并执行请求
		// 预支付交易会话标识 prepay_id string[1,64] 预支付交易会话标识。用于后续接口调用中使用，该值有效期为2小时
		// 示例值：wx201410272009395522657a690389285100
		CloseableHttpResponse response = this.httpClient.execute(httpPost);

		try {
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 200) { // 处理成功
				System.out.println("success,return body = " + EntityUtils.toString(response.getEntity()));
			}
			else if (statusCode == 204) { // 处理成功，无返回Body
				System.out.println("success");
			}
			else {
				System.out.println("failed,resp code = " + statusCode + ",return body = "
						+ EntityUtils.toString(response.getEntity()));
				throw new IOException("request failed");
			}
		}
		finally {
			response.close();
		}

		return null;
	}

	@Override
	public PreOrderRes preOrder(PreOrderReq req) {
		Preconditions.checkNotNull(req);
		WechatPreOrderReq wechatPreOrderReq = new WechatPreOrderReq().setMchid(this.wechatPayConfig.getMchId())
				.setAppid(this.wechatPayConfig.getAppId()).setNotify_url(this.wechatPayConfig.getNotifyUrl())
				.setOut_trade_no(req.getOutTradeNo()).setDescription(req.getDescription());

		wechatPreOrderReq.setAmount(new WechatPreOrderReq.Amount().setTotal(req.getAmount()));
		return this.preOrder(wechatPreOrderReq);
	}

}
