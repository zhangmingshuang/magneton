package org.magneton.cache.redis;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.magneton.cache.Cache;
import org.magneton.cache.CacheTestApplication;
import org.magneton.cache.KV;
import org.magneton.cache.ops.ValueOps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * .
 *
 * @author zhangmsh 2021/6/24
 * @since 1.0.0
 */
@SpringBootTest(classes = CacheTestApplication.class)
public class RedisTemplateValueOpsTest {

  @Autowired private RedisTemplate redisTemplate;

  private Cache cache;
  private ValueOps valueOps;

  @BeforeEach
  void prepare() {
    this.cache = new RedisTemplateCache(this.redisTemplate);
    this.valueOps = this.cache.opsForValue();
  }

  @Test
  void testKeyValue() {
    this.valueOps.set("key", "value");
    String value = this.valueOps.get("key");
    Assertions.assertEquals("value", value);
    // 覆盖
    this.valueOps.set("key", "value2");
    value = this.valueOps.get("key");
    Assertions.assertEquals("value2", value);

    List<Boolean> response = this.valueOps.set(KV.of("a", "b"), KV.of("c", "d"));
    Assertions.assertEquals(2, response.size());
    Assertions.assertTrue(
        () -> {
          for (Boolean aBoolean : response) {
            if (!aBoolean) {
              return false;
            }
          }
          return true;
        });
    long del = this.cache.del("a", "c");
    Assertions.assertEquals(2, del);
  }

  @Test
  void testNx() {
    boolean b = this.valueOps.setNx("key", "value");
    Assertions.assertTrue(b);
    b = this.valueOps.setNx("key", "value2");
    Assertions.assertFalse(b);
    long num = this.cache.del("key");
    Assertions.assertTrue(num > 0);
  }
}
