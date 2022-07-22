package org.magneton.module.wechat.miniprogram.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zhangmsh 2022/6/16
 * @since 1.0.1
 */
@Setter
@Getter
@ToString
public class MPPhoneInfo {

	/**
	 * phoneNumber string 用户绑定的手机号（国外手机号会有区号）
	 *
	 */
	private String phoneNumber;

	/**
	 * purePhoneNumber string 没有区号的手机号
	 */
	private String purePhoneNumber;

	/**
	 * countryCode string 区号
	 */
	private String countryCode;

	/**
	 * 数据水印
	 */
	private Watermark watermark;

}
