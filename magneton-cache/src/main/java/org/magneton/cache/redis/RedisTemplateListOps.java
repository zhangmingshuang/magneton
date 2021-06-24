package org.magneton.cache.redis;

import java.util.Collections;
import java.util.List;
import org.magneton.cache.ops.ListOps;
import org.magneton.cache.util.Datas;
import org.magneton.cache.util.Trans;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * .
 *
 * @author zhangmsh 2021/6/24
 * @since 1.0.0
 */
@SuppressWarnings("ConstantConditions")
public class RedisTemplateListOps extends AbstractRedisTemplateOps implements ListOps {

  public RedisTemplateListOps(RedisTemplate redisTemplate) {
    super(redisTemplate);
  }

  @Override
  public long add(String list, List<String> values) {
    return (long)
        this.redisTemplate.execute(
            (RedisCallback) rc -> rc.rPush(Trans.toByte(list), Trans.toBytes(values)));
  }

  @Override
  public List<String> range(String list, long start, long end) {
    List<byte[]> response =
        (List<byte[]>)
            this.redisTemplate.execute(
                (RedisCallback) rc -> rc.lRange(Trans.toByte(list), start, end));

    return Datas.isEmpty(response) ? Collections.emptyList() : Trans.toStr(response);
  }
}
