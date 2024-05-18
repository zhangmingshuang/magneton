package org.magneton.foundation.str

import spock.lang.Specification;

/**
 * Test Case For {@link BitStatus}.
 *
 * @author zhangmsh.
 * @since 2024
 */
class BitStatusSpec extends Specification {

    def "test"() {
        given:
        BitStatus bitStatus = new BitStatus()
        when:
        bitStatus.add(4)
        then:
        bitStatus.getStatus() == 4
        bitStatus.has(4)

        when:
        bitStatus.add(2)
        then:
        bitStatus.getStatus() == 6
        bitStatus.has(2)
        bitStatus.has(4)

        when:
        bitStatus.remove(4)
        then:
        bitStatus.getStatus() == 2
        bitStatus.has(2)
        !bitStatus.has(4)
    }

}