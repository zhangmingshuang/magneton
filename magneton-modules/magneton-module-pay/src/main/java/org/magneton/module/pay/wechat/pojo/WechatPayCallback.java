package org.magneton.module.pay.wechat.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author zhangmsh 2022/4/4
 * @since 1.0.0
 */
@Setter
@Getter
@ToString
@Accessors(chain = true)
public class WechatPayCallback {

	/**
	 * 错误码，SUCCESS为清算机构接收成功，其他错误码为失败。 示例值：FAIL
	 */
	private String code;

	/**
	 * 返回信息，如非空，为错误原因。 示例值：失败
	 */
	private String message;

}
