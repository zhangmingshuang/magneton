package org.magneton.module.wechat;

import org.magneton.module.wechat.core.oauth2.OAuth;

/**
 * @author zhangmsh 2022/4/2
 * @since 1.0.0
 */
public interface WechatContext {

	WechatConfig getWechatConfig();

	OAuth getOAuth();

}
