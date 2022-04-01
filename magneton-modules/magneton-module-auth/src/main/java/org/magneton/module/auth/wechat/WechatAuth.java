package org.magneton.module.auth.wechat;

import org.magneton.core.Consequences;

/**
 * 微信授权登录
 *
 * @apiNote 文档：{@code https://developers.weixin.qq.com/doc/oplatform/Mobile_App/WeChat_Login/Authorized_API_call_UnionID.html}
 * @author zhangmsh 2022/4/1
 * @since 1.0.0
 */
public interface WechatAuth {

	/**
	 * 获取用户信息
	 * @param accessToken 授权AccessToken
	 * @param openid 微他openid
	 * @return 微信用户信息
	 */
	Consequences<WechatUserInfo> getUserInfo(String accessToken, String openid);

}
