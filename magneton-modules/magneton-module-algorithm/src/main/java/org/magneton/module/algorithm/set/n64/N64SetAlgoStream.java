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

package org.magneton.module.algorithm.set.n64;

import java.util.List;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.magneton.module.algorithm.set.SetAlgoData;
import org.magneton.module.algorithm.set.SetAlgoFile;
import org.magneton.module.algorithm.set.SetAlgoResource;
import org.magneton.module.algorithm.set.SetAlgoStream;
import org.magneton.module.algorithm.set.SetStream;
import org.magneton.module.algorithm.set.StreamProcessor;
import org.magneton.module.algorithm.set.chain.ChainNode;
import org.magneton.module.algorithm.set.chain.SetAlgoChain;
import org.magneton.module.algorithm.set.chain.SetAlgoNode;
import org.magneton.module.algorithm.set.chain.SimpleSetAlgoChain;
import org.roaringbitmap.longlong.Roaring64Bitmap;

/**
 * 默认的集合算法流
 *
 * @author zhangmsh 2022/10/26
 * @since 2.1.0
 */
@Slf4j
public class N64SetAlgoStream implements SetAlgoResource<Long, N64SetAlgoStream>, SetAlgoStream<Long> {

	@Getter
	private final SetAlgoChain<Long, Roaring64Bitmap> chain = new SimpleSetAlgoChain<>();

	private final Bmp64ChainData chainData;

	public N64SetAlgoStream(Bmp64ChainData chainData) {
		this.chainData = chainData;
	}

	@Override
	public N64SetAlgoStream stream() {
		// 加载流
		this.doStream();
		// 数据流处理完成之后，移除已经处理完成的链
		this.chain.getChainNodes().clear();
		return this;
	}

	@Override
	public SetStream<Long> read() {
		Roaring64Bitmap data = this.chainData.getData();
		return new N64SetStream(data, this);
	}

	@Override
	public SetStream<Long> randomExtract(long size) {
		return new N64SetStream(this.chainData.randomExtract(size), this);
	}

	@Override
	public boolean isEmpty() {
		return this.chainData == null || this.chainData.isEmpty();
	}

	@Override
	public void clear() {
		if (this.chainData != null) {
			this.chainData.clear();
		}
		for (ChainNode<Long, Roaring64Bitmap> chainNode : this.chain.getChainNodes()) {
			chainNode.getNode().clear();
		}
	}

	private void doStream() {
		List<ChainNode<Long, Roaring64Bitmap>> chainNodes = this.chain.getChainNodes();

		long s = System.currentTimeMillis();
		SetAlgoNode<Long, Roaring64Bitmap> preNode = null;
		for (ChainNode<Long, Roaring64Bitmap> chainNode : chainNodes) {
			SetAlgoNode<Long, Roaring64Bitmap> algoNode = chainNode.getNode();
			StreamProcessor<Long> processor = chainNode.getProcessor();
			while (processor.hasNext()) {
				processor.nextData(data -> algoNode.useable(this.chainData, data));
				algoNode.eachStream(this.chainData, algoNode);
			}
			algoNode.eachNode(this.chainData, algoNode);
			preNode = algoNode;
		}
		if (log.isDebugEnabled()) {
			log.debug("doStream, used {} ms", (System.currentTimeMillis() - s));
		}
	}

	@Override
	public N64SetAlgoStream distinct(SetAlgoFile file) {
		Preconditions.checkNotNull(file, "file");
		this.addChainNode(new Bmp64DistinctSetAlgoNode(), new FileN64StreamProcessor(file));
		return this;
	}

	@Override
	public SetAlgoResource<Long, N64SetAlgoStream> distinct(SetAlgoData<Long> data) {
		Preconditions.checkNotNull(data, "data");
		this.addChainNode(new Bmp64DistinctSetAlgoNode(), new DataN64StreamProcessor(data));
		return this;
	}

	@Override
	public SetAlgoResource<Long, N64SetAlgoStream> distinct(SetStream<Long> stream) {
		Preconditions.checkNotNull(stream, "stream");
		this.addChainNode(new Bmp64DistinctSetAlgoNode(), new StreamN64StreamProcessor(stream));
		return this;
	}

	@Override
	public N64SetAlgoStream exclude(SetAlgoFile files) {
		Preconditions.checkNotNull(files, "file");
		this.addChainNode(new Bmp64ExcludeSetAlgoNode(), new FileN64StreamProcessor(files));
		return this;
	}

	@Override
	public SetAlgoResource<Long, N64SetAlgoStream> exclude(SetAlgoData<Long> data) {
		Preconditions.checkNotNull(data, "data");
		this.addChainNode(new Bmp64ExcludeSetAlgoNode(), new DataN64StreamProcessor(data));
		return this;
	}

	@Override
	public SetAlgoResource<Long, N64SetAlgoStream> exclude(SetStream<Long> stream) {
		Preconditions.checkNotNull(stream, "stream");
		this.addChainNode(new Bmp64ExcludeSetAlgoNode(), new StreamN64StreamProcessor(stream));
		return this;
	}

	@Override
	public N64SetAlgoStream intersect(SetAlgoFile file) {
		Preconditions.checkNotNull(file, "file");
		this.addChainNode(new Bmp64IntersectSetAlgoNode(), new FileN64StreamProcessor(file));
		return this;
	}

	@Override
	public SetAlgoResource<Long, N64SetAlgoStream> intersect(SetAlgoData<Long> data) {
		Preconditions.checkNotNull(data, "data");
		this.addChainNode(new Bmp64IntersectSetAlgoNode(), new DataN64StreamProcessor(data));
		return this;
	}

	@Override
	public SetAlgoResource<Long, N64SetAlgoStream> intersect(SetStream<Long> stream) {
		Preconditions.checkNotNull(stream, "stream");
		this.addChainNode(new Bmp64IntersectSetAlgoNode(), new StreamN64StreamProcessor(stream));
		return this;
	}

	@Override
	public N64SetAlgoStream union(SetAlgoFile file) {
		Preconditions.checkNotNull(file, "file");
		this.addChainNode(new Bmp64UnionSetAlgoNode(), new FileN64StreamProcessor(file));
		return this;
	}

	@Override
	public SetAlgoResource<Long, N64SetAlgoStream> union(SetAlgoData<Long> data) {
		Preconditions.checkNotNull(data, "data");
		this.addChainNode(new Bmp64UnionSetAlgoNode(), new DataN64StreamProcessor(data));
		return this;
	}

	@Override
	public SetAlgoResource<Long, N64SetAlgoStream> union(SetStream<Long> stream) {
		Preconditions.checkNotNull(stream, "stream");
		this.addChainNode(new Bmp64UnionSetAlgoNode(), new StreamN64StreamProcessor(stream));
		return this;
	}

	private void addChainNode(SetAlgoNode<Long, Roaring64Bitmap> node, N64StreamProcessor processor) {
		Preconditions.checkNotNull(node, "node");
		Preconditions.checkNotNull(node, "processor");
		this.chain.addChainNode(new ChainNode<>(node, processor));
	}

}
