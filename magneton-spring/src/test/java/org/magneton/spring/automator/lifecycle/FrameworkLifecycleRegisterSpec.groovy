package org.magneton.spring.automator.lifecycle

import org.magneton.spring.test.ALifeCycle
import org.magneton.spring.test.BLifeCycle
import org.magneton.spring.test.PriorityALifeCycle
import org.magneton.spring.test.PriorityBLifeCycle
import spock.lang.Specification

/**
 * Test Case For {@link FrameworkLifecycleRegister}
 * @author zhangmsh
 * @since 2024
 */
class FrameworkLifecycleRegisterSpec extends Specification {

    def init() {
        given:
        def list = FrameworkLifecycleRegister.getPriorityFrameworkApplications()
        when:
        def aI = list.findIndexOf { it.class == PriorityALifeCycle }
        def bI = list.findIndexOf { it.class == PriorityBLifeCycle }
        println "a: $aI, b: $bI"
        then:
        aI < bI

        when:
        list = FrameworkLifecycleRegister.getFrameworkApplications()
        aI = list.findIndexOf { it.class == ALifeCycle }
        bI = list.findIndexOf { it.class == BLifeCycle }
        println "a: $aI, b: $bI"
        then:
        aI > bI
    }

    def register() {
        given:
        def a = new ALifeCycle()
        def b = new BLifeCycle()
        when:
        FrameworkLifecycleRegister.register(a)
        FrameworkLifecycleRegister.register(b)
        def lifecycles = FrameworkLifecycleRegister.getFrameworkApplications()

        def ai = lifecycles.indexOf(a)
        def bi = lifecycles.indexOf(b)
        then:
        ai > bi
    }

    def unregister() {
        given:
        def a = new ALifeCycle()
        def b = new BLifeCycle()
        when:
        FrameworkLifecycleRegister.register(a)
        FrameworkLifecycleRegister.register(b)

        FrameworkLifecycleRegister.unregister(a)
        def lifecycles = FrameworkLifecycleRegister.getFrameworkApplications()
        def ai = lifecycles.indexOf(a)
        then:
        ai == -1
    }
}