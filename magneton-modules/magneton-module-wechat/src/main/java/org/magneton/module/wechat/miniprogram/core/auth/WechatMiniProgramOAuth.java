package org.magneton.module.wechat.miniprogram.core.auth;

import org.magneton.module.wechat.miniprogram.entity.MPAccessToken;

/**
 * @author zhangmsh 2022/6/16
 * @since 1.0.1
 */
public interface WechatMiniProgramOAuth {

	Reply<MPAccessToken> accessToken();

}
