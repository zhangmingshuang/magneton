package org.magneton.module.pay.wechat.v3;

import com.wechat.pay.contrib.apache.httpclient.constant.WechatPayHttpHeaders;
import com.wechat.pay.contrib.apache.httpclient.notification.Notification;
import com.wechat.pay.contrib.apache.httpclient.notification.NotificationHandler;
import com.wechat.pay.contrib.apache.httpclient.notification.NotificationRequest;
import java.nio.charset.StandardCharsets;
import java.security.Security;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpGet;
import org.magneton.core.Consequences;
import org.magneton.core.Response;
import org.magneton.core.ResponseException;
import org.magneton.core.base.Preconditions;
import org.magneton.core.base.Strings;
import org.magneton.foundation.exception.ProcessException;
import org.magneton.module.pay.exception.PaySerialException;
import org.magneton.module.pay.wechat.v3.core.DefaultWechatV3PayContext;
import org.magneton.module.pay.wechat.v3.core.WechatPayConfig;
import org.magneton.module.pay.wechat.v3.core.WechatV3PayContext;
import org.magneton.module.pay.wechat.v3.entity.WechatV3PayNotification;
import org.magneton.module.pay.wechat.v3.entity.WechatV3PayOrder;
import org.magneton.module.pay.wechat.v3.entity.WechatV3PayOrderReq;
import org.magneton.module.pay.wechat.v3.entity.WechatV3PayOrderReq.Type;
import org.magneton.module.pay.wechat.v3.prepay.WechatAppV3Prepay;
import org.magneton.module.pay.wechat.v3.prepay.WechatAppV3PrepayImpl;
import org.magneton.module.pay.wechat.v3.prepay.WechatBaseV3Pay;
import org.magneton.module.pay.wechat.v3.prepay.WechatBaseV3PayImpl;

/**
 * 微信支付.
 *
 * 文档地址：{@code https://pay.weixin.qq.com/wiki/doc/apiv3_partner/open/pay/chapter2_5_2.shtml}
 *
 * @author zhangmsh 28/03/2022
 * @since 2.0.7
 */
@Slf4j
public class WechatV3PayImpl implements WechatV3Pay {

	static {
		// 使用Java加载密钥时，抛出异常InvalidKeyException: Illegal key size
		// https://wechatpay-api.gitbook.io/wechatpay-api-v3/chang-jian-wen-ti/api-v3-mi-yao-xiang-guan#shi-yong-java-jia-zai-mi-yao-shi-pao-chu-yi-chang-invalidkeyexception-illegal-key-size
		Security.setProperty("crypto.policy", "unlimited");
	}

	private WechatBaseV3Pay wechatBaseV3Pay;

	private WechatAppV3Prepay wechatAppV3Pay;

	private WechatV3PayContext wechatV3PayContext;

	public WechatV3PayImpl(WechatPayConfig wechatPayConfig) {
		this.wechatV3PayContext = new DefaultWechatV3PayContext(wechatPayConfig);
		this.wechatBaseV3Pay = new WechatBaseV3PayImpl(this.wechatV3PayContext);
	}

	@Override
	public WechatAppV3Prepay appPrepay() {
		// noinspection DoubleCheckedLocking
		if (this.wechatAppV3Pay == null) {
			// noinspection SynchronizeOnThis
			synchronized (WechatAppV3Prepay.class) {
				if (this.wechatAppV3Pay == null) {
					// noinspection UnnecessaryLocalVariable
					WechatAppV3Prepay pay = new WechatAppV3PrepayImpl(this.wechatBaseV3Pay);
					this.wechatAppV3Pay = pay;
				}
			}
		}
		return this.wechatAppV3Pay;
	}

	@Override
	public Consequences<WechatV3PayOrder> queryOrder(WechatV3PayOrderReq req) {
		Type reqIdType = req.getReqIdType();
		String url;
		switch (reqIdType) {
		case OUT_TRADE_NO:
			url = Strings.lenientFormat(
					"https://api.mch.weixin.qq.com/v3/pay/partner/transactions/out-trade-no/%s?mchid=%s",
					Preconditions.checkNotNull(req.getReqId()), this.wechatV3PayContext.getPayConfig().getMerchantId());
			break;
		case TRANSACTION_ID:
			url = Strings.lenientFormat("https://api.mch.weixin.qq.com/v3/pay/transactions/id/%s?mchid=%s",
					Preconditions.checkNotNull(req.getReqId()), this.wechatV3PayContext.getPayConfig().getMerchantId());
			break;
		default:
			throw new ProcessException("unknown reqIdType %s", reqIdType);
		}
		HttpGet httpGet = this.wechatBaseV3Pay.newHttpGet(url);
		Consequences<WechatV3PayOrder> res = this.wechatBaseV3Pay.doRequest(httpGet, WechatV3PayOrder.class);
		if (!res.isSuccess()) {
			return Consequences.failMessageOnly(res.getMessage());
		}
		return res;
	}

	// 支付通知文档：https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_2_5.shtml
	@Override
	@SuppressWarnings("OverlyBroadCatchBlock")
	public WechatV3PayNotification parsePaySuccessData(Map<String, String> httpHeaders, String body) {
		Preconditions.checkNotNull(httpHeaders);
		Preconditions.checkNotNull(body);
		// 检查平台证书序列号 https://pay.weixin.qq.com/wiki/doc/apiv3/wechatpay/wechatpay4_1.shtml
		// 证签名前，请商户先检查序列号是否跟商户当前所持有的
		// 微信支付平台证书的序列号一致。如果不一致，请重新获取证书。否则，签名的私钥和证书不匹配，将无法成功验证签名。
		String reqWechatPaySerial = httpHeaders.get(WechatPayHttpHeaders.WECHAT_PAY_SERIAL);
		Preconditions.checkArgument(Strings.isNullOrEmpty(reqWechatPaySerial), "request header %s miss",
				WechatPayHttpHeaders.WECHAT_PAY_SERIAL);
		// HTTP头Wechatpay-Timestamp 中的应答时间戳。
		String reqWechatPayTimestamp = httpHeaders.get(WechatPayHttpHeaders.WECHAT_PAY_TIMESTAMP);
		Preconditions.checkArgument(Strings.isNullOrEmpty(reqWechatPayTimestamp), "request header %s miss",
				WechatPayHttpHeaders.WECHAT_PAY_TIMESTAMP);
		// HTTP头Wechatpay-Nonce 中的应答随机串。
		String reqWechatPayNonce = httpHeaders.get(WechatPayHttpHeaders.WECHAT_PAY_NONCE);
		Preconditions.checkArgument(Strings.isNullOrEmpty(reqWechatPayNonce), "request header %s miss",
				WechatPayHttpHeaders.WECHAT_PAY_NONCE);
		// 应答签名
		String reqWechatPaySignature = httpHeaders.get(WechatPayHttpHeaders.WECHAT_PAY_SIGNATURE);
		Preconditions.checkArgument(Strings.isNullOrEmpty(reqWechatPaySignature), "request header %s miss",
				WechatPayHttpHeaders.WECHAT_PAY_SIGNATURE);

		WechatPayConfig payConfig = this.wechatV3PayContext.getPayConfig();
		// 证书序列号
		String wechatPaySerial = payConfig.getMerchantSerialNumber();
		if (!wechatPaySerial.equals(reqWechatPaySerial)) {
			throw new PaySerialException("wechat pay serial not match");
		}
		NotificationRequest request = new NotificationRequest.Builder().withSerialNumber(wechatPaySerial)
				.withNonce(reqWechatPayNonce).withTimestamp(reqWechatPayTimestamp).withSignature(reqWechatPaySignature)
				.withBody(body).build();
		NotificationHandler handler = new NotificationHandler(this.wechatV3PayContext.getVerifier(),
				payConfig.getApiV3Key().getBytes(StandardCharsets.UTF_8));
		// 验签和解析请求体
		try {
			Notification notification = handler.parse(request);
			String decryptData = notification.getDecryptData();
			return this.wechatV3PayContext.getObjectMapper().readValue(decryptData, WechatV3PayNotification.class);
		}
		catch (Exception e) {
			throw new ResponseException(Response.bad().message(e.getMessage()));
		}
	}

}
