package org.magneton.enhance.sms.redis;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * .
 *
 * @author zhangmsh 18/03/2022
 * @since 2.0.7
 */
@Setter
@Getter
@ToString
@Accessors(chain = true)
public class CacheToken {

	private String mobile;

	private String code;

	private long time;

}
