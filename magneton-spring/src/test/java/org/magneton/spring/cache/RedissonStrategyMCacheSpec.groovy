package org.magneton.spring.cache

import org.magneton.cache.MCache
import org.magneton.spring.TestApplication
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import javax.annotation.Resource

/**
 *
 * @author zhangmsh
 * @since
 */
@SpringBootTest(classes = TestApplication)
class RedissonStrategyMCacheSpec extends Specification {

    @Resource
    private MCache cache;

    def "test"() {
        when:
        def id = cache.clientId()
        then:
        id == "org.redisson.Redisson"
    }
}
