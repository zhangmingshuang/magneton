package org.magneton.module.wechat.core.oauth2;

import javax.annotation.Nullable;

import org.magneton.core.Consequences;
import org.magneton.module.wechat.entity.AccessTokenRes;
import org.magneton.module.wechat.entity.UserInfoReq;
import org.magneton.module.wechat.entity.UserInfoRes;

/**
 * 微信授权登录
 *
 * @apiNote 文档：{@code https://developers.weixin.qq.com/doc/oplatform/Mobile_App/WeChat_Login/Authorized_API_call_UnionID.html}
 * @author zhangmsh 2022/4/1
 * @since 1.0.0
 */
public interface WechatOAuth {

	/**
	 * 获取用户信息
	 * @param userInfoReq 请求数据
	 * @return 微信用户信息
	 */
	Consequences<UserInfoRes> userInfo(UserInfoReq userInfoReq);

	/**
	 * 使用用户授权的Code获取用户信息
	 * @param code 用户授权的Code
	 * @return 微信用户信息
	 */
	Consequences<AccessTokenRes> accessToken(String code);

	@Nullable
	AccessTokenRes accessTokenFromCache(String openid);

}
