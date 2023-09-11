package org.magneton.module.wechat.open.platform;

import com.google.common.base.Preconditions;
import org.magneton.core.Result;
import org.magneton.foundation.exception.BusinessException;
import org.magneton.module.wechat.open.WechatOpenContext;
import org.magneton.module.wechat.open.entity.AccessTokenRes;
import org.magneton.module.wechat.open.entity.UserInfoReq;
import org.magneton.module.wechat.open.entity.UserInfoRes;

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
	public Result<AccessTokenRes> requestAccessTokenByCode(String code) {
		Preconditions.checkNotNull(code, "code is null");
		return this.wechatOpenContext.getOAuth().accessToken(code);
	}

	@Override
	public Result<AccessTokenRes> getAccessTokenByOpenid(String openid) {
		Preconditions.checkNotNull(openid);
		Result<AccessTokenRes> result = this.wechatOpenContext.getOAuth().accessToken(openid);
		if (!result.isSuccess()) {
			throw new BusinessException(String.format("get accessToken exception: %s", result.getMessage()));
		}
		return result;
	}

	@Override
	public Result<UserInfoRes> requestUserInfo(UserInfoReq userInfoReq) {
		Preconditions.checkNotNull(userInfoReq);
		return this.wechatOpenContext.getOAuth().userInfo(userInfoReq);
	}

	public WechatOpenContext getWechatContext() {
		return this.wechatOpenContext;
	}

}
