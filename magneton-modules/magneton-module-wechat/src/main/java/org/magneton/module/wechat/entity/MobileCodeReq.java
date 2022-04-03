package org.magneton.module.wechat.entity;

import javax.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author zhangmsh 2022/4/3
 * @since 1.0.0
 */
@Setter
@Getter
@ToString
@Accessors(chain = true)
public class MobileCodeReq {

	/**
	 * 应用授权作用域，如获取用户个人信息则填写 snsapi_userinfo
	 */
	private String scope = "snsapi_userinfo";

	/**
	 * 用于保持请求和回调的状态，授权请求后原样带回给第三方。该参数可用于防止 csrf 攻击（跨站请求伪造攻击），
	 *
	 * 建议第三方带上该参数，可设置为简单的随机数加 session 进行校验。在state传递的过程中会将该参数作为url的一部分进行处理，因此建议对该参数进行url
	 * encode操作，防止其中含有影响url解析的特殊字符（如'#'、'&'等）导致该参数无法正确回传。
	 */
	@Nullable
	private String state;

}
