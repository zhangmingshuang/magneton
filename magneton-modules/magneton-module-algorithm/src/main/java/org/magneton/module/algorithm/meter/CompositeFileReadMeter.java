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

package org.magneton.module.algorithm.meter;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.google.common.base.Preconditions;

/**
 * 复合
 *
 * @author zhangmsh 2022/10/25
 * @since 2.1.0
 */
public class CompositeFileReadMeter implements FileReadMeter {

	private final List<FileReadMeter> fileReadMeters = new CopyOnWriteArrayList<>();

	public void add(FileReadMeter fileReadMeter) {
		this.fileReadMeters.add(Preconditions.checkNotNull(fileReadMeter, "fileReadMeter"));
	}

	@Override
	public boolean preRead(Path file) {
		for (FileReadMeter fileReadMeter : this.fileReadMeters) {
			if (!fileReadMeter.preRead(file)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void incrReadCount(Path file) {
		for (FileReadMeter fileReadMeter : this.fileReadMeters) {
			fileReadMeter.incrReadCount(file);
		}
	}

	@Override
	public void incrSkipCount(Path file) {
		for (FileReadMeter fileReadMeter : this.fileReadMeters) {
			fileReadMeter.incrSkipCount(file);
		}
	}

	@Override
	public <T> void ignoreCount(Path file, Object data) {
		for (FileReadMeter fileReadMeter : this.fileReadMeters) {
			fileReadMeter.ignoreCount(file, data);
		}
	}

}
