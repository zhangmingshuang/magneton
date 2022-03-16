package org.magneton.support.admin.basic.impl;

import org.junit.jupiter.api.Test;
import org.magneton.support.admin.basic.service.BasicSysConfigService;
import org.magneton.support.admin.basic.service.impl.BasicSysConfigServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * .
 *
 * @author zhangmsh 2022/2/16
 * @since 1.2.0
 * @see BasicSysConfigServiceImpl
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class BasicSysConfigServiceImplTest {

	@Autowired
	private BasicSysConfigService basicSysConfigService;

	@Test
	void getValue() {
		String test = this.basicSysConfigService.getValue("test");
		System.out.println(test);
		test = this.basicSysConfigService.getValue("test");
		System.out.println(test);
		test = this.basicSysConfigService.getValue("test");
		System.out.println(test);
	}

}