package org.magneton.module.oss.aliyun;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * .
 *
 * @author zhangmsh 25/03/2022
 * @since 2.0.7
 */
@Setter
@Getter
@ToString
public class AliyunOssProperty {

	/**
	 * AccessKey
	 */
	private String accessKey;

	/**
	 * AccessKey Secret
	 */
	private String accessKeySecret;

	/**
	 * STS接入地址，例如sts.cn-hangzhou.aliyuncs.com。
	 */
	private String endpoint;

	/**
	 * 角色ARN
	 */
	private String roleArn;

	/**
	 * 自定义角色会话名称，用来区分不同的令牌
	 */
	private String roleSessionName;

	/**
	 * regionId表示RAM的地域ID。以华东1（杭州）地域为例，regionID填写为cn-hangzhou。也可以保留默认值，默认值为空字符串（""）。
	 */
	private String regionId;

}
