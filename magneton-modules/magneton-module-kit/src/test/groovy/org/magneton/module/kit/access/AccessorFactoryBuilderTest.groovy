package org.magneton.module.kit.access

import spock.lang.Specification

/**
 * Test case for {@link AccessorFactoryBuilder}.
 * @author zhangmsh.
 * @since 2023.1
 */
class AccessorFactoryBuilderTest extends Specification {

    def "test"() {
        given:
        def builder = AccessorFactoryBuilder.newBuilder()
        when:
        def factory = builder.build()
        then:
        factory != null
    }
}
