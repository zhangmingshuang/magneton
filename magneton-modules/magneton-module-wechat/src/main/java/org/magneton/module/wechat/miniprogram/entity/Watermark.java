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
public class Watermark {

	/** appid string 小程序appid */
	private String appid;

	/** timestamp number 用户获取手机号操作的时间戳 */
	private long timestamp;

}
