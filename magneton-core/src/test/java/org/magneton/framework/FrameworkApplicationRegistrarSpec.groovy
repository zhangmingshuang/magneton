package org.magneton.framework


import spock.lang.Specification

/**
 * Test Case For {@link FrameworkApplicationRegistrar}.
 * @author zhangmsh.
 * @since 2024
 */
class FrameworkApplicationRegistrarSpec extends Specification {

    void "register"() {
        given:
        FrameworkApplicationRegistrar.register(new TestFrameworkApplication(1))
        FrameworkApplicationRegistrar.register(new TestFrameworkApplication(3))
        FrameworkApplicationRegistrar.register(new TestFrameworkApplication(2))
        when:
        def applications = FrameworkApplicationRegistrar.getFrameworkApplications()
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