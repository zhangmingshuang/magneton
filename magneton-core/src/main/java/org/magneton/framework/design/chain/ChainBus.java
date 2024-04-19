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

package org.magneton.framework.design.chain;

import com.google.common.base.Preconditions;
import org.magneton.foundation.collection.CopyOnWriteHashMap;
import org.magneton.framework.design.exception.DuplicateChainException;
import org.magneton.framework.design.exception.DuplicateChainNodeException;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * Chains
 *
 * @author zhangmsh.
 * @since 1.0.0
 */
public class ChainBus {

	private static final String P_CHAIN_CONTEXT = "##CHAIN_CONTEXT#";

	private static final Map<String, Chain> CHAINS = new CopyOnWriteHashMap<>();

	private static final Map<String, ChainNode> NODES = new CopyOnWriteHashMap<>();

	private static final ThreadLocal<ChainContext> CHAIN_CONTEXT = new InheritableThreadLocal<>();

	private ChainBus() {
		// private
	}

	public static void addNode(String nodeId, ChainNode node) {
		ChainNode oldNode = NODES.put(nodeId, node);
		if (oldNode != null) {
			throw new DuplicateChainNodeException(
					String.format("duplicate node %s found. (%s and %s)", nodeId, oldNode.getClass(), node.getClass()));
		}
	}

	public static boolean containNode(String nodeId) {
		return NODES.containsKey(nodeId);
	}

	@Nullable
	public static ChainNode getNode(String nodeId) {
		return NODES.get(nodeId);
	}

	@Nullable
	public static Chain getChain(String id) {
		return CHAINS.get(id);
	}

	public static void addChain(Chain chain) {
		Chain oldChain = CHAINS.put(chain.getChainId(), chain);
		if (oldChain != null) {
			throw new DuplicateChainException(String.format("duplicate chain %s found", chain.getChainId()));
		}
	}

	public static void removeChain(String chainId) {
		Chain chain = CHAINS.remove(chainId);
		if (chain != null) {
			chain.getChainNodes().clear();
			chain = null;
		}
	}

	public static boolean containChain(String chainId) {
		return CHAINS.containsKey(chainId);
	}

	public static void setChainData(String key, Object value) {
		ChainContext context = CHAIN_CONTEXT.get();
		if (context == null) {
			context = new ChainContext();
			CHAIN_CONTEXT.set(context);
		}
		context.setData(key, value);
	}

	public static void setChainContext(ChainContext chainContext) {
		Preconditions.checkNotNull(chainContext, "chainContext");
		setChainData(P_CHAIN_CONTEXT, chainContext);
	}

	@Nullable
	public static ChainContext getChainContext() {
		return CHAIN_CONTEXT.get() == null ? null : CHAIN_CONTEXT.get().getData(P_CHAIN_CONTEXT);
	}

	public static void removeChainContext() {
		CHAIN_CONTEXT.remove();
	}

}