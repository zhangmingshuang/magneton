package org.magneton.module.safedog.access

import spock.lang.Specification

/**
 * Test case for {@link DefaultAccessorFactory}
 * @author zhangmsh.
 * @since 2023.9
 */
class DefaultAccessorFactorySpec extends Specification {

    def "test"() {
        given:
        def ap = Mock(AccessorProcessor)
        def ac = Mock(AccessorContainer)

        def accessor = Mock(Accessor)
        def daf = new DefaultAccessorFactory(ap, ac)
        and: "mock"
        ap.create(_ as String) >> accessor
        ac.get(_ as String) >> null >> null

        when:
        def testAccessor = daf.get("test")
        then:
        testAccessor != null
        // accessor created by AccessorProcessor at first time when uncached
        // so the second time will get from AccessorContainer
        accessor == daf.get("test")
    }
}
