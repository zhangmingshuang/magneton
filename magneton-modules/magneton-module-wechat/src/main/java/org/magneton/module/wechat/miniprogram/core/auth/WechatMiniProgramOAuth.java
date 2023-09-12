package org.magneton.module.wechat.miniprogram.core.auth;

import org.magneton.core.Result;
import org.magneton.module.wechat.miniprogram.entity.MPAccessToken;

/**
 * @author zhangmsh 2022/6/16
 * @since 1.0.1
 */
public interface WechatMiniProgramOAuth {

	Result<MPAccessToken> accessToken();

}
