package org.magneton.enhance.wechat.mp;

import org.magneton.core.Result;
import org.magneton.enhance.wechat.mp.core.asset.AssetManagement;
import org.magneton.enhance.wechat.mp.core.menu.MenuManagement;
import org.magneton.enhance.wechat.mp.core.message.pojo.MpMsgBody;

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

	/**
	 * 素材管理.
	 * @param appid appid，用来区分不同的公众号
	 * @return 素材管理
	 */
	AssetManagement assetManagement(String appid);

	/**
	 * 菜单管理.
	 * @param appid appid, 用来区分不同的公众号
	 * @return 菜单管理
	 */
	MenuManagement menuManagement(String appid);

}