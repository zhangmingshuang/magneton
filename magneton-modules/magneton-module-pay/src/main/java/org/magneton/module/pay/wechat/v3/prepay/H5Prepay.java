package org.magneton.module.pay.wechat.v3.prepay;

import org.magneton.module.pay.wechat.v3.prepay.entity.WxPayH5Prepay;
import org.magneton.module.pay.wechat.v3.prepay.entity.WxPayH5PrepayReq;

/**
 * H5支付
 *
 * https://pay.weixin.qq.com/wiki/doc/apiv3/open/pay/chapter2_6_0.shtml
 *
 * @author zhangmsh 2022/4/5
 * @since 1.0.0
 */
public interface H5Prepay extends Prepay<WxPayH5PrepayReq, WxPayH5Prepay> {

}
