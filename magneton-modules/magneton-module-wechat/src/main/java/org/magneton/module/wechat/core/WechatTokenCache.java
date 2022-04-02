package org.magneton.module.wechat.core;

/**
 * @author zhangmsh 2022/4/1
 * @since 1.0.0
 */
public interface WechatTokenCache {

	WechatAccessToken get(String code);

	void set(String code, WechatAccessToken wechatAccessToken);

}
