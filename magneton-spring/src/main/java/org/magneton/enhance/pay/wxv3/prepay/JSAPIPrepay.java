package org.magneton.enhance.pay.wxv3.prepay;

import org.magneton.enhance.pay.wxv3.prepay.entity.WxPayJSAPIPrepay;
import org.magneton.enhance.pay.wxv3.prepay.entity.WxPayJSAPIPrepayReq;

/**
 * JSAPI支付
 *
 * https://pay.weixin.qq.com/wiki/doc/apiv3/open/pay/chapter1_1_1.shtml
 *
 * @author zhangmsh 2022/4/5
 * @since 1.0.0
 */
public interface JSAPIPrepay extends Prepay<WxPayJSAPIPrepayReq, WxPayJSAPIPrepay> {

}
