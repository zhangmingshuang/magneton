package org.magneton.module.distributed.cache;

import org.magneton.module.distributed.cache.ops.*;

/**
 * Distributed Cache Builder
 *
 * @author zhangmsh.
 * @since 2023.9
 */
public class DistributedCacheBuilder {

	/**
	 * HashOps for Hash Operations
	 */
	HashOps hashOps;

	/**
	 * ListOps for List Operations
	 */
	ListOps listOps;

	/**
	 * SetOps for Set Operations
	 */
	SetOps setOps;

	/**
	 * SortedSetOps for Sorted Set Operations
	 */
	SortedSetOps sortedSetOps;

	/**
	 * ValueOps for Value Operations
	 */
	ValueOps valueOps;

	/**
	 * CommonOps for Common Operations
	 */
	CommonOps commonOps;

	/**
	 * 创建一个新的Builder
	 */
	public static DistributedCacheBuilder newBuilder() {
		return new DistributedCacheBuilder();
	}

	/**
	 * 创建一个新的Builder
	 * @param assembler 组装器
	 * @return Builder
	 */
	public static DistributedCacheBuilder newBuilder(Assembler assembler) {
		DistributedCacheBuilder builder = new DistributedCacheBuilder();
		builder.hashOps = assembler.hashOps();
		builder.listOps = assembler.listOps();
		builder.setOps = assembler.setOps();
		builder.sortedSetOps = assembler.sortedSetOps();
		builder.valueOps = assembler.valueOps();
		builder.commonOps = assembler.commonOps();
		return builder;
	}

	public DistributedCacheBuilder hashOps(HashOps hashOps) {
		this.hashOps = hashOps;
		return this;
	}

	public DistributedCacheBuilder listOps(ListOps listOps) {
		this.listOps = listOps;
		return this;
	}

	public DistributedCacheBuilder setOps(SetOps setOps) {
		this.setOps = setOps;
		return this;
	}

	public DistributedCacheBuilder sortedSetOps(SortedSetOps sortedSetOps) {
		this.sortedSetOps = sortedSetOps;
		return this;
	}

	public DistributedCacheBuilder valueOps(ValueOps valueOps) {
		this.valueOps = valueOps;
		return this;
	}

	public DistributedCacheBuilder commonOps(CommonOps commonOps) {
		this.commonOps = commonOps;
		return this;
	}

	public DistributedCache build() {
		if (this.hashOps == null) {
			throw new IllegalArgumentException("hashOps must not be null");
		}
		if (this.listOps == null) {
			throw new IllegalArgumentException("listOps must not be null");
		}
		if (this.setOps == null) {
			throw new IllegalArgumentException("setOps must not be null");
		}
		if (this.sortedSetOps == null) {
			throw new IllegalArgumentException("sortedSetOps must not be null");
		}
		if (this.valueOps == null) {
			throw new IllegalArgumentException("valueOps must not be null");
		}
		if (this.commonOps == null) {
			throw new IllegalArgumentException("commonOps must not be null");
		}
		return new DistributedCacheImpl(this);
	}

	public interface Assembler {

		/**
		 * 获取HashOps
		 * @return HashOps
		 */
		HashOps hashOps();

		/**
		 * 获取ListOps
		 * @return ListOps
		 */
		ListOps listOps();

		/**
		 * 获取SetOps
		 * @return SetOps
		 */
		SetOps setOps();

		/**
		 * 获取SortedSetOps
		 * @return SortedSetOps
		 */
		SortedSetOps sortedSetOps();

		/**
		 * 获取ValueOps
		 * @return ValueOps
		 */
		ValueOps valueOps();

		/**
		 * 获取CommonOps
		 * @return CommonOps
		 */
		CommonOps commonOps();

	}

}
