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

package org.magneton.enhance.algorithm.file;

import com.google.common.base.Preconditions;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

/**
 * 按行加载的文件内容加载器
 *
 * @author zhangmsh 2022/10/26
 * @since 2.1.0
 */
public class LineFileContextLoader extends AbstractFileContextLoader {

	private final int bufferSize;

	private final Charset charset;

	public LineFileContextLoader() {
		this(1024 * 1024, StandardCharsets.UTF_8);
	}

	public LineFileContextLoader(int bufferSize, Charset charset) {
		this.bufferSize = Math.max(1024 * 1024, bufferSize);
		this.charset = Preconditions.checkNotNull(charset, "charset");
	}

	@Override
	protected FileContentReader newFileContentReader(Path path) throws IOException {
		return new LineFileContentReader(new FileInputStream(path.toFile()), this.bufferSize, this.charset);
	}

	public static class LineFileContentReader implements FileContentReader {

		private final BufferedInputStream bis;

		private final BufferedReader reader;

		private final FileInputStream fis;

		public LineFileContentReader(FileInputStream fis, int bufferSize, Charset charset) {
			this.fis = fis;
			this.bis = new BufferedInputStream(fis);
			this.reader = new BufferedReader(new InputStreamReader(this.bis, charset), bufferSize);
		}

		@Override
		public boolean hasNext() throws IOException {
			return this.reader.ready();
		}

		@Override
		public String next() throws IOException {
			return this.reader.readLine();
		}

		@Override
		public void close() {
			try {
				this.fis.close();
				this.bis.close();
				this.reader.close();
			}
			catch (Exception e) {
				// ignore
			}
		}

	}

}
