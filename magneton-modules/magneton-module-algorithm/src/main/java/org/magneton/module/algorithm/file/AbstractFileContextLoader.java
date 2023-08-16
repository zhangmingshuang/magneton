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

package org.magneton.module.algorithm.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.common.base.Preconditions;
import org.magneton.module.algorithm.exception.NotFileException;

/**
 * 抽象的文件内容加载器
 *
 * @author zhangmsh 2022/10/26
 * @since 2.1.0
 */
public abstract class AbstractFileContextLoader implements FileContentLoader {

	@Override
	public void load(Path path, FileContextReadListener listener) {
		Preconditions.checkNotNull(path, "path");
		Preconditions.checkNotNull(listener, "listener");
		if (!Files.exists(path)) {
			throw new NotFileException(path.toString());
		}
		try {
			FileContentReader reader = this.newFileContentReader(path);
			try {
				while (reader.hasNext()) {
					String body = reader.next();
					if (!listener.onRead(path, body)) {
						break;
					}
				}
			}
			finally {
				reader.close();
			}
		}
		catch (Exception e) {
			throw new NotFileException(e);
		}
	}

	/**
	 * 创建文件内容读取器
	 * @param path 文件地址
	 * @return 读取器
	 * @throws IOException IO异常
	 */
	protected abstract FileContentReader newFileContentReader(Path path) throws IOException;

}
