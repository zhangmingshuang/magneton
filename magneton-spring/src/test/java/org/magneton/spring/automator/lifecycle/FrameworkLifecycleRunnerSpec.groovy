package org.magneton.spring.automator.lifecycle

import org.magneton.spring.test.ALifeCycle
import org.magneton.spring.test.BLifeCycle
import spock.lang.Specification

/**
 * Test Case For {@link FrameworkLifecycleRunner}
 * @author zhangmsh
 * @since 2024
 */
class FrameworkLifecycleRunnerSpec extends Specification {

    final String p = "FrameworkLifecycleRunnerSpec:";

    def safePut() {
        given:
        def k = p + "safePut"
        FrameworkLifecycleRunner.remove(k)
        FrameworkLifecycleRunner.safePut(k, "test")
        when:
        def val = FrameworkLifecycleRunner.get(k)
        then:
        val == "test"

        when:
        FrameworkLifecycleRunner.safePut(k, "test1")
        then:
        thrown(IllegalArgumentException)

        cleanup:
        FrameworkLifecycleRunner.remove(k)
    }

    def put() {
        given:
        def k = p + "put"
        FrameworkLifecycleRunner.remove(k)
        FrameworkLifecycleRunner.put(k, "test")
        when:
        FrameworkLifecycleRunner.put(k, "test1")
        then:
        FrameworkLifecycleRunner.get(k) == "test1"

        cleanup:
        FrameworkLifecycleRunner.remove(k)
    }

    def putIfAbsent() {
        given:
        def k = p + "putIfAbsent"
        FrameworkLifecycleRunner.remove(k)
        FrameworkLifecycleRunner.putIfAbsent(k, "test")
        when:
        FrameworkLifecycleRunner.putIfAbsent(k, "test1")
        then:
        FrameworkLifecycleRunner.get(k) == "test"

        cleanup:
        FrameworkLifecycleRunner.remove(k)
    }

    def starting() {
        when:
        FrameworkLifecycleRunner.starting()
        then:
        BLifeCycle.ref.get()
        ALifeCycle.ref.get()
    }
}
