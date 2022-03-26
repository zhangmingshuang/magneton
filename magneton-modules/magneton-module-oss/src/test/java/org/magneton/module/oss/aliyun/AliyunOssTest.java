package org.magneton.module.oss.aliyun;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
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
	void test() throws ClientException {
		AliyunOssProperty property = new AliyunOssProperty();
		AliyunOss aliyunOss = new MockAliyunOss(property);
		DefaultAcsClient client = aliyunOss.getClient();
		AssumeRoleRequest request = new AssumeRoleRequest();
		request.setSysMethod(MethodType.POST);
		request.setRoleArn(property.getRoleArn());
		request.setRoleSessionName(property.getRoleSessionName());
		request.setSysRegionId(property.getRegionId());
		//@formatter:off
        String policy = "{\n" +
            "    \"Version\": \"1\", \n" +
            "    \"Statement\": [\n" +
            "        {\n" +
            "            \"Action\": [\n" +
            "                \"oss:PutObject\"\n" +
            "            ], \n" +
            "            \"Resource\": [\n" +
            "                \"acs:oss:*:*:lookersci/*\" \n" +
            "            ], \n" +
            "            \"Effect\": \"Allow\"\n" +
            "        }\n" +
            "    ]\n" +
            "}";
        //@formatter:on
		request.setPolicy(policy); // 如果policy为空，则用户将获得该角色下所有权限。
		request.setDurationSeconds(3600L); // 设置临时访问凭证的有效时间为3600秒。
		AssumeRoleResponse response = client.getAcsResponse(request);
		Assertions.assertNotNull(response.getCredentials());

		System.out.println("Expiration: " + response.getCredentials().getExpiration());
		System.out.println("Access Key Id: " + response.getCredentials().getAccessKeyId());
		System.out.println("Access Key Secret: " + response.getCredentials().getAccessKeySecret());
		System.out.println("Security Token: " + response.getCredentials().getSecurityToken());
		System.out.println("RequestId: " + response.getRequestId());
	}

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
		property.setAccessKey("LTAI5t7pQ6X5ikNdG28J7NjS");
		property.setAccessKeySecret("owUAxuLKuYzuArT7QTYCImAtyasaoz");
		property.setEndpoint("oss-cn-hangzhou.aliyuncs.com");
		property.setRoleArn("acs:ram::1199947484795824:role/ramossrole");
		property.setRoleSessionName("test");
		property.setRegionId("cn-hangzhou");
		property.setDefaultBucket("lookersci");
		AliyunOss aliyunOss = new AliyunOss(property);
		AliyunStsRes sts = aliyunOss.sts(null);

		System.out.println("======== sts ==========");
		System.out.println(sts);
		System.out.println("======== sts ==========");

		aliyunOss.simpleUpdate(sts, "/abc/aaa/test.png", Paths.get("D://1.png").toFile());
	}

}