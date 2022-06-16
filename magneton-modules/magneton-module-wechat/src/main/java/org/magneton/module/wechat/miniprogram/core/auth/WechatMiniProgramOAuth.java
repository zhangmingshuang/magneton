package org.magneton.module.wechat.miniprogram.core.auth;

import javax.annotation.Nullable;
import org.magneton.core.Consequences;
import org.magneton.module.wechat.miniprogram.entity.MPAccessTokenRes;

/**
 * @author zhangmsh 2022/6/16
 * @since 1.0.1
 */
public interface WechatMiniProgramOAuth {

	Consequences<MPAccessTokenRes> accessToken();

	@Nullable
	MPAccessTokenRes accessTokenFromCache();

}
