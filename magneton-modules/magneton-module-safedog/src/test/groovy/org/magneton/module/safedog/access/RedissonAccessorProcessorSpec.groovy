package org.magneton.module.safedog.access

import org.mockito.Mockito
import org.redisson.api.RAtomicLong
import org.redisson.api.RBucket
import org.redisson.api.RedissonClient
import spock.lang.Specification

/**
 * Test case of {@link RedissonAccessorProcessor}.
 * @author zhangmsh.
 * @since 2023.9
 */
class RedissonAccessorProcessorSpec extends Specification {

    def "test"() {
        given:
        def rc = Mock(RedissonClient)

        def ac = new AccessConfig()
        def rap = new RedissonAccessorProcessor(rc)
        rap.setAccessConfig(ac)

        and: "mock"
        def rb = Mock(RBucket)
        rb.remainTimeToLive()
                // for ttl
                >> 11 >> -2
                // for locked
                >> 1 >> -1
        rc.getBucket(_ as String) >> rb

        when:
        def test = rap.create("test")
        then:
        test.ttl() == 11
        test.ttl() == -1
        test.locked()
        !test.locked()
    }

    def "test on error"() {
        given:
        def redissonClient = Mock(RedissonClient)

        def accessConfig = new AccessConfig()
        accessConfig.setNumberOfWrongs(2)
        accessConfig.setAccessTimeCalculator(new DefaultAccessTimeCalculator())
        def rap = new RedissonAccessorProcessor(redissonClient)
        rap.setAccessConfig(accessConfig)

        and: "mock"
        def rAtomicLong = Mock(RAtomicLong)
        rAtomicLong.incrementAndGet() >> 1 >> 3
        redissonClient.getAtomicLong(_ as String) >> rAtomicLong

        def rBucket = Mock(RBucket)
        1 * rBucket.set(_, _, _) >> _
        redissonClient.getBucket(_ as String) >> rBucket

        when:
        def test = rap.create("test")
        def remainError = test.onError();
        then:
        remainError == 1
        0 * rAtomicLong.delete()

        when:
        remainError = test.onError();
        then:
        remainError == 0
        1 * rAtomicLong.delete()
    }

    def "test on exception"() {
        given:
        def accessor = Mockito.mock(RedissonAccessorProcessor.RedissonAccessor)

        and: "mock"
        Mockito.when(accessor.locked()).thenThrow(RuntimeException)
        Mockito.when(accessor.onError()).thenCallRealMethod()
        
        when:
        accessor.onError()
        then:
        thrown(AccessException)
    }

}
