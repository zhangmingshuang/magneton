/*
 * Copyright (c) 2020-2030  Xiamen Nascent Corporation. All rights reserved.
 *
 * https://www.nascent.cn
 *
 * 厦门南讯股份有限公司创立于2010年，是一家始终以技术和产品为驱动，帮助大消费领域企业提供客户资源管理（CRM）解决方案的公司。
 * 福建省厦门市软件园二期观日路22号401
 * 客服电话 400-009-2300
 * 电话 +86（592）5971731 传真 +86（592）5971710
 *
 * All source code copyright of this system belongs to Xiamen Nascent Co., Ltd.
 * Any organization or individual is not allowed to reprint, publish, disclose, embezzle, sell and use it for other illegal purposes without permission!
 */

package org.magneton.module.algorithm.set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * SetAlgoFileTest
 *
 * @author zhangmsh 2022/10/28
 * @since 2.1.0
 * @see SetAlgoFile
 */
class SetAlgoFileTest {

	@Test
	void of() {
		SetAlgoFile files = SetAlgoFile.of("a", "b");
		Assertions.assertEquals("[a, b]", files.getFiles().toString());

		files = SetAlgoFile.of(Lists.newArrayList("1", "2"));
		Assertions.assertEquals("[1, 2]", files.getFiles().toString());

		files = SetAlgoFile.of(Sets.newHashSet("1", "2"));
		Assertions.assertEquals("[1, 2]", files.getFiles().toString());

		files = SetAlgoFile.of("1", 1);
		Assertions.assertEquals("[1]", files.getFiles().toString());
		Assertions.assertEquals(1, files.getSkip());

		files = SetAlgoFile.of("1", 2, 1);
		Assertions.assertEquals("[1]", files.getFiles().toString());
		Assertions.assertEquals(2, files.getSkip());
		Assertions.assertEquals(1, files.getLimit());

		files = new SetAlgoFile(Lists.newArrayList("1"));
		Assertions.assertEquals("[1]", files.getFiles().toString());

		files = new SetAlgoFile(Lists.newArrayList("2"), 2, 1);
		Assertions.assertEquals("[2]", files.getFiles().toString());
		Assertions.assertEquals(2, files.getSkip());
		Assertions.assertEquals(1, files.getLimit());

	}

	@Test
	void test() {
		SetAlgoFile file = new SetAlgoFile();
		file.setFiles(Sets.newHashSet("1"));
		Assertions.assertEquals(1, file.getFiles().size());

		file.setFiles(Lists.newArrayList("1", "2"));
		Assertions.assertEquals(2, file.getFiles().size());

		file.setSkip(1);
		Assertions.assertEquals(1, file.getSkip());

		file.setLimit(1);
		Assertions.assertEquals(1, file.getLimit());
	}

	@Test
	void getRange() {
		SetAlgoFile algoFile = SetAlgoFile.of("a", "b", "c");
		SetAlgoFile algoFile1 = algoFile.get(1);
		Assertions.assertTrue(algoFile1.getFiles().size() == 1 && algoFile1.getFiles().contains("b"));

		SetAlgoFile algoFileRange = algoFile.getRange(2, algoFile.getFiles().size());
		System.out.println(algoFileRange);
		Assertions.assertTrue(algoFileRange.getFiles().size() == 1 && algoFileRange.getFiles().contains("c"));
	}

}