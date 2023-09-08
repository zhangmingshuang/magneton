package org.magneton.module.pay.wechat.v3.prepay;

import org.magneton.module.pay.wechat.v3.core.BaseV3Api;

/**
 *
 * 签名工具：https://pay.weixin.qq.com/wiki/doc/apiv3/wechatpay/wechatpay6_0.shtml
 *
 * @author zhangmsh 2022/4/5
 * @since 1.0.0
 */
public interface WechatBaseV3Pay extends BaseV3Api {

	<T> Reply<T> doPreOrder(String url, Object req, Class<T> type);

}
