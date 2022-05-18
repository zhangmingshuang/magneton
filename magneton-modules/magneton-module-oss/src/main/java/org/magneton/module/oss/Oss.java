package org.magneton.module.oss;

import java.io.File;
import javax.annotation.Nullable;
import org.magneton.core.base.Preconditions;

/**
 * .
 *
 * @author zhangmsh 25/03/2022
 * @since 2.0.7
 */
public interface Oss<Sts> {

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
		if (Preconditions.checkNotNull(fileName).startsWith("http")) {
			return fileName;
		}
		String domain = this.getDomain(bucket);
		if (fileName.charAt(0) == '/') {
			return domain + fileName;
		}
		return domain + "/" + fileName;
	}

	default String urlAmend(String url) {
		return this.urlAmend(url, null);
	}

	default String urlAmend(String url, @Nullable String bucket) {
		if (!Preconditions.checkNotNull(url).startsWith("http")) {
			return url;
		}
		String domain = this.getDomain(bucket);
		if (url.startsWith(domain)) {
			url = url.replace(domain, "");
		}
		else {
			url = url.replace(domain.replace("https://", "http://"), "");
		}
		if (url.charAt(0) == '/') {
			return url;
		}
		return url;
	}

}
