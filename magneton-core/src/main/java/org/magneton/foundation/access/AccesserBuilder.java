package org.magneton.foundation.access;

import com.google.common.base.Preconditions;

/**
 * .
 *
 * @author zhangmsh 2021/2/25
 * @since 4.0.0
 */
public class AccesserBuilder {

	private final AbstractAccesser accesser;

	/** lock data size limit. release the last data if timeout. */
	private int lockSize = 2048;

	/** error count of lock. locked if out of count. */
	private int lockErrorCount = 5;

	/** lock time. lock release if timeout. */
	private int lockTime = 1000;

	/** error record size limit. */
	private int errorRecordSize = 2048;

	/** error record time. release the last record if timeout. */
	private int errorRecordTime = 5 * 60 * 1000;

	private AccesserBuilder(AbstractAccesser accesser) {
		this.accesser = Preconditions.checkNotNull(accesser);
	}

	public static AccesserBuilder of(AbstractAccesser accesser) {
		return new AccesserBuilder(Preconditions.checkNotNull(accesser));
	}

	public AccesserBuilder errorRecordSize(int errorRecordSize) {
		this.errorRecordSize = Math.max(errorRecordSize, 1);
		return this;
	}

	public AccesserBuilder errorRecordTime(int errorRecordTime) {
		this.errorRecordTime = Math.max(errorRecordTime, 1000);
		return this;
	}

	public AccesserBuilder lockErrorCount(int lockErrorCount) {
		this.lockErrorCount = Math.max(lockErrorCount, 1);
		return this;
	}

	public AccesserBuilder lockSize(int lockSize) {
		this.lockSize = Math.max(lockSize, 1);
		return this;
	}

	public AccesserBuilder lockTime(int lockTime) {
		this.lockTime = Math.max(lockTime, 1000);
		return this;
	}

	/**
	 * build.
	 * @return accesser.
	 */
	public Accesser build() {
		AccessConfig config = new AccessConfig();
		config.setLockTime(this.lockTime);
		config.setLockSize(this.lockSize);
		config.setLockErrorCount(this.lockErrorCount);
		config.setErrorRecordTime(this.errorRecordTime);
		config.setErrorRecordSize(this.errorRecordSize);
		this.accesser.afterConfigSet(config);
		return this.accesser;
	}

}
