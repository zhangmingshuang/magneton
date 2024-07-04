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

package org.magneton.enhance.design.chain;

import com.google.common.base.MoreObjects;
import lombok.extern.slf4j.Slf4j;
import org.magneton.enhance.design.exception.ExtNotFoundException;

import javax.annotation.Nullable;
import java.util.List;

/**
 * 责任链执行器
 *
 * @author zhangmsh 2022/11/22
 * @since 1.0.0
 */
@Slf4j
public class ChainExecutor {

	private ChainExecutor() {
		// private
	}

	/**
	 * 链执行
	 * @param chainId 要执行的链ID
	 * @return 执行结果
	 */
	public static ChainContext execute(String chainId) {
		return execute(chainId, null);
	}

	public static ChainContext executeAndClean(String chainId) {
		return executeAndClean(chainId, null);
	}

	public static <T extends ChainContext> T executeAndClean(String chainId, @Nullable T chainContext) {
		Chain chain = ChainBus.getChain(chainId);
		if (chain == null) {
			throw new ExtNotFoundException(String.format("not chain found for %s", chainId));
		}
		ChainContext cc = MoreObjects.firstNonNull(chainContext, new ChainContext());
		doExecute(chain, cc, true);
		return (T) cc;
	}

	public static <T extends ChainContext> T execute(String chainId, @Nullable T chainContext) {
		Chain chain = ChainBus.getChain(chainId);
		if (chain == null) {
			throw new ExtNotFoundException(String.format("not chain found for %s", chainId));
		}
		ChainContext cc = MoreObjects.firstNonNull(chainContext, new ChainContext());
		doExecute(chain, cc, false);
		return (T) cc;
	}

	private static void doExecute(Chain chain, ChainContext chainContext, boolean clean) {
		List<Chain.Node> nodes = chain.getChainNodes();
		ChainBus.setChainContext(chainContext);
		try {
			nodes.forEach(node -> node.getNode().beforeAll());
			for (Chain.Node node : nodes) {
				ChainNode chainNode = node.getNode();
				if (!chainNode.preProcess()) {
					continue;
				}
				chainNode.process();
				chainNode.afterProcess();
				if (chainNode.isEnd()) {
					break;
				}
			}
			nodes.forEach(node -> node.getNode().afterAll());

			if (clean) {
				ChainBus.removeChain(chain.getChainId());
			}
		}
		finally {
			ChainBus.removeChainContext();
		}
	}

}