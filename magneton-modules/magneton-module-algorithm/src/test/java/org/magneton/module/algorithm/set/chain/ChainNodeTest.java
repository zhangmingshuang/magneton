/*
 * Copyright (c) 2020-2030  Xiamen Nascent Corporation. All rights reserved.
 *
 * https://www.nascent.cn
 *
 * 厦门南讯股份有限公司创立于2010年，是一家始终以技术和产品为驱动，帮助大消费领域企业提供客户资源管理（CRM）解决方案的公司。
 * 福建省厦门市软件园二期观日路22号501
 * 客服电话 400-009-2300
 * 电话 +86（592）5971731 传真 +86（592）5971710
 *
 * All source code copyright of this system belongs to Xiamen Nascent Co., Ltd.
 * Any organization or individual is not allowed to reprint, publish, disclose, embezzle, sell and use it for other illegal purposes without permission!
 */

package org.magneton.module.algorithm.set.chain;

import java.util.function.Function;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.magneton.module.algorithm.set.StreamProcessor;

/**
 * ChainNodeTest
 *
 * @author zhangmsh 2022/11/4
 * @since 2.2.0
 * @see ChainNode
 */
class ChainNodeTest {

	@Test
	void getNode() {
		ChainNode chainNode = new ChainNode(null, null);
		Assertions.assertNull(chainNode.getNode());

		SetAlgoNode setAlgoNode = new SetAlgoNode() {
			@Override
			public boolean useable(ChainData chainData, String data) {
				return false;
			}
		};
		chainNode = new ChainNode(setAlgoNode, null);
		Assertions.assertEquals(setAlgoNode, chainNode.getNode());
		Assertions.assertFalse(setAlgoNode.useable(null, null));
	}

	@Test
	void getProcessor() {
		ChainNode chainNode = new ChainNode(null, null);
		Assertions.assertNull(chainNode.getProcessor());

		StreamProcessor streamProcessor = new StreamProcessor() {

			@Override
			public boolean hasNext() {
				return false;
			}

			@Override
			public void nextData(Function accessor) {
				accessor.apply("test");
			}
		};
		chainNode = new ChainNode(null, streamProcessor);
		Assertions.assertEquals(streamProcessor, chainNode.getProcessor());

		streamProcessor.nextData(t -> {
			Assertions.assertEquals("test", t);
			return null;
		});
	}

}