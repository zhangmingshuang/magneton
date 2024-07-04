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

package org.magneton.enhance.algorithm.meter;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.util.concurrent.atomic.LongAdder;

/**
 * 简单的文件读取指标
 *
 * @author zhangmsh 2022/10/25
 * @since 2.1.0
 */
@Slf4j
public class SimpleFileReadMeter implements FileReadMeter {

	@Getter
	private final LongAdder readCount = new LongAdder();

	@Getter
	private final LongAdder skipCount = new LongAdder();

	@Getter
	private final LongAdder ignoreCount = new LongAdder();

	@Override
	public boolean preRead(Path file) {
		if (log.isDebugEnabled()) {
			log.debug("reading {}", file);
		}
		return true;
	}

	@Override
	public void incrReadCount(Path file) {
		this.readCount.increment();
	}

	@Override
	public void incrSkipCount(Path file) {
		this.skipCount.increment();
	}

	@Override
	public <T> void ignoreCount(Path file, Object data) {
		this.ignoreCount.increment();
	}

}
