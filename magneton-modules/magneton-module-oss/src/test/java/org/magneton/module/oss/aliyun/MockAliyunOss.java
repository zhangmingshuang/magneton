package org.magneton.module.oss.aliyun;

import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.auth.sts.AssumeRoleResponse.Credentials;
import com.aliyuncs.exceptions.ClientException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author zhangmsh 2022/3/26
 * @since 1.0.0
 */
public class MockAliyunOss extends AliyunOss {

	public MockAliyunOss(AliyunOssProperty aliyunOssProperty) {
		super(aliyunOssProperty);
	}

	@Override
	protected AssumeRoleResponse doAssumeRequest(AssumeRoleRequest request) throws ClientException {
		AssumeRoleResponse response = new AssumeRoleResponse();

		Credentials credentials = new Credentials();
		credentials.setSecurityToken("test-security-token");
		credentials.setAccessKeySecret("test-access-key-secret");
		credentials.setAccessKeyId("test-access-key-id");

		// LocalDateTime parse = LocalDateTime.of(credentials.getExpiration(),
		// DateTimeFormatter.ISO_DATE_TIME);

		credentials.setExpiration(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));

		response.setRequestId("test");
		response.setCredentials(credentials);
		response.setAssumedRoleUser(null);
		return response;
	}

}
