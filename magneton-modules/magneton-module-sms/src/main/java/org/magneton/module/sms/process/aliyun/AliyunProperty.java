package org.magneton.module.sms.process.aliyun;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * .
 *
 * @author zhangmsh 16/03/2022
 * @since 2.0.7
 */
@Setter
@Getter
@ToString
public class AliyunProperty {

	private String accessKeyId;

	private String accessKeySecret;

	private String endpoint = "dysmsapi.aliyuncs.com";

}
