package org.magneton.module.wechat.mp;

import org.magneton.core.Result;
import org.magneton.module.wechat.mp.core.pojo.MpMsgBody;

/**
 * 微信公众号模块.
 *
 * @author zhangmsh.
 * @since 2024
 */
public interface WechatMpTemplate {

	/**
	 * 授权.
	 * @param appid appid，用来区分不同的公众号
	 * @param message 消息
	 * @return 返回消息
	 */
	Result<String> auth(String appid, MpMsgBody message);

	/**
	 * 分发消息.
	 * @param appid appid，用来区分不同的公众号
	 * @param message 消息
	 * @return 返回消息
	 */
	Result<String> dispatch(String appid, MpMsgBody message);

}