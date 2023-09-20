package org.magneton.module.safedog.access

import spock.lang.Specification

/**
 * Test case for {@link MemoryAccessorProcessor}
 * @author zhangmsh.
 * @since 2023.9
 */
class MemoryAccessorProcessorSpec extends Specification {


    def "test"() {
        given:
        def accessConfig = new AccessConfig()
        accessConfig.setNumberOfWrongs(3)
        accessConfig.setLockTime(1000)
        accessConfig.setWrongTimeToForget(1000)
        accessConfig.setAccessTimeCalculator(new DefaultAccessTimeCalculator())

        def accessorProcessor = new MemoryAccessorProcessor()
        accessorProcessor.setAccessConfig(accessConfig)

        when:
        def accessor = accessorProcessor.create("MemoryAccessorProcessorSpec")
        then:
        !accessor.locked()
        2 == accessor.onError()

        when:
        sleep(accessConfig.getWrongTimeToForget())
        then:
        2 == accessor.onError()

        when:
        accessor = accessorProcessor.create("MemoryAccessorProcessorSpec")
        accessConfig.getNumberOfWrongs().times {
            accessor.onError()
        }
        then:
        0 == accessor.onError()
        accessor.locked()

        when: "ttl"
        accessor = accessorProcessor.create("MemoryAccessorProcessorSpec")
        def ttl = accessor.ttl()
        then:
        ttl <= accessConfig.getLockTime()

        when: "wait ttl"
        sleep(accessConfig.getLockTime())
        then:
        accessor.ttl() == -1

        when: "reset"
        accessor.reset()
        then:
        !accessor.locked()
    }
}