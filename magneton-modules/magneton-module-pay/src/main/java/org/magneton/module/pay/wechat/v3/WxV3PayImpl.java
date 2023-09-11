package org.magneton.module.pay.wechat.v3;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.wechat.pay.contrib.apache.httpclient.constant.WechatPayHttpHeaders;
import com.wechat.pay.contrib.apache.httpclient.notification.Notification;
import com.wechat.pay.contrib.apache.httpclient.notification.NotificationHandler;
import com.wechat.pay.contrib.apache.httpclient.notification.NotificationRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpGet;
import org.magneton.core.Result;
import org.magneton.core.ResultException;
import org.magneton.foundation.exception.ProcessException;
import org.magneton.module.pay.wechat.v3.core.DefaultWxPayContext;
import org.magneton.module.pay.wechat.v3.core.WxPayConfig;
import org.magneton.module.pay.wechat.v3.core.WxPayContext;
import org.magneton.module.pay.wechat.v3.prepay.*;
import org.magneton.module.pay.wechat.v3.prepay.entity.WxPayNotification;
import org.magneton.module.pay.wechat.v3.prepay.entity.WxPayOrder;
import org.magneton.module.pay.wechat.v3.prepay.entity.WxPayOrderQuery;
import org.magneton.module.pay.wechat.v3.prepay.entity.WxPayOrderQuery.Type;
import org.magneton.module.pay.wechat.v3.profitsharing.ProfitSharing;
import org.magneton.module.pay.wechat.v3.profitsharing.ProfitSharingImpl;

import java.nio.charset.StandardCharsets;
import java.security.Security;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

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

	private WxPayContext payContext;

	private Map<Class, Object> classCache = Maps.newConcurrentMap();

	public WxV3PayImpl(WxPayConfig wxPayConfig) {
		this.payContext = new DefaultWxPayContext(wxPayConfig);
		this.wechatBaseV3Pay = new WechatBaseV3PayImpl(this.payContext);
	}

	@Override
	public AppPrepay appPrepay() {
		return (AppPrepay) this.classCache.computeIfAbsent(AppPrepay.class,
				clazz -> new AppPrepayImpl(this.wechatBaseV3Pay));
	}

	@Override
	public JSAPIPrepay jsapiPrepay() {
		return (JSAPIPrepay) this.classCache.computeIfAbsent(JSAPIPrepay.class,
				clazz -> new JSAPIPrepayImpl(this.wechatBaseV3Pay));
	}

	@Override
	public H5Prepay h5Prepay() {
		return (H5Prepay) this.classCache.computeIfAbsent(H5Prepay.class,
				clazz -> new H5PrepayImpl(this.wechatBaseV3Pay));
	}

	@Override
	public ProfitSharing profitSharing() {
		return (ProfitSharing) this.classCache.computeIfAbsent(ProfitSharing.class,
				clazz -> new ProfitSharingImpl(this.payContext));
	}

	@Override
	public Result<WxPayOrder> queryOrder(WxPayOrderQuery query) {
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
		Result<WxPayOrder> res = this.wechatBaseV3Pay.doRequest(httpGet, WxPayOrder.class);
		if (!res.isSuccess()) {
			return Result.failBy(res.getMessage());
		}
		return res;
	}

	// 支付通知文档：https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_2_5.shtml
	@Override
	@SuppressWarnings("OverlyBroadCatchBlock")
	public WxPayNotification parsePaySuccessData(Map<String, String> httpHeaders, String body) {
		Preconditions.checkNotNull(httpHeaders);
		Preconditions.checkNotNull(body);
		if (log.isDebugEnabled()) {
			log.debug("parsePaySuccessData, headers:{}, body:{}", httpHeaders, body);
		}
		for (Entry<String, String> entry : httpHeaders.entrySet()) {
			httpHeaders.put(entry.getKey().toLowerCase(Locale.ROOT), entry.getValue());
		}
		// 检查平台证书序列号 https://pay.weixin.qq.com/wiki/doc/apiv3/wechatpay/wechatpay4_1.shtml
		// 证签名前，请商户先检查序列号是否跟商户当前所持有的
		// 微信支付平台证书的序列号一致。如果不一致，请重新获取证书。否则，签名的私钥和证书不匹配，将无法成功验证签名。
		String reqWechatPaySerial = httpHeaders.get(WechatPayHttpHeaders.WECHAT_PAY_SERIAL.toLowerCase(Locale.ROOT));
		Preconditions.checkArgument(!Strings.isNullOrEmpty(reqWechatPaySerial), "request header %s miss",
				WechatPayHttpHeaders.WECHAT_PAY_SERIAL);
		// HTTP头Wechatpay-Timestamp 中的应答时间戳。
		String reqWechatPayTimestamp = httpHeaders
				.get(WechatPayHttpHeaders.WECHAT_PAY_TIMESTAMP.toLowerCase(Locale.ROOT));
		Preconditions.checkArgument(!Strings.isNullOrEmpty(reqWechatPayTimestamp), "request header %s miss",
				WechatPayHttpHeaders.WECHAT_PAY_TIMESTAMP);
		// HTTP头Wechatpay-Nonce 中的应答随机串。
		String reqWechatPayNonce = httpHeaders.get(WechatPayHttpHeaders.WECHAT_PAY_NONCE.toLowerCase(Locale.ROOT));
		Preconditions.checkArgument(!Strings.isNullOrEmpty(reqWechatPayNonce), "request header %s miss",
				WechatPayHttpHeaders.WECHAT_PAY_NONCE);
		// 应答签名
		String reqWechatPaySignature = httpHeaders
				.get(WechatPayHttpHeaders.WECHAT_PAY_SIGNATURE.toLowerCase(Locale.ROOT));
		Preconditions.checkArgument(!Strings.isNullOrEmpty(reqWechatPaySignature), "request header %s miss",
				WechatPayHttpHeaders.WECHAT_PAY_SIGNATURE);

		WxPayConfig payConfig = this.payContext.getPayConfig();
		// 证书序列号
		// String wechatPaySerial =
		// this.getPayContext().getVerifier().getValidCertificate().getSerialNumber().toString();
		// if (!wechatPaySerial.equals(reqWechatPaySerial)) {
		// throw new PaySerialException(String.format("wechat pay serial not match. should
		// be %s but %s",
		// wechatPaySerial, reqWechatPaySerial));
		// }
		NotificationRequest request = new NotificationRequest.Builder().withSerialNumber(reqWechatPaySerial)
				.withNonce(reqWechatPayNonce).withTimestamp(reqWechatPayTimestamp).withSignature(reqWechatPaySignature)
				.withBody(body).build();
		NotificationHandler handler = new NotificationHandler(this.payContext.getVerifier(),
				payConfig.getApiV3Key().getBytes(StandardCharsets.UTF_8));
		// 验签和解析请求体
		try {
			Notification notification = handler.parse(request);
			String decryptData = notification.getDecryptData();
			if (log.isDebugEnabled()) {
				log.debug("wxPay notification data:{}", decryptData);
			}
			return this.payContext.getObjectMapper().readValue(decryptData, WxPayNotification.class);
		}
		catch (Exception e) {
			throw new ResultException(Result.fail().message(e.getMessage()));
		}
	}

	public WxPayContext getPayContext() {
		return this.payContext;
	}

	public WechatBaseV3Pay getWechatBaseV3Pay() {
		return this.wechatBaseV3Pay;
	}

}
