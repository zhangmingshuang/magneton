package org.magneton.framework.core.safedog.access;

/**
 * Accessor factory builder.
 *
 * @author zhangmsh.
 * @since 2023.1
 */
public class SafeDogAccessorFactoryBuilder {

	public static SafeDogAccessorFactoryBuilder newBuilder() {
		return new SafeDogAccessorFactoryBuilder();
	}

	/**
	 * 允许的错误次数
	 */
	private int numberOfWrongs = 5;

	/**
	 * 错误的遗忘时间, 单位毫秒.
	 */
	private int wrongTimeToForget = 5 * 60 * 1000;

	/**
	 * 琐定时间，单位毫秒.
	 */
	private long lockTime = 5 * 60 * 1000;

	/**
	 * 访问器处理器.
	 */
	private SafeDogAccessorProcessor safeDogAccessorProcessor;

	/**
	 * 时间计算器.
	 */
	private AccessTimeCalculator accessTimeCalculator;

	/**
	 * 访问器容器.
	 */
	private AccessorContainer accessorContainer;

	public SafeDogAccessorFactoryBuilder numberOfWrongs(int numberOfWrongs) {
		this.numberOfWrongs = numberOfWrongs;
		return this;
	}

	public SafeDogAccessorFactoryBuilder wrongTimeToForget(int wrongTimeToForget) {
		this.wrongTimeToForget = wrongTimeToForget;
		return this;
	}

	public SafeDogAccessorFactoryBuilder lockTime(long lockTime) {
		this.lockTime = lockTime;
		return this;
	}

	public SafeDogAccessorFactoryBuilder timeCalculator(AccessTimeCalculator accessTimeCalculator) {
		this.accessTimeCalculator = accessTimeCalculator;
		return this;
	}

	public SafeDogAccessorFactoryBuilder accessorContainer(AccessorContainer accessorContainer) {
		this.accessorContainer = accessorContainer;
		return this;
	}

	public SafeDogAccessorFactoryBuilder accessorProcessor(SafeDogAccessorProcessor safeDogAccessorProcessor) {
		this.safeDogAccessorProcessor = safeDogAccessorProcessor;
		return this;
	}

	public SafeDogAccessorFactory build() {

		if (this.safeDogAccessorProcessor == null) {
			this.safeDogAccessorProcessor = new MemorySafeDogAccessorProcessor();
		}

		if (this.accessTimeCalculator == null) {
			this.accessTimeCalculator = new DefaultAccessTimeCalculator();
		}

		if (this.accessorContainer == null) {
			this.accessorContainer = new CachedAccessorContainer();
		}

		AccessConfig accessConfig = new AccessConfig(this.numberOfWrongs, this.wrongTimeToForget, this.lockTime,
				this.accessTimeCalculator);
		this.safeDogAccessorProcessor.setAccessConfig(accessConfig);

		return new DefaultSafeDogAccessorFactory(this.safeDogAccessorProcessor, this.accessorContainer);
	}

}
