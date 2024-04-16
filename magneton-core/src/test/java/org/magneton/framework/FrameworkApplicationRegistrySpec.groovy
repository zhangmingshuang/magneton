package org.magneton.framework


import spock.lang.Specification

/**
 * Test Case For {@link FrameworkApplicationRegistry}.
 * @author zhangmsh.
 * @since 2024
 */
class FrameworkApplicationRegistrySpec extends Specification {

    void "register"() {
        given:
        FrameworkApplicationRegistry.register(new TestFrameworkApplication(1))
        FrameworkApplicationRegistry.register(new TestFrameworkApplication(3))
        FrameworkApplicationRegistry.register(new TestFrameworkApplication(2))
        when:
        def applications = FrameworkApplicationRegistry.getFrameworkApplications()
        then:
        applications.size() == 3
        applications.get(0).getOrder() == 1
        applications.get(1).getOrder() == 2
        applications.get(2).getOrder() == 3
    }


    public static class TestFrameworkApplication implements FrameworkApplication {
        private int order;

        TestFrameworkApplication(int order) {
            this.order = order
        }

        @Override
        int getOrder() {
            return order;
        }

        @Override
        void starting() {

        }
    }
}