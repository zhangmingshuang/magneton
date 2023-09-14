package org.magneton.module.safedog.geetest

import spock.lang.Specification

/**
 * Test case for {@link GeeTestBehaviorConfig}
 * @author zhangmsh.
 * @since 2023.9
 */
class GeeTestBehaviorConfigSpec extends Specification {

    def "test"() {
        given:
        def cfg = new GeeTestBehaviorConfig()

        when:
        cfg.validate()
        then:
        thrown(IllegalArgumentException)

        when:
        cfg.setCaptchaId("test")
        cfg.setCaptchaKey("test")

        cfg.validate()
        then:
        notThrown(Throwable)

        cleanup:
        cfg.toString()
    }
}
