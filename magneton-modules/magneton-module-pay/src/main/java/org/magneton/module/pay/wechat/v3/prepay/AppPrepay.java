package org.magneton.module.pay.wechat.v3.prepay;

import org.magneton.module.pay.wechat.v3.prepay.entity.WxPayAppPrepay;
import org.magneton.module.pay.wechat.v3.prepay.entity.WxPayAppPrepayReq;

/**
 * APP支付
 *
 * https://pay.weixin.qq.com/wiki/doc/apiv3/open/pay/chapter2_5_0.shtml
 *
 * @author zhangmsh 2022/4/5
 * @since 1.0.0
 */
public interface AppPrepay extends Prepay<WxPayAppPrepayReq, WxPayAppPrepay> {

}
