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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.magneton.enhance.design.exception.DuplicateChainNodeException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 链数据
 *
 * @author zhangmsh.
 * @since 1.0.0
 */
@ToString
public class Chain {

	/**
	 * 链路ID
	 */
	private String chainId;

	/**
	 * 链路可执行节点 key=nodeId, value=node
	 */
	private List<Node> chainNodes = new ArrayList<>();

	public Chain(String chainId) {
		this.chainId = chainId;
	}

	public String getChainId() {
		return this.chainId;
	}

	public List<Node> getChainNodes() {
		return this.chainNodes;
	}

	public void sort(Comparator<ChainNode> comparator) {
		if (this.chainNodes == null) {
			return;
		}
		this.chainNodes.sort((n1, n2) -> comparator.compare(n1.getNode(), n2.getNode()));
	}

	public void add(ChainNode chainNode) {
		String nodeId = chainNode.getClass().getName();
		this.add(nodeId, chainNode);
	}

	public void add(String nodeId, ChainNode chainNode) {
		Node newNode = new Node(nodeId, chainNode);
		for (Node node : this.chainNodes) {
			if (node.getNodeId().equals(newNode.getNodeId())) {
				throw new DuplicateChainNodeException(String.format("duplicate node %s found. (%s and %s)", nodeId,
						newNode.getNode().getClass(), node.getNode().getClass()));
			}
		}
		this.chainNodes.add(newNode);
	}

	@Getter
	@AllArgsConstructor
	public static class Node {

		private String nodeId;

		private ChainNode node;

	}

}