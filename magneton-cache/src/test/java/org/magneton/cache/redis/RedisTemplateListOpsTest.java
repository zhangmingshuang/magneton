package org.magneton.cache.redis;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.magneton.cache.Cache;
import org.magneton.cache.CacheTestApplication;
import org.magneton.cache.ops.ListOps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * .
 *
 * @author zhangmsh 2021/6/24
 * @since
 */
@SpringBootTest(classes = CacheTestApplication.class)
class RedisTemplateListOpsTest {
  @Autowired private RedisTemplate redisTemplate;

  private Cache cache;
  private ListOps listOps;

  @BeforeEach
  void prepare() {
    this.cache = new RedisTemplateCache(this.redisTemplate);
    this.listOps = this.cache.opsForList();
  }

  @Test
  void testAdd() {
    String key = "add-key";
    this.cache.del(key);
    long count = this.listOps.add(key, "1", "2", "3");
    Assertions.assertEquals(3, count);
    List<String> datas = this.listOps.range(key, 0, -1);
    Assertions.assertEquals(3, datas.size());
    // 获取最后一个元素
    List<String> last = this.listOps.range(key, -1, -1);
    Assertions.assertEquals(1, last.size());
    Assertions.assertEquals("3", last.get(0));

    List<String> range = this.listOps.range(key, -3, 1);
    Assertions.assertEquals(2, range.size());
    Assertions.assertEquals("[1, 2]", range.toString());

    range = this.listOps.range(key, -1, 1);
    System.out.println(range);
    Assertions.assertTrue(range.isEmpty());
  }
}
