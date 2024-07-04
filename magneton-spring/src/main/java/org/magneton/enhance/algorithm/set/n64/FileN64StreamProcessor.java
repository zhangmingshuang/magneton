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

import org.magneton.enhance.algorithm.file.FileContentLoader;
import org.magneton.enhance.algorithm.file.FileContextReadListener;
import org.magneton.enhance.algorithm.file.LineFileContextLoader;
import org.magneton.enhance.algorithm.meter.FileReadMeter;
import org.magneton.enhance.algorithm.meter.Meters;
import org.magneton.enhance.algorithm.set.SetAlgoFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.function.Function;

/**
 * 文件流处理器
 *
 * @author zhangmsh 2022/10/28
 * @since 2.1.9
 */
public class FileN64StreamProcessor implements N64StreamProcessor {

	private final FileReadMeter fileReadMeter = Meters.fileReadMeter();

	private final FileContentLoader fileContentLoader = new LineFileContextLoader();

	private final SetAlgoFile algoFile;

	private final Iterator<String> iterator;

	public FileN64StreamProcessor(SetAlgoFile algoFile) {
		this.algoFile = algoFile;
		this.iterator = algoFile.getFiles().iterator();
	}

	@Override
	public boolean hasNext() {
		return this.iterator.hasNext();
	}

	@Override
	public void nextData(Function<String, Boolean> accessor) {
		String processFile = this.iterator.next();
		Path path = Paths.get(processFile);
		this.doFileStream(path, accessor);
	}

	private void doFileStream(Path path, Function<String, Boolean> accessor) {
		if (!this.fileReadMeter.preRead(path)) {
			return;
		}
		int skip = this.algoFile.getSkip();
		int limit = this.algoFile.getLimit();
		this.fileContentLoader.load(path, new FileContextReadListener() {
			private int readLine = 0;

			@Override
			public boolean onRead(Path path, String data) {
				FileN64StreamProcessor.this.fileReadMeter.incrReadCount(path);
				if (this.readLine++ < skip || data == null || data.isEmpty()) {
					FileN64StreamProcessor.this.fileReadMeter.incrSkipCount(path);
					return true;
				}
				if (limit > 0 && this.readLine > limit) {
					return false;
				}
				boolean next = accessor.apply(data);
				if (!next) {
					FileN64StreamProcessor.this.fileReadMeter.ignoreCount(path, data);
					return true;
				}
				return true;
			}
		});
	}

}
