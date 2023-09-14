package org.magneton.module.distributed.cache;

import org.magneton.module.distributed.cache.ops.*;

/**
 * Distributed Cache Builder
 *
 * @author zhangmsh.
 * @since 2023.9
 */
public class DistributedCacheBuilder {

	HashOps hashOps;

	ListOps listOps;

	SetOps setOps;

	SortedSetOps sortedSetOps;

	ValueOps valueOps;

	CommonOps commonOps;

	public static DistributedCacheBuilder newBuilder() {
		return new DistributedCacheBuilder();
	}

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
			throw new IllegalArgumentException("hashOps must be not null");
		}
		if (this.listOps == null) {
			throw new IllegalArgumentException("listOps must be not null");
		}
		if (this.setOps == null) {
			throw new IllegalArgumentException("setOps must be not null");
		}
		if (this.sortedSetOps == null) {
			throw new IllegalArgumentException("sortedSetOps must be not null");
		}
		if (this.valueOps == null) {
			throw new IllegalArgumentException("valueOps must be not null");
		}
		if (this.commonOps == null) {
			throw new IllegalArgumentException("commonOps must be not null");
		}
		return new DistributedCacheImpl(this);
	}

	public interface Assembler {

		HashOps hashOps();

		ListOps listOps();

		SetOps setOps();

		SortedSetOps sortedSetOps();

		ValueOps valueOps();

		CommonOps commonOps();

	}

}
