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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import com.google.common.base.CaseFormat;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

/**
 * Insert SQL 生成器
 *
 * @author Binary Wang
 */
public class InsertSQLGenerator {

	private static final Joiner COMMA_JOINER = Joiner.on(", ");

	private Connection con;

	private final String tableName;

	public InsertSQLGenerator(String url, String username, String password, String tableName) {
		try {
			this.con = DriverManager.getConnection(url, username, password);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}

		this.tableName = tableName;
	}

	public String generateSQL() {
		List<String> columns = this.getColumns();

		return String.format("insert into %s(%s) values(%s)", this.tableName, COMMA_JOINER.join(columns),
				COMMA_JOINER.join(Collections.nCopies(columns.size(), "?")));
	}

	public String generateParams() {
		return COMMA_JOINER.join(Collections2.transform(this.getColumns(), new Function<String, String>() {

			@Override
			public String apply(String input) {
				return "abc.get" + CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, input) + "()";
			}
		}));
	}

	private List<String> getColumns() {
		List<String> columns = Lists.newArrayList();
		try (PreparedStatement ps = this.con.prepareStatement("select * from " + this.tableName);
				ResultSet rs = ps.executeQuery();) {

			ResultSetMetaData rsm = rs.getMetaData();
			for (int i = 1; i <= rsm.getColumnCount(); i++) {
				String columnName = rsm.getColumnName(i);
				System.out.print("Name: " + columnName);
				System.out.println(", Type : " + rsm.getColumnClassName(i));
				columns.add(columnName);
			}

		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

		return columns;
	}

	public void close() {
		try {
			this.con.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
