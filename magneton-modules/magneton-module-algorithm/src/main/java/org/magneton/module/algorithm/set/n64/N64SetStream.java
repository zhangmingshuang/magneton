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

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import com.google.common.base.Preconditions;
import org.magneton.module.algorithm.set.SetAlgoStream;
import org.magneton.module.algorithm.set.SetStream;
import org.roaringbitmap.longlong.PeekableLongIterator;
import org.roaringbitmap.longlong.Roaring64Bitmap;

/**
 * long型输出流
 *
 * @author zhangmsh 2022/10/28
 * @since 2.1.0
 */
public class N64SetStream implements SetStream<Long> {

	private final Roaring64Bitmap data;

	private final SetAlgoStream<Long> stream;

	public N64SetStream(Roaring64Bitmap data, SetAlgoStream<Long> stream) {
		this.data = data;
		this.stream = stream;
	}

	@Override
	public List<Long> fetch() {
		List<Long> result = new LinkedList<>();
		this.data.forEach(result::add);
		return result;
	}

	@Override
	public Iterator<Long> iterator() {
		PeekableLongIterator iterator = this.data.getLongIterator();
		return new Iterator<Long>() {
			@Override
			public boolean hasNext() {
				return iterator.hasNext();
			}

			@Override
			public Long next() {
				return iterator.next();
			}
		};
	}

	@Override
	public void forEach(Consumer<Long> consumer) {
		this.data.forEach(consumer::accept);
	}

	@Override
	public long write(Path path, List<String> headers) {
		Preconditions.checkNotNull(path, "path");
		Preconditions.checkNotNull(headers, "header");
		this.writeTo(this.data, path, headers);
		return this.data.getLongCardinality() + headers.size();
	}

	@Override
	public long size() {
		return this.data.getLongCardinality();
	}

	@Override
	public boolean isEmpty() {
		return this.data.isEmpty();
	}

	@Override
	public void clear() {
		this.data.clear();
		this.stream.clear();
	}

	private void writeTo(Roaring64Bitmap writedBitmap, Path path, List<String> headers) {
		try (FileOutputStream fos = new FileOutputStream(path.toFile());
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos))) {
			PeekableLongIterator iterator = writedBitmap.getLongIterator();
			boolean first = true;
			for (String header : headers) {
				if (!first) {
					writer.newLine();
				}
				writer.write(header);
				first = false;
			}
			while (iterator.hasNext()) {
				if (!first) {
					writer.newLine();
				}
				long val = iterator.next();
				writer.write(String.valueOf(val));
				first = false;
			}
		}
		catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

}
