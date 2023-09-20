package org.magneton.module.safedog.geetest;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 极验行为配置.
 *
 * @author zhangmsh 09/04/2022
 * @since 2.0.8
 * @see GeeTestBehaviorValidator
 */
@Setter
@Getter
@ToString
@Accessors(chain = true)
public class GeeTestBehaviorConfig {

	/**
	 * the captcha id.
	 */
	private String captchaId;

	/**
	 * the captcha key.
	 */
	private String captchaKey;

	/**
	 * validate params is expect.
	 */
	public void validate() {
		if (this.captchaId == null || this.captchaKey == null) {
			throw new IllegalArgumentException("captchaId or captchaKey is null");
		}
	}

}
