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

package org.magneton.enhance.algorithm.set.n64;

import org.magneton.enhance.algorithm.set.chain.ChainData;
import org.roaringbitmap.longlong.Roaring64Bitmap;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

/**
 * 基于Bitmap的链路数据
 *
 * @author zhangmsh 2022/10/27
 * @since 2.1.0
 */
public class Bmp64ChainData implements ChainData<Long, Roaring64Bitmap> {

	private final Roaring64Bitmap bitmap = new Roaring64Bitmap();

	@Override
	public boolean contains(Long val) {
		return this.bitmap.contains(val);
	}

	@Override
	public void add(Long val) {
		this.bitmap.addLong(val);
	}

	@Override
	public void remove(Long val) {
		this.bitmap.removeLong(val);
	}

	@Override
	public boolean isEmpty() {
		return this.bitmap.isEmpty();
	}

	@Override
	public Roaring64Bitmap getData() {
		return this.bitmap;
	}

	@Override
	public void clear() {
		this.bitmap.clear();
	}

	@Override
	public long size() {
		return this.bitmap.getLongCardinality();
	}

	@Override
	public Roaring64Bitmap randomExtract(long size) {
		Roaring64Bitmap extractBitmap = new Roaring64Bitmap();
		this.randomExtract(size, extractBitmap::addLong);
		return extractBitmap;
	}

	private void randomExtract(long size, Consumer<Long> consumer) {
		if (size < 1) {
			return;
		}
		long dataSize = this.size();
		if (size >= dataSize) {
			this.bitmap.forEach(consumer::accept);
			this.bitmap.clear();
			return;
		}
		Roaring64Bitmap mark = new Roaring64Bitmap();
		int deep;
		for (long i = 0; i < size; i++) {
			deep = 0;
			long idx = ThreadLocalRandom.current().nextLong(0, dataSize);
			while (mark.contains(idx)) {
				idx = ThreadLocalRandom.current().nextLong(0, dataSize);
				deep++;
				if (deep > 1000) {
					throw new RuntimeException("无法找到合适的数据");
				}
			}
			long val = this.bitmap.select(idx);
			consumer.accept(val);
			// 抽取之后，原结果集要丢失该数据
			this.remove(val);
			dataSize--;
		}
	}

}
