package org.magneton.module.wechat.open.core.oauth2;

import org.magneton.core.Reply;
import org.magneton.module.wechat.open.entity.AccessTokenRes;
import org.magneton.module.wechat.open.entity.UserInfoReq;
import org.magneton.module.wechat.open.entity.UserInfoRes;

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
	Reply<UserInfoRes> userInfo(UserInfoReq userInfoReq);

	/**
	 * 使用用户授权的Code获取用户信息
	 * @param code 用户授权的Code
	 * @return 微信用户信息
	 */
	Reply<AccessTokenRes> accessToken(String code);

}
