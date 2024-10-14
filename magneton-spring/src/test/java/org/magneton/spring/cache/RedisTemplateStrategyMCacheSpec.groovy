package org.magneton.spring.cache

import org.magneton.cache.MCache
import org.magneton.cache.redis.RedisTemplateMCache
import org.magneton.spring.TestApplication
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

import javax.annotation.Resource

/**
 * RedisTemplateStrategyMCacheSpec
 * @author zhangmsh
 * @since 2024
 * @see RedisTemplateMCache
 */
@SpringBootTest(classes = TestApplication)
@ActiveProfiles("redis-template")
class RedisTemplateStrategyMCacheSpec extends Specification {

    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private MCache cache;

    def "test"() {
        when:
        def id = cache.clientId()
        then:
        redisTemplate != null
        id == "org.springframework.data.redis.core.RedisTemplate"
    }
}
