package org.magneton.spring.launcher;

import org.springframework.stereotype.Component;

/**
 * .
 *
 * @author zhangmsh 2022/2/7
 * @since 1.2.0
 */
@Component
public class BeanRunLauncher implements RunLauncher {

	public static int FLAG = 0;

	@Override
	public void launch() {
		FLAG = 1;
	}

}
