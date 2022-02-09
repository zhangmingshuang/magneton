package org.magneton.spring.launcher;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.magneton.spring.TestApplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * .
 *
 * @author zhangmsh 2022/2/7
 * @since 1.2.0
 */
@SpringBootTest(classes = TestApplication.class)
class RunLauncherTest {

	@Autowired
	private RunLauncher runLauncher;

	@Test
	void testSpi() {
		Assertions.assertEquals(1, SPIRunLauncher.FLAG);
	}

	@Test
	void testBean() {
		Assertions.assertNotNull(this.runLauncher);
		Assertions.assertEquals(1, SPIRunLauncher.FLAG);
		Assertions.assertEquals(1, BeanRunLauncher.FLAG);
	}

}