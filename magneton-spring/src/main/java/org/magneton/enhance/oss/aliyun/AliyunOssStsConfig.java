package org.magneton.enhance.oss.aliyun;

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
public class AliyunOssStsConfig {

	/**
	 * STS临时AccessKey
	 */
	private String accessKeyId;

	/**
	 * STS临时AccessKeySecret
	 */
	private String accessKeySecret;

	/**
	 * 临时安全令牌SecurityToken。
	 */
	private String securityToken;

	/**
	 * 授權Bucket
	 */
	private String bucket;

	/**
	 * endpoint
	 */
	private String endpoint;

	/**
	 * 授权过期时间
	 */
	private long expireTime;

}
