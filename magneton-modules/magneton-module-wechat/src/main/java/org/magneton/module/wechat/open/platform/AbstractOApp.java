package org.magneton.module.wechat.open.platform;

import com.google.common.base.Preconditions;
import org.magneton.foundation.exception.BusinessException;
import org.magneton.module.wechat.open.WechatOpenContext;
import org.magneton.module.wechat.open.entity.AccessTokenRes;
import org.magneton.module.wechat.open.entity.UserInfoReq;
import org.magneton.module.wechat.open.entity.UserInfoRes;

import javax.annotation.Nullable;

/**
 * @author zhangmsh 2022/4/2
 * @since 1.0.0
 */
public class AbstractOApp implements OApp {

	private final WechatOpenContext wechatOpenContext;

	public AbstractOApp(WechatOpenContext wechatOpenContext) {
		this.wechatOpenContext = Preconditions.checkNotNull(wechatOpenContext);
	}

	@Override
	public Reply<AccessTokenRes> requestAccessTokenByCode(String code) {
		Preconditions.checkNotNull(code);
		return this.wechatOpenContext.getOAuth().accessToken(code);
	}

	@Nullable
	@Override
	public AccessTokenRes getAccessTokenByOpenid(String openid) {
		Preconditions.checkNotNull(openid);
		Reply<AccessTokenRes> reply = this.wechatOpenContext.getOAuth().accessToken(openid);
		if (!reply.isSuccess()) {
			throw new BusinessException(String.format("get accessToken exception: %s", reply.getMessage()));
		}
		return reply.getData();
	}

	@Override
	public Reply<UserInfoRes> requestUserInfo(UserInfoReq userInfoReq) {
		Preconditions.checkNotNull(userInfoReq);
		return this.wechatOpenContext.getOAuth().userInfo(userInfoReq);
	}

	public WechatOpenContext getWechatContext() {
		return this.wechatOpenContext;
	}

}
