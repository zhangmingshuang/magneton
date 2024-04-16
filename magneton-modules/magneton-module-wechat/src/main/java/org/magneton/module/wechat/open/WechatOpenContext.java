package org.magneton.module.wechat.open;

import org.magneton.module.wechat.open.core.oauth2.WechatOAuth;

/**
 * 微信开放平台上下文
 *
 * @author zhangmsh 2022/4/2
 * @since 1.0.0
 */
public interface WechatOpenContext {

	/**
	 * 获取微信开放平台配置
	 * @return 微信开放平台配置
	 */
	WechatOpenConfig getWechatConfig();

	/**
	 * 获取微信开放平台OAuth
	 * @return 微信开放平台OAuth
	 */
	WechatOAuth getOAuth();

}