package org.magneton.module.wechat.open.platform;

import org.magneton.core.Result;
import org.magneton.module.wechat.open.entity.AccessTokenRes;
import org.magneton.module.wechat.open.entity.UserInfoReq;
import org.magneton.module.wechat.open.entity.UserInfoRes;

/**
 * @author zhangmsh 2022/4/2
 * @since 1.0.0
 */
public interface OApp {

	/**
	 * 通过Code获取AccessToken
	 *
	 * @apiNote https://developers.weixin.qq.com/doc/oplatform/Website_App/WeChat_Login/Authorized_Interface_Calling_UnionID.html
	 * @param code Code
	 * @return AccessToken
	 */
	Result<AccessTokenRes> requestAccessTokenByCode(String code);

	/**
	 * 通过Openid获取AccessToken
	 * @param openid 用户的Openid
	 * @return AccessToken. 如果缓存已经失效或者不存在，则返回{@code null}
	 */
	Result<AccessTokenRes> getAccessTokenByOpenid(String openid);

	/**
	 * 获取用户的信息
	 *
	 * @apiNote https://developers.weixin.qq.com/doc/oplatform/Website_App/WeChat_Login/Authorized_Interface_Calling_UnionID.html
	 * @param userInfoReq 用户信息请求
	 * @return 用户的信息
	 */
	Result<UserInfoRes> requestUserInfo(UserInfoReq userInfoReq);

}