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

package org.magneton.module.algorithm.set;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.magneton.module.algorithm.set.n64.N64SetAlgoStream;

/**
 * SetAlgorithmTest
 *
 * @author zhangmsh 2022/10/27
 * @since 2.1.0
 * @see SetAlgorithm
 */
class SetAlgorithmTest {

	@SneakyThrows
	@Test
	void emptyFile() {
		String f = "/tmp-empty.txt";
		Path path = Paths.get(f);
		Files.deleteIfExists(path);
		Files.createFile(path);

		SetAlgoFile saf = SetAlgoFile.of(f).skip(1);
		List<Long> ids = SetAlgorithm.number64().distinct(saf).stream().read().fetch();
		Assertions.assertEquals(0, ids.size());

		Files.deleteIfExists(path);
	}

	@Test
	void testDistinct() {
		N64SetAlgoStream stream = SetAlgorithm.number64().distinct(1L, 2L, 2L, 3L, 4L).stream();
		List<Long> ids = stream.read().fetch();
		Assertions.assertEquals("[1, 2, 3, 4]", ids.toString());

		ids = SetAlgorithm.number64().distinct(1L, 2L, 2L, 3L).distinct(4L, 4L, 5L, 6L).stream().read().fetch();
		Assertions.assertEquals("[1, 2, 3, 4, 5, 6]", ids.toString());

		ids = SetAlgorithm.number64().distinct(1L, 2L).distinct(2L, 3L).stream().read().fetch();
		Assertions.assertEquals("[1, 2, 3]", ids.toString());

		SetStream<Long> read = SetAlgorithm.number64().distinct(1L, 2L, 3L, 3L).stream().read();
		ids = SetAlgorithm.number64().distinct(4L).distinct(read).stream().read().fetch();
		Assertions.assertEquals("[1, 2, 3, 4]", ids.toString());
	}

	@Test
	void testStreamDistinct() {
		N64SetAlgoStream algoStream = SetAlgorithm.number64().distinct(1L, 2L, 2L, 3L, 4L).stream();
		SetStream<Long> stream = algoStream.read();

		SetStream<Long> stream2 = SetAlgorithm.number64().distinct(3L).intersect(stream).stream().read();
		List<Long> ids = stream2.fetch();
		Assertions.assertEquals("[3]", ids.toString());

		Assertions.assertEquals("[1, 2, 3, 4]", algoStream.read().fetch().toString());
		Assertions.assertEquals("[3, 4]", algoStream.intersect(3L, 4L).stream().read().fetch().toString());
	}

	@Test
	void testMultiFileDistinct() throws Exception {
		Path path1 = Paths.get("/tmp-m1.txt");
		Path path2 = Paths.get("/tmp-m2.txt");
		try {
			this.write(path1.toFile(), 1L, 2L, 3L, 3L, 4L);
			this.write(path2.toFile(), 4L, 5L, 5L, 6L);

			Set<String> s1 = Sets.newHashSet(path1.toString());
			Set<String> s2 = Sets.newHashSet(path2.toString());

			List<Long> ids = SetAlgorithm.number64().distinct(s1).stream().read().fetch();
			Assertions.assertEquals("[1, 2, 3, 4]", ids.toString());

			ids = SetAlgorithm.number64().distinct(s1).distinct(s2).stream().read().fetch();
			Assertions.assertEquals("[1, 2, 3, 4, 5, 6]", ids.toString());

			N64SetAlgoStream stream = SetAlgorithm.number64().distinct(s1).stream();
			ids = stream.read().fetch();
			Assertions.assertEquals("[1, 2, 3, 4]", ids.toString());

			ids = stream.distinct(s2).stream().read().fetch();
			Assertions.assertEquals("[1, 2, 3, 4, 5, 6]", ids.toString());
		}
		finally {
			Files.deleteIfExists(path1);
			Files.deleteIfExists(path2);
		}
	}

	@Test
	void testIntersect() {
		List<Long> ids = SetAlgorithm.number64().intersect(1L, 2L, 2L, 3L).stream().read().fetch();
		Assertions.assertEquals("[]", ids.toString());

		ids = SetAlgorithm.number64().distinct(1L, 2L, 2L, 3L).intersect(2L, 3L).stream().read().fetch();
		Assertions.assertEquals("[2, 3]", ids.toString());

		ids = SetAlgorithm.number64().distinct(1L, 2L, 2L, 3L).stream().intersect(2L).stream().read().fetch();
		Assertions.assertEquals("[2]", ids.toString());

		ids = SetAlgorithm.number64().distinct(2L, 3L, 3L).intersect(3L, 4L, 5L, 1L).stream().read().fetch();
		Assertions.assertEquals("[3]", ids.toString());

		ids = SetAlgorithm.number64().distinct(2L, 3L).intersect(4L, 5L).stream().read().fetch();
		Assertions.assertEquals("[]", ids.toString());

		ids = SetAlgorithm.number64().distinct(1L, 2L, 2L, 3L)
				// 取交集
				.intersect(2L, 3L).stream().read().fetch();
		Assertions.assertEquals("[2, 3]", ids.toString());
	}

	@Test
	void testFileIntersect() throws IOException {
		Path path1 = Paths.get("/tmp-i1.txt");
		Path path2 = Paths.get("/tmp-i2.txt");
		Path path3 = Paths.get("/tmp-i3.txt");
		Set<String> s1 = Sets.newHashSet(path1.toString());
		Set<String> s2 = Sets.newHashSet(path2.toString());
		Set<String> s3 = Sets.newHashSet(path3.toString());
		try {
			this.write(path1.toFile(), 1L, 2L, 3L, 3L, 4L);
			this.write(path2.toFile(), 3L, 3L, 4L);
			this.write(path3.toFile(), 5L, 6L, 7L);

			List<Long> ids = SetAlgorithm.number64().intersect(s1).stream().read().fetch();
			Assertions.assertEquals("[]", ids.toString(), "没有前置数据集交集一定为空");

			ids = SetAlgorithm.number64().distinct(s1).intersect(s2).stream().read().fetch();
			Assertions.assertEquals("[3, 4]", ids.toString());

			ids = SetAlgorithm.number64().distinct(s2).intersect(s1).stream().read().fetch();
			Assertions.assertEquals("[3, 4]", ids.toString());

			ids = SetAlgorithm.number64().distinct(s2).intersect(s3).stream().read().fetch();
			Assertions.assertEquals("[]", ids.toString());

			ids = SetAlgorithm.number64().distinct(s1).stream().intersect(s2).intersect(s3).stream().read().fetch();
			Assertions.assertEquals("[]", ids.toString());
		}
		finally {
			Files.deleteIfExists(path1);
			Files.deleteIfExists(path2);
			Files.deleteIfExists(path3);
		}
	}

	@Test
	void testUnion() {
		List<Long> ids = SetAlgorithm.number64().union(1L, 2L, 3L, 3L).stream().read().fetch();
		Assertions.assertEquals("[1, 2, 3]", ids.toString());

		ids = SetAlgorithm.number64().union(1L, 2L, 3L, 3L).union(2L, 4L).stream().read().fetch();
		Assertions.assertEquals("[1, 2, 3, 4]", ids.toString());

		ids = SetAlgorithm.number64().distinct(1L, 2L, 2L).union(3L, 4L).stream().read().fetch();
		Assertions.assertEquals("[1, 2, 3, 4]", ids.toString());

	}

	@Test
	void testFileUnion() throws IOException {
		Path path1 = Paths.get("/tmp-u1.txt");
		Path path2 = Paths.get("/tmp-u2.txt");
		Path path3 = Paths.get("/tmp-u3.txt");
		Set<String> s1 = Sets.newHashSet(path1.toString());
		Set<String> s2 = Sets.newHashSet(path2.toString());
		Set<String> s3 = Sets.newHashSet(path3.toString());
		try {
			this.write(path1.toFile(), 1L, 2L, 3L, 3L, 4L);
			this.write(path2.toFile(), 3L, 3L, 4L);
			this.write(path3.toFile(), 5L, 6L, 7L);

			List<Long> ids = SetAlgorithm.number64().union(s1).union(s2).stream().read().fetch();
			Assertions.assertEquals("[1, 2, 3, 4]", ids.toString());

			ids = SetAlgorithm.number64().distinct(s1).union(s3).stream().read().fetch();
			Assertions.assertEquals("[1, 2, 3, 4, 5, 6, 7]", ids.toString());

			ids = SetAlgorithm.number64().union(Sets.newHashSet(path1.toString(), path2.toString(), path3.toString()))
					.stream().read().fetch();
			Assertions.assertEquals("[1, 2, 3, 4, 5, 6, 7]", ids.toString());
		}
		finally {
			Files.deleteIfExists(path1);
			Files.deleteIfExists(path2);
			Files.deleteIfExists(path3);
		}
	}

	@Test
	void testExclude() {
		List<Long> ids = SetAlgorithm.number64().exclude(1L, 2L, 2L, 3L).stream().read().fetch();
		Assertions.assertEquals("[]", ids.toString());

		ids = SetAlgorithm.number64().distinct(1L, 2L, 3L, 3L).exclude(1L, 2L).stream().read().fetch();
		Assertions.assertEquals("[3]", ids.toString());

		ids = SetAlgorithm.number64().distinct(1L, 2L, 3L).exclude(4L).stream().read().fetch();
		Assertions.assertEquals("[1, 2, 3]", ids.toString());
	}

	@Test
	void randomExtract() {
		List<Long> ids = new ArrayList<>(1000);
		for (long i = 0L; i < 1000L; i++) {
			ids.add(i);
		}
		N64SetAlgoStream stream = SetAlgorithm.number64().distinct(SetAlgoData.of(ids)).stream();
		SetStream<Long> setStream = stream.read();
		Assertions.assertEquals(1000, setStream.size());

		SetStream<Long> randomExtract = stream.randomExtract(100);
		Assertions.assertEquals(100, randomExtract.size());
		Assertions.assertEquals(900, setStream.size());

		stream.randomExtract(800);
		Assertions.assertEquals(100, setStream.size());

		N64SetAlgoStream algoStream = SetAlgorithm.number64().distinct(1L, 2L, 3L).stream();
		SetStream<Long> extractStream = algoStream.randomExtract(3);
		Assertions.assertEquals("[1, 2, 3]", extractStream.fetch().toString());
		Assertions.assertTrue(algoStream.randomExtract(3).fetch().isEmpty());
		Assertions.assertEquals(0, algoStream.read().size());

		algoStream = SetAlgorithm.number64().distinct(1L, 2L, 3L).stream();
		Assertions.assertEquals("[1, 2, 3]", algoStream.randomExtract(5).fetch().toString());

		SetStream<Long> emptyStream = SetAlgorithm.number64().distinct(1L, 2L, 3L).stream().randomExtract(-1);
		Assertions.assertTrue(emptyStream.isEmpty());

	}

	@Test
	void clear() {
		N64SetAlgoStream stream = SetAlgorithm.number64().distinct(1L).stream();
		Assertions.assertEquals(1, stream.read().size());
		stream.clear();
		Assertions.assertEquals(0, stream.read().size());

		stream.distinct(1L).stream();
		Assertions.assertEquals(1, stream.read().size());
		stream.clear();

		stream = SetAlgorithm.number64().distinct(1L, 2L, 3L, 3L).stream();
		SetStream<Long> setStream = stream.read();
		Assertions.assertEquals(3, setStream.size());
		setStream.clear();
		Assertions.assertEquals(0, setStream.size());
	}

	private void write(File file, long... values) throws IOException {
		try (FileOutputStream fos = new FileOutputStream(file);
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos))) {
			for (long val : values) {
				writer.write(String.valueOf(val));
				writer.newLine();
			}
		}
	}

}