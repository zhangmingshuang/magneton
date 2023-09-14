package org.magneton.module.safedog.access

import spock.lang.Specification

/**
 * Test case for {@link AccessConfig}
 * @author zhangmsh.
 * @since 2023.9
 */
class AccessConfigSpec extends Specification {


    def "test"() {
        given:
        def calc = new DefaultAccessTimeCalculator()

        def accessConfig = new AccessConfig()
        accessConfig.setNumberOfWrongs(3)
        accessConfig.setWrongTimeToForget(1000)
        accessConfig.setLockTime(1000)
        accessConfig.setAccessTimeCalculator(calc)

        def accessConfig2 = new AccessConfig(3, 1000, 1000, calc)

        expect:
        accessConfig == accessConfig2

        cleanup:
        accessConfig.toString()
    }
}
