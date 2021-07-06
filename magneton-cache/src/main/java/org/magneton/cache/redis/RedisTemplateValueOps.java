package org.magneton.cache.redis;

import java.util.List;
import org.magneton.cache.EKV;
import org.magneton.cache.KV;
import org.magneton.cache.ops.ValueOps;
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
public class RedisTemplateValueOps extends AbstractRedisTemplateOps implements ValueOps {

  public RedisTemplateValueOps(
      RedisTemplate redisTemplate, RedisValueSerializer redisValueSerializer) {
    super(redisTemplate, redisValueSerializer);
  }

  @Override
  public boolean set(KV kv) {
    return (boolean)
        this.redisTemplate.execute(
            (RedisCallback) cb -> cb.set(this.toByte(kv.getKey()), this.serialize(kv.getValue())));
  }

  @Override
  public List<Boolean> set(List<KV> kvs) {
    return this.redisTemplate.executePipelined(
        (RedisCallback<Boolean>)
            rc -> {
              kvs.forEach(kv -> rc.set(this.toByte(kv.getKey()), this.serialize(kv.getValue())));
              return null;
            });
  }

  @Override
  public boolean setNx(KV kv) {
    return (boolean)
        this.redisTemplate.execute(
            (RedisCallback) rc -> rc.setNX(this.toByte(kv.getKey()), this.serialize(kv.getValue())));
  }

  @Override
  public List<Boolean> setNx(List<KV> kvs) {
    return this.redisTemplate.executePipelined(
        (RedisCallback<Boolean>)
            rc -> {
              kvs.forEach(kv -> rc.setNX(this.toByte(kv.getKey()), this.serialize(kv.getValue())));
              return null;
            });
  }

  @Override
  public boolean setEx(EKV ekv) {
    return (boolean)
        this.redisTemplate.execute(
            (RedisCallback)
                cb ->
                    cb.setEx(
                        this.toByte(ekv.getKey()), ekv.getExpire(), this.serialize(ekv.getValue())));
  }

  @Override
  public List<Boolean> setEx(List<EKV> ekvs) {
    return this.redisTemplate.executePipelined(
        (RedisCallback<Boolean>)
            rc -> {
              ekvs.forEach(
                  ekv ->
                      rc.setEx(
                          this.toByte(ekv.getKey()), ekv.getExpire(), this.serialize(ekv.getValue())));
              return null;
            });
  }

  @Override
  public String get(String key) {
    byte[] response =
        (byte[]) this.redisTemplate.execute((RedisCallback) cb -> cb.get(this.toByte(key)));
    return Datas.isEmpty(response) ? null : Trans.toStr(response);
  }

  @Override
  public <T> T get(String key, Class<T> clazz) {
    byte[] response =
        (byte[]) this.redisTemplate.execute((RedisCallback) cb -> cb.get(this.toByte(key)));
    return Datas.isEmpty(response) ? null : this.deserialize(response, clazz);
  }
}
