package org.magneton.module.oss.aliyun;

import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author zhangmsh 2022/3/25
 * @since 1.0.0
 */
class AliyunOssTest {

	@Test
	void testTime() {
		String time = "2022-03-25T16:40:03Z";
		LocalDateTime parse = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME);
		long second = parse.toInstant(ZoneOffset.UTC).toEpochMilli();
		System.out.println(second);
		Date date = new Date(second);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Assertions.assertDoesNotThrow(() -> sdf.format(date));
	}

	@Test
	void sts() {
		AliyunOssProperty property = new AliyunOssProperty();
		AliyunOss aliyunOss = new MockAliyunOss(property);
		AliyunStsRes test = aliyunOss.sts("lookersci");
		Assertions.assertNotNull(test);
		System.out.println(test);
	}

	@Test
	void putObject() {
		AliyunOssProperty property = new AliyunOssProperty();
		property.setDefaultBucket("defaultBucket");
		AliyunOss aliyunOss = new MockAliyunOss(property);
		AliyunStsRes sts = aliyunOss.sts(null);

		System.out.println("======== sts ==========");
		System.out.println(sts);
		System.out.println("======== sts ==========");

		Assertions.assertThrows(Exception.class,
				() -> aliyunOss.simpleUpdate(sts, "/abc/aaa/test.png", Paths.get("D://1.png").toFile()));
	}

}