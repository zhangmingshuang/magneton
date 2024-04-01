package org.magneton.module.safedog.sign

import com.google.common.collect.Sets
import spock.lang.Specification

/**
 * Test Case for {@link DefaultSignKeySorter}.
 * @author zhangmsh.
 * @since M2024
 */
class DefaultSignKeySorterSpec extends Specification {

    void "test"() {
        given:
        def sorter = new DefaultSignKeySorter()

        when:
        def sorted = sorter.sort(null)
        then:
        sorted.isEmpty()

        when:
        def keys = Sets.newHashSet("a", "c", "d", "b")
        sorted = sorter.sort(keys)
        then:
        sorted == ["a", "b", "c", "d"]
    }
}