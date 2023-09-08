package org.magneton.module.kit.access;

/**
 * Accessor factory builder.
 *
 * @author zhangmsh.
 * @since 2023.1
 */
public class AccessorFactoryBuilder {

	public static AccessorFactoryBuilder newBuilder() {
		return new AccessorFactoryBuilder();
	}

	/**
	 * 允许的错误次数
	 */
	int numberOfWrongs = 5;

	/**
	 * 错误的遗忘时间, 单位毫秒.
	 */
	int wrongTimeToForget = 5 * 60 * 1000;

	/**
	 * 琐定时间，单位毫秒.
	 */
	long lockTime = 5 * 60 * 1000;

	/**
	 * 访问器处理器.
	 */
	AccessorProcessor accessorProcessor;

	/**
	 * 时间计算器.
	 */
	AccessTimeCalculator accessTimeCalculator;

	/**
	 * 访问器容器.
	 */
	AccessorContainer accessorContainer;

	public AccessorFactoryBuilder numberOfWrongs(int numberOfWrongs) {
		this.numberOfWrongs = numberOfWrongs;
		return this;
	}

	public AccessorFactoryBuilder wrongTimeToForget(int wrongTimeToForget) {
		this.wrongTimeToForget = wrongTimeToForget;
		return this;
	}

	public AccessorFactoryBuilder lockTime(long lockTime) {
		this.lockTime = lockTime;
		return this;
	}

	public AccessorFactoryBuilder timeCalculator(AccessTimeCalculator accessTimeCalculator) {
		this.accessTimeCalculator = accessTimeCalculator;
		return this;
	}

	public AccessorFactoryBuilder accessorContainer(AccessorContainer accessorContainer) {
		this.accessorContainer = accessorContainer;
		return this;
	}

	public AccessorFactoryBuilder accessorFactory(AccessorProcessor accessorProcessor) {
		this.accessorProcessor = accessorProcessor;
		return this;
	}

	public AccessorFactory build() {

		if (this.accessorProcessor == null) {
			this.accessorProcessor = new MemoryAccessorProcessor();
		}

		if (this.accessTimeCalculator == null) {
			this.accessTimeCalculator = new DefaultAccessTimeCalculator();
		}

		if (this.accessorContainer == null) {
			this.accessorContainer = new CachedAccessorContainer();
		}
		AccessConfig accessConfig = new AccessConfig(this.numberOfWrongs, this.wrongTimeToForget, this.lockTime,
				this.accessTimeCalculator);
		this.accessorProcessor.setAccessConfig(accessConfig);

		return new DefaultAccessorFactory(this.accessorProcessor, this.accessorContainer);
	}

}
