package cn.nascent.framework.test.core;

import cn.nascent.framework.test.annotation.TestComponentScan;
import cn.nascent.framework.test.injector.InjectorFactory;
import org.junit.jupiter.api.Test;

/**
 * .
 *
 * @author zhangmsh 2021/8/2
 * @since
 */
@TestComponentScan("cn.nascent.framework.test")
class ChaosContextTest implements ChaosApplication {

	@Test
	void test() {
		ChaosContext.init(ChaosContextTest.class);
		InjectorFactory injectorFactory = ChaosContext.get(InjectorFactory.class);
		System.out.println(injectorFactory);
	}

}
