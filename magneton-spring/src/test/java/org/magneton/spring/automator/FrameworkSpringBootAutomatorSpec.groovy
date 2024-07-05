package org.magneton.spring.automator

import org.magneton.spring.TestApplication
import org.magneton.spring.automator.lifecycle.FrameworkLifecycleRunner
import org.magneton.spring.test.ALifeCycle
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

/**
 * Test Case For {@link FrameworkSpringBootAutomator}
 * @author zhangmsh
 * @since 2024
 */
@SpringBootTest(classes = TestApplication)
class FrameworkSpringBootAutomatorSpec extends Specification {


    def test() {
        expect:
        def get = FrameworkLifecycleRunner.get(FrameworkSpringBootAutomator.FRAMEWORK_BOOTSTRAP_CONTEXT)
        get != null
        println get
        ALifeCycle.ref.get()
    }


}
