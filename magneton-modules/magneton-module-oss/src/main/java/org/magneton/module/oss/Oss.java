package org.magneton.module.oss;

import java.io.File;
import javax.annotation.Nullable;

/**
 * .
 *
 * @author zhangmsh 25/03/2022
 * @since 2.0.7
 */
public interface Oss<Sts extends StsRes> {

	/**
	 * 简单上传
	 * @param sts STS
	 * @param fileName 文件目录与名称
	 * @param file 文件
	 * @param bucket Bucket
	 */
	void simpleUpdate(Sts sts, String fileName, File file, @Nullable String bucket);

	/**
	 * 简单上传
	 * @param sts STS
	 * @param fileName 文件目录与名称
	 * @param file 文件
	 */
	default void simpleUpdate(Sts sts, String fileName, File file) {
		this.simpleUpdate(sts, fileName, file, null);
	}

	/**
	 * 获取资源域名
	 * @param bucket Bucket
	 * @return 域名
	 */
	String getDomain(@Nullable String bucket);

	default String getUrl(String fileName) {
		return this.getUrl(fileName, null);
	}

	default String getUrl(String fileName, @Nullable String bucket) {
		String domain = this.getDomain(bucket);
		if (fileName.charAt(0) == '/') {
			return domain + fileName;
		}
		return domain + "/" + fileName;
	}

}
