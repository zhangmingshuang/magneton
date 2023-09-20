package org.magneton.module.safedog.access

import spock.lang.Specification

/**
 * Test case of {@link CachedAccessorContainer}
 * @author zhangmsh.
 * @since 2023.9
 */
class CachedAccessorContainerSpec extends Specification {

    def "test"() {
        given:
        def cac = new CachedAccessorContainer()

        when:
        def accessor = Mock(Accessor)
        cac.put("test", accessor)

        then:
        cac.get("test") == accessor
    }
}
