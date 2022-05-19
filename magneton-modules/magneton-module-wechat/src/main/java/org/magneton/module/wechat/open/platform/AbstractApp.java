package org.magneton.module.wechat.open.platform;

import com.google.common.base.Preconditions;
import javax.annotation.Nullable;
import org.magneton.core.Consequences;
import org.magneton.module.wechat.open.WechatContext;
import org.magneton.module.wechat.open.entity.AccessTokenRes;
import org.magneton.module.wechat.open.entity.UserInfoReq;
import org.magneton.module.wechat.open.entity.UserInfoRes;

/**
 * @author zhangmsh 2022/4/2
 * @since 1.0.0
 */
public class AbstractApp implements App {

	private final WechatContext wechatContext;

	public AbstractApp(WechatContext wechatContext) {
		this.wechatContext = Preconditions.checkNotNull(wechatContext);
	}

	@Override
	public Consequences<AccessTokenRes> requestAccessTokenByCode(String code) {
		Preconditions.checkNotNull(code);
		return this.wechatContext.getOAuth().accessToken(code);
	}

	@Nullable
	@Override
	public AccessTokenRes getAccessTokenByOpenid(String openid) {
		Preconditions.checkNotNull(openid);
		return this.wechatContext.getOAuth().accessTokenFromCache(openid);
	}

	@Override
	public Consequences<UserInfoRes> requestUserInfo(UserInfoReq userInfoReq) {
		Preconditions.checkNotNull(userInfoReq);
		return this.wechatContext.getOAuth().userInfo(userInfoReq);
	}

	public WechatContext getWechatContext() {
		return this.wechatContext;
	}

}
