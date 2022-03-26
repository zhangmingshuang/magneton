package org.magneton.module.oss.aliyun;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import org.junit.jupiter.api.Test;
import org.magneton.module.oss.Sts;

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
		final AssumeRoleResponse response = client.getAcsResponse(request);
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
		System.out.println(sdf.format(date));
	}

	@Test
	void testSts() {
		AliyunOssProperty property = new AliyunOssProperty();
		AliyunOss aliyunOss = new MockAliyunOss(property);
		Sts test = aliyunOss.sts("test");
		System.out.println(test);
	}

}