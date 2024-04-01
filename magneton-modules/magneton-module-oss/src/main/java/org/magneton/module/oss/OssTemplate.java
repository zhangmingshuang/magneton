package org.magneton.module.oss;

import com.google.common.base.Preconditions;

import javax.annotation.Nullable;
import java.io.File;

/**
 * .
 *
 * @author zhangmsh 25/03/2022
 * @since 2.0.7
 */
public interface OssTemplate<Sts> {

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

	/**
	 * 从文件名获取URL
	 * @param fileName 文件名
	 * @return URL
	 */
	default String getUrl(String fileName) {
		return this.getUrl(fileName, null);
	}

	/**
	 * 从文件名获取URL
	 * @param fileName 文件名
	 * @param bucket Bucket
	 * @return URL
	 */
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

	/**
	 * URL修正
	 * @param url URL
	 * @return 修正后的URL
	 */
	default String urlAmend(String url) {
		return this.urlAmend(url, null);
	}

	/**
	 * URL修正
	 * @param url URL
	 * @param bucket Bucket
	 * @return 修正后的URL
	 */
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