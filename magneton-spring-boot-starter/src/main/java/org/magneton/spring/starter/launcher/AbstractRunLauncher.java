package org.magneton.spring.starter.launcher;

import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.List;
import org.magneton.foundation.collection.MoreCollections;
import org.magneton.foundation.spi.SPILoader;

/**
 * .
 *
 * @author zhangmsh 2022/2/7
 * @since 1.2.0
 */
public abstract class AbstractRunLauncher {

	/**
	 * 通过SPI加载的启动执行器
	 */
	private static final List<RunLauncher> SPI_RUN_LAUNCHERS;

	private static final List<RunLauncher> SUBSEQUENT_RUN_LAUNCHERS = Lists.newCopyOnWriteArrayList();

	static {
		SPI_RUN_LAUNCHERS = SPILoader.loadServices(RunLauncher.class,
				RunLauncherContextInitializer.class.getClassLoader());
	}

	protected AbstractRunLauncher() {
		this.initServiceLaunchers(SPI_RUN_LAUNCHERS);
	}

	protected void initSubsequentRunLaunchers(Collection<RunLauncher> runLaunchers) {
		if (MoreCollections.isNullOrEmpty(runLaunchers)) {
			return;
		}
		SUBSEQUENT_RUN_LAUNCHERS.addAll(runLaunchers);
		this.initServiceLaunchers(runLaunchers);
	}

	private void initServiceLaunchers(Collection<RunLauncher> runLaunchers) {
		if (MoreCollections.isNullOrEmpty(runLaunchers)) {
			return;
		}
		for (RunLauncher runLauncher : runLaunchers) {
			this.runLaunch(runLauncher);
		}
	}

	protected void runLaunch(RunLauncher runLauncher) {
		runLauncher.launch();
	}

}
