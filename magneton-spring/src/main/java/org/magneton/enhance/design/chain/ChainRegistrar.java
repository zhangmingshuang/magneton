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

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.ToString;
import org.magneton.enhance.design.exception.DuplicateChainException;
import org.magneton.enhance.design.exception.ExtNotFoundException;
import org.magneton.foundation.collection.CopyOnWriteHashMap;

import java.util.Comparator;
import java.util.Map;

/**
 * 链构建器
 *
 * @author zhangmsh.
 * @since 1.0.0
 */
@Getter
@ToString
public class ChainRegistrar {

	private static final Map<String, ChainRegistrar> CHAIN_REGISTRARS = new CopyOnWriteHashMap<>();

	private final Chain chain;

	public static ChainRegistrar get(String chainId) {
		Chain existChain = ChainBus.getChain(chainId);
		if (existChain == null) {
			CHAIN_REGISTRARS.computeIfPresent(chainId, (k, v) -> null);
			return null;
		}
		return CHAIN_REGISTRARS.computeIfAbsent(chainId, k -> new ChainRegistrar(existChain));
	}

	public static ChainRegistrar getOrRegister(String chainId) {
		ChainRegistrar existChainRegistrar = get(chainId);
		if (existChainRegistrar != null) {
			return existChainRegistrar;
		}
		return register(chainId);
	}

	public static ChainRegistrar register(String chainId) {
		Preconditions.checkNotNull(chainId, "chainId must be not null");

		if (ChainBus.containChain(chainId)) {
			throw new DuplicateChainException(String.format("duplicate chain %s found", chainId));
		}
		return new ChainRegistrar(chainId);
	}

	public static void unregister(String chainId) {
		CHAIN_REGISTRARS.computeIfPresent(chainId, (k, v) -> null);
		ChainBus.removeChain(chainId);
	}

	public ChainRegistrar(Chain chain) {
		this.chain = chain;
	}

	private ChainRegistrar(String chainId) {
		this.chain = new Chain(chainId);
		CHAIN_REGISTRARS.put(chainId, this);
		ChainBus.addChain(this.chain);
	}

	public void remove() {
		unregister(this.chain.getChainId());
	}

	public ChainRegistrar add(ChainNode chainNode) {
		this.chain.add(chainNode);
		return this;
	}

	public ChainRegistrar add(String nodeId, ChainNode chainNode) {
		this.chain.add(nodeId, chainNode);
		return this;
	}

	public ChainRegistrar add(String nodeId) {
		ChainNode node = ChainBus.getNode(nodeId);
		if (node == null) {
			throw new ExtNotFoundException(String.format("node %s not found", nodeId));
		}
		this.chain.add(nodeId, node);
		return this;
	}

	public ChainRegistrar sort(Comparator<ChainNode> comparator) {
		this.chain.sort(comparator);
		return this;
	}

}