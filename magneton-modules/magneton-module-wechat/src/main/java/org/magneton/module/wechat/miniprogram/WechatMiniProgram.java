package org.magneton.module.wechat.miniprogram;

import org.magneton.core.Consequences;
import org.magneton.module.wechat.miniprogram.entity.Code2Session;
import org.magneton.module.wechat.miniprogram.entity.PhoneInfo;

/**
 * 微信小程序
 *
 * https://developers.weixin.qq.com/miniprogram/dev/api-backend/
 *
 * @author zhangmsh 2022/5/1
 * @since 1.0.0
 */
public interface WechatMiniProgram {

	/**
	 * 登录凭证校验
	 * @param code 小程序通过 wx.login 接口获得临时登录凭证 code 后传到开发者服务器调用此接口完成登录流程
	 * @return 登录凭证
	 */
	Consequences<Code2Session> code2Session(String code);

	/**
	 * 获取手机号
	 *
	 * code换取用户手机号。 每个 code 只能使用一次，code的有效期为5min
	 *
	 * @apiNote https://developers.weixin.qq.com/miniprogram/dev/framework/open-ability/getPhoneNumber.html
	 * @param code 动态令牌。可通过动态令牌换取用户手机号。使用方法详情
	 * https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/phonenumber/phonenumber.getPhoneNumber.html
	 * @return 手机号
	 */
	Consequences<PhoneInfo> getPhoneNumber(String code);

}
