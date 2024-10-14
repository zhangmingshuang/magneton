package org.magneton.spring.comp

import org.magneton.enhance.auth.MTokenAuth
import org.magneton.spring.TestApplication
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

import javax.annotation.Resource

/**
 * 禁用缓存，所以此时无法获取到 MTokenAuth
 * @author zhangmsh
 * @since 2024
 */
@SpringBootTest(classes = TestApplication)
@ActiveProfiles("nilcache")
class NilCachedTokenAuthTest extends Specification {

    @Resource
    private ApplicationContext applicationContext;

    def "test"() {
        when:
        applicationContext.getBean(MTokenAuth)
        then:
        thrown(Throwable)
    }


}
