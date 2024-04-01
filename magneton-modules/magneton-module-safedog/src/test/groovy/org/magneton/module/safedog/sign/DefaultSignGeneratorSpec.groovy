package org.magneton.module.safedog.sign

import org.magneton.foundation.MoreStrings
import spock.lang.Specification

/**
 * Test Case for {@link DefaultSignGenerator}.
 * @author zhangmsh.
 * @since M2024
 */
class DefaultSignGeneratorSpec extends Specification {

    def "test sign string"() {
        given:
        def signGenerator = new DefaultSignGenerator("&")
        def data = new HashMap();
        data.put("test", "test")
        data.put("test1", "test1")
        data.put("test3", "")
        data.put("test4", null)

        when:
        def signString = signGenerator.getSignString(data, null, null)
        then:
        signString == "test=test&test1=test1"

        when:
        signString = signGenerator.getSignString(data, ["test"], null)
        then:
        signString == "test=test"

        when:
        signString = signGenerator.getSignString(data, ["test", "test1"], null)
        then:
        signString == "test=test&test1=test1"

        when:
        signGenerator = new DefaultSignGenerator()
        signString = signGenerator.getSignString(data, ["test", "test1", "test3"], "slat")
        String sign = signGenerator.generate(data, ["test", "test1", "test3"], "slat")
        then:
        signString == MoreStrings.format("test=test%s0test1=test1%s0slat", DefaultSignGenerator.DEFAULT_LINK_SYMBOL)
        "9643ad8c6079db73462e8acce5e6749e849dbe971e91a65a1788d28f5eb11212" == sign
    }

}