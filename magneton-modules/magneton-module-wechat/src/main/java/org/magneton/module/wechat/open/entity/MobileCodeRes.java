package org.magneton.module.wechat.open.entity;

import javax.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/***
 * @author zhangmsh 2022/4/2
 * @since 1.0.0
 */
@Setter
@Getter
@ToString
@Accessors(chain = true)
public class MobileCodeRes {

	/**
	 * 应用唯一标识，在微信开放平台提交应用审核通过后获得
	 */
	private String appid;

	/**
	 * 应用授权作用域
	 */
	private String scope;

	/**
	 * 第三方程序发送时用来标识其请求的唯一性的标志，由第三方程序调用 sendReq 时传入，由微信终端回传，state 字符串长度不能超过 1K
	 */
	@Nullable
	private String state;

}
