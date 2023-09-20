package org.magneton.module.distributed.cache;

import org.magneton.module.distributed.cache.ops.*;

/**
 * .
 *
 * @author zhangmsh 2021/6/24
 * @since 1.0.0
 */
public interface DistributedCache extends CommonOps {

	ValueOps opsForValue();

	ListOps opsForList();

	HashOps opsForHash();

	SetOps opsForSet();

	SortedSetOps opsForSortedSet();

}
