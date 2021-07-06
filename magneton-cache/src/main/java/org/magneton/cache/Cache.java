package org.magneton.cache;

import org.magneton.cache.ops.HashOps;
import org.magneton.cache.ops.ListOps;
import org.magneton.cache.ops.ValueOps;

/**
 * .
 *
 * @author zhangmsh 2021/6/24
 * @since 1.0.0
 */
public interface Cache {

  ValueOps opsForValue();

  ListOps opsForList();

  HashOps opsForHash();

  /**
   * 获取Key的过期时间(秒）
   *
   * @param key Key
   * @return Key的过期时间，如果Key不存在，则返回-2，如果Key存在但是是永久的，返回-1。
   */
  long ttl(String key);

  boolean expire(String key, long expire);

  boolean exists(String key);

  /**
   * 删除一个Key
   *
   * @param keys 要删除的Key
   * @return 成功删除的个数
   */
  long del(String... keys);
}
