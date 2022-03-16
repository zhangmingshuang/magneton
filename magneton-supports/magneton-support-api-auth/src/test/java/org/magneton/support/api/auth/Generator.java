package org.magneton.support.api.auth;

import cn.org.atool.generator.FileGenerator;
import cn.org.atool.generator.annotation.Table;
import cn.org.atool.generator.annotation.Tables;

/**
 * .
 *
 * @author zhangmsh 2022/2/16
 * @since 1.2.0
 */
class Generator {

	public static final String url = "jdbc:mysql://localhost:3306/magneton?useUnicode=true&characterEncoding=utf8";

	public static void main(String[] args) {
		FileGenerator.build(Empty.class);
	}

	@Tables(
			// 设置数据库连接信息
			url = url, username = "root", password = "123456",
			// 设置entity类生成src目录, 相对于 user.dir
			srcDir = "/magneton-supports/magneton-support-api-auth/src/main/java",
			// 设置entity类的package值
			basePack = "org.magneton.support.api.auth",
			// 设置dao接口和实现的src目录, 相对于 user.dir
			daoDir = "/magneton-supports/magneton-support-api-auth/src/main/java",
			// 生成的数据实体的后缀
			entitySuffix = "DO",
			// 设置哪些表要生成Entity文件
			tables = { @Table(value = {

					"api_auth_log", "api_auth_statistics", "api_auth_user"

			}) })
	static class Empty {

	}

}
