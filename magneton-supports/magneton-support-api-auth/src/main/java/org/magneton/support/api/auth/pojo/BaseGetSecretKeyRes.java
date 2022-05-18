package org.magneton.support.api.auth.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author zhangmsh 2022/3/20
 * @since 1.0.0
 */
@Setter
@Getter
@ToString
@Accessors(chain = true)
public class BaseGetSecretKeyRes {

	/**
	 * 秘钥Id
	 */
	private String secretId;

	/**
	 * 秘钥
	 */
	private String secretKey;

}
