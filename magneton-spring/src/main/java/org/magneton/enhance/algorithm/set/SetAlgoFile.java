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

package org.magneton.enhance.algorithm.set;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Set;

/**
 * 集合算法配置
 *
 * @author zhangmsh 2022/10/26
 * @since 2.1.0
 */
@Setter
@Getter
@ToString
public class SetAlgoFile {

	/**
	 * 文件列表
	 */
	private List<String> files;

	/**
	 * 读文件时跳过的行，小于1则表示不跳过
	 */
	private int skip;

	/**
	 * 限制行数，小于1表示不限制
	 */
	private int limit;

	public static SetAlgoFile of(String... files) {
		return new SetAlgoFile(Lists.newArrayList(files));
	}

	public static SetAlgoFile of(List<String> files) {
		return new SetAlgoFile(files);
	}

	public static SetAlgoFile of(Set<String> files) {
		return new SetAlgoFile(Lists.newArrayList(files));
	}

	public static SetAlgoFile of(String file, int skip) {
		return of(file, skip, -1);
	}

	public static SetAlgoFile of(String file, int skip, int limit) {
		SetAlgoFile algoFile = new SetAlgoFile();
		algoFile.setFiles(Lists.newArrayList(file));
		algoFile.setSkip(skip);
		algoFile.setLimit(limit);
		return algoFile;
	}

	public SetAlgoFile() {

	}

	public SetAlgoFile(List<String> files) {
		this.files = files;
	}

	public SetAlgoFile(List<String> files, int skip, int limit) {
		this.files = files;
		this.skip = skip;
		this.limit = limit;
	}

	public SetAlgoFile get(int i) {
		String file = this.files.get(i);
		return SetAlgoFile.of(file).skip(this.skip).limit(this.limit);
	}

	public SetAlgoFile getRange(int start, int end) {
		List<String> rangeFiles = this.files.subList(start, end);
		return new SetAlgoFile(Lists.newArrayList(rangeFiles)).skip(this.skip).limit(this.limit);
	}

	public void setFiles(Set<String> files) {
		this.files = Lists.newArrayList(files);
	}

	public void setFiles(List<String> files) {
		this.files = files;
	}

	public SetAlgoFile skip(int skip) {
		this.skip = skip;
		return this;
	}

	public SetAlgoFile limit(int limit) {
		this.limit = limit;
		return this;
	}

}
