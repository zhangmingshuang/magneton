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
import org.magneton.module.pay.wechat.v3.core.DefaultWxPayContext;
import org.magneton.module.pay.wechat.v3.core.WxPayConfig;
import org.magneton.module.pay.wechat.v3.core.WxPayContext;
import org.magneton.module.pay.wechat.v3.entity.WxPayNotification;
import org.magneton.module.pay.wechat.v3.entity.WxPayOrder;
import org.magneton.module.pay.wechat.v3.entity.WxPayOrderQuery;
import org.magneton.module.pay.wechat.v3.entity.WxPayOrderQuery.Type;
import org.magneton.module.pay.wechat.v3.prepay.AppPrepay;
import org.magneton.module.pay.wechat.v3.prepay.AppPrepayImpl;
import org.magneton.module.pay.wechat.v3.prepay.H5Prepay;
import org.magneton.module.pay.wechat.v3.prepay.H5PrepayImpl;
import org.magneton.module.pay.wechat.v3.prepay.JSAPIPrepay;
import org.magneton.module.pay.wechat.v3.prepay.JSAPIPrepayImpl;
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
public class WxV3PayImpl implements WxV3Pay {

	static {
		// 使用Java加载密钥时，抛出异常InvalidKeyException: Illegal key size
		// https://wechatpay-api.gitbook.io/wechatpay-api-v3/chang-jian-wen-ti/api-v3-mi-yao-xiang-guan#shi-yong-java-jia-zai-mi-yao-shi-pao-chu-yi-chang-invalidkeyexception-illegal-key-size
		Security.setProperty("crypto.policy", "unlimited");
	}

	private WechatBaseV3Pay wechatBaseV3Pay;

	private AppPrepay appPrepay;

	private JSAPIPrepay jsapiPrepay;

	private H5Prepay h5Prepay;

	private WxPayContext payContext;

	public WxV3PayImpl(WxPayConfig wxPayConfig) {
		this.payContext = new DefaultWxPayContext(wxPayConfig);
		this.wechatBaseV3Pay = new WechatBaseV3PayImpl(this.payContext);
	}

	@Override
	public AppPrepay appPrepay() {
		if (this.appPrepay == null) {
			synchronized (this) {
				this.appPrepay = new AppPrepayImpl(this.wechatBaseV3Pay);
			}
		}
		return this.appPrepay;
	}

	@Override
	public JSAPIPrepay jsapiPrepay() {
		if (this.jsapiPrepay == null) {
			synchronized (this) {
				this.jsapiPrepay = new JSAPIPrepayImpl(this.wechatBaseV3Pay);
			}
		}
		return this.jsapiPrepay;
	}

	@Override
	public H5Prepay h5Prepay() {
		if (this.h5Prepay == null) {
			synchronized (this) {
				this.h5Prepay = new H5PrepayImpl(this.wechatBaseV3Pay);
			}
		}
		return this.h5Prepay;
	}

	@Override
	public Consequences<WxPayOrder> queryOrder(WxPayOrderQuery query) {
		Type reqIdType = query.getReqIdType();
		String url;
		switch (reqIdType) {
		case OUT_TRADE_NO:
			url = Strings.lenientFormat(
					"https://api.mch.weixin.qq.com/v3/pay/partner/transactions/out-trade-no/%s?mchid=%s",
					Preconditions.checkNotNull(query.getReqId()), this.payContext.getPayConfig().getMerchantId());
			break;
		case TRANSACTION_ID:
			url = Strings.lenientFormat("https://api.mch.weixin.qq.com/v3/pay/transactions/id/%s?mchid=%s",
					Preconditions.checkNotNull(query.getReqId()), this.payContext.getPayConfig().getMerchantId());
			break;
		default:
			throw new ProcessException("unknown reqIdType %s", reqIdType);
		}
		HttpGet httpGet = this.wechatBaseV3Pay.newHttpGet(url);
		Consequences<WxPayOrder> res = this.wechatBaseV3Pay.doRequest(httpGet, WxPayOrder.class);
		if (!res.isSuccess()) {
			return Consequences.failMessageOnly(res.getMessage());
		}
		return res;
	}

	// 支付通知文档：https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_2_5.shtml
	@Override
	@SuppressWarnings("OverlyBroadCatchBlock")
	public WxPayNotification parsePaySuccessData(Map<String, String> httpHeaders, String body) {
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

		WxPayConfig payConfig = this.payContext.getPayConfig();
		// 证书序列号
		String wechatPaySerial = payConfig.getMerchantSerialNumber();
		if (!wechatPaySerial.equals(reqWechatPaySerial)) {
			throw new PaySerialException("wechat pay serial not match");
		}
		NotificationRequest request = new NotificationRequest.Builder().withSerialNumber(wechatPaySerial)
				.withNonce(reqWechatPayNonce).withTimestamp(reqWechatPayTimestamp).withSignature(reqWechatPaySignature)
				.withBody(body).build();
		NotificationHandler handler = new NotificationHandler(this.payContext.getVerifier(),
				payConfig.getApiV3Key().getBytes(StandardCharsets.UTF_8));
		// 验签和解析请求体
		try {
			Notification notification = handler.parse(request);
			String decryptData = notification.getDecryptData();
			return this.payContext.getObjectMapper().readValue(decryptData, WxPayNotification.class);
		}
		catch (Exception e) {
			throw new ResponseException(Response.bad().message(e.getMessage()));
		}
	}

}
