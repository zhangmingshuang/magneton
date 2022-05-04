package org.magneton.module.distributed.cache;

import java.util.List;
import javax.annotations.Underperforming;
import org.magneton.module.distributed.cache.ops.HashOps;
import org.magneton.module.distributed.cache.ops.ListOps;
import org.magneton.module.distributed.cache.ops.SetOps;
import org.magneton.module.distributed.cache.ops.ValueOps;

/**
 * .
 *
 * @author zhangmsh 2021/6/24
 * @since 1.0.0
 */
public interface DistributedCache {

	ValueOps opsForValue();

	ListOps opsForList();

	HashOps opsForHash();

	SetOps opsForSet();

	/**
	 * 获取Key的过期时间(秒）
	 * @param key Key
	 * @return Key的过期时间，如果Key不存在，则返回-2，如果Key存在但是是永久的，返回-1。
	 */
	long ttl(String key);

	/**
	 * 设置过期
	 * @param key 要过期的Key
	 * @param expire 设置过期时间，单位秒
	 * @return 是否设置成功。如果key不存在则返回 {@code false}，如果已存在，不管Key是否已经有过期策略，只要设置成功，则返回
	 * {@code true}
	 */
	boolean expire(String key, long expire);

	/**
	 * 判断是否存在
	 * @param key 要判断的Key
	 * @return 如果存在则返回 {@code true}，否则返回 {@code false}
	 */
	boolean exists(String key);

	/**
	 * 删除一个Key
	 * @param keys 要删除的Key
	 * @return 成功删除的个数
	 */
	long del(String... keys);

	void flushDb();

	/**
	 * 查找Key列表
	 *
	 * <pre>
	 *    	h?llo matches hello, hallo and hxllo
	 * 		h*llo matches hllo and heeeello
	 * 		h[ae]llo matches hello and hallo, but not hillo
	 * 		h[^e]llo matches hallo, hbllo, ... but not hello
	 * 		h[a-b]llo matches hallo and hbllo
	 * </pre>
	 * @param pattern Key表达式
	 * @return 查找到的列表
	 */
	@Underperforming
	List<String> keys(String pattern);

}
