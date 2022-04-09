package org.magneton.module.safedog.geetest;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * .
 *
 * @author zhangmsh 09/04/2022
 * @since 2.0.8
 */
@Setter
@Getter
@ToString
public class GeeTestTodoConfig {

	private String captchaId;

	private String captchaKey;

	public void validate() {
		if (this.captchaId == null || this.captchaKey == null) {
			throw new IllegalArgumentException("captchaId or captchaKey is null");
		}
	}

}
