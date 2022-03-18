package org.magneton.support.api.auth.pojo;

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
public class SmsLoginRes {

	private String token;

	private String autoLoginToken;

}
