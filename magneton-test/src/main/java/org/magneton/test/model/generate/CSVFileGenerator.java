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

package org.magneton.test.model.generate;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.io.Files;

public class CSVFileGenerator {

	private static final String LINE_SEPERATOR = System.getProperty("line.separator");

	private static final Charset UTF8_CHARSET = Charset.forName("utf-8");

	public static void generate(List<HashMap<String, Object>> data, String[] columns, String fileName) {
		File file = new File(fileName);
		if (file.exists()) {
			file.delete();
		}

		for (Map<String, Object> objects : data) {
			List<String> result = Lists.newArrayList();
			for (String column : columns) {
				if (objects.get(column) != null) {
					result.add(objects.get(column).toString());
				}
				else {
					result.add("");
				}
			}

			String lineData = Joiner.on(",").skipNulls().join(result);
			try {
				Files.append(lineData + LINE_SEPERATOR, file, UTF8_CHARSET);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
