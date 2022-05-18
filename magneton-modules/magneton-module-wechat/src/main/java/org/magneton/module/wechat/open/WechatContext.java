package org.magneton.module.wechat.open;

import org.magneton.module.wechat.open.core.oauth2.WechatOAuth;

/**
 * @author zhangmsh 2022/4/2
 * @since 1.0.0
 */
public interface WechatContext {

	WechatOpenConfig getWechatConfig();

	WechatOAuth getOAuth();

}
