package org.magneton.module.wechat.entity;

import java.util.List;
import javax.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 微信用户信息.
 *
 * @apiNote 开发者最好保存 unionID 信息，以便以后在不同应用之间进行用户信息互通。
 * 详见：{@code https://developers.weixin.qq.com/doc/oplatform/Mobile_App/WeChat_Login/Authorized_API_call_UnionID.html}
 * @author zhangmsh 2022/4/1
 * @since 1.0.0
 */
@Setter
@Getter
@ToString
public class UserInfoRes {

	/**
	 * 用户统一标识。针对一个微信开放平台帐号下的应用，同一用户的 unionid 是唯一的。
	 */
	private String unionid;

	/**
	 * 普通用户的标识，对当前开发者帐号唯一
	 */
	private String openid;

	/**
	 * 普通用户昵称
	 */
	private String nickname;

	/**
	 * 普通用户性别，1 为男性，2 为女性
	 */
	private int sex;

	/**
	 * 国家，如中国为 CN
	 */
	private String country;

	/**
	 * 普通用户个人资料填写的省份
	 */
	private String province;

	/**
	 * 普通用户个人资料填写的城市
	 */
	private String city;

	/**
	 * 用户头像，最后一个数值代表正方形头像大小（有 0、46、64、96、132 数值可选，0 代表 640*640 正方形头像），用户没有头像时该项为空
	 */
	@Nullable
	private String headimgurl;

	/**
	 * 用户特权信息，如微信沃卡用户为（chinaunicom）
	 */
	private List<String> privilege;

}
