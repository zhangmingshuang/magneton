package org.magneton.spring.launcher;

import com.google.auto.service.AutoService;

/**
 * .
 *
 * @author zhangmsh 2022/2/7
 * @since 1.2.0
 */
@AutoService(RunLauncher.class)
public class SPIRunLauncher implements RunLauncher {

	public static int FLAG = 0;

	@Override
	public void launch() {
		FLAG = 1;
	}

}
