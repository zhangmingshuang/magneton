package org.magneton.module.oss.aliyun;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.auth.sts.AssumeRoleResponse.Credentials;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import javax.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.magneton.core.base.Strings;
import org.magneton.core.base.Verify;
import org.magneton.module.oss.Sts;
import org.magneton.module.oss.StsOss;

/**
 * .
 *
 * @author zhangmsh 25/03/2022
 * @since 2.0.7
 */
@Slf4j
public class AliyunOss implements StsOss {

	private final AliyunOssProperty aliyunOssProperty;

	private DefaultAcsClient defaultAcsClient;

	public AliyunOss(AliyunOssProperty aliyunOssProperty) {
		this.aliyunOssProperty = aliyunOssProperty;
	}

	protected DefaultAcsClient getClient() {
		if (this.defaultAcsClient == null) {
			synchronized (AliyunOss.class) {
				if (this.defaultAcsClient == null) {
					String endpoint = this.aliyunOssProperty.getEndpoint();
					DefaultProfile.addEndpoint("", "Sts", endpoint);
					// 构造default profile。
					IClientProfile profile = DefaultProfile.getProfile("", this.aliyunOssProperty.getAccessKey(),
							this.aliyunOssProperty.getAccessKeySecret());
					// 构造client。
					this.defaultAcsClient = new DefaultAcsClient(profile);
				}
			}
		}
		return this.defaultAcsClient;
	}

	@Override
	public <T extends Sts> T sts(@Nullable String bucket) {
		AssumeRoleRequest request = new AssumeRoleRequest();
		request.setSysMethod(MethodType.POST);
		request.setRoleArn(this.aliyunOssProperty.getRoleArn());
		request.setRoleSessionName(this.aliyunOssProperty.getRoleSessionName());
		request.setSysRegionId(this.aliyunOssProperty.getRegionId());
		if (Strings.isNullOrEmpty(bucket)) {
			bucket = this.aliyunOssProperty.getDefaultBucket();
		}
		Verify.verify(!Strings.isNullOrEmpty(bucket), "bucket must not be null");
		//@formatter:off
		String policy = "{\n" +
			"    \"Version\": \"1\", \n" +
			"    \"Statement\": [\n" +
			"        {\n" +
			"            \"Action\": [\n" +
			"                \"oss:PutObject\"\n" +
			"            ], \n" +
			"            \"Resource\": [\n" +
			"                \"acs:oss:*:*:" + bucket + "/*\" \n" +
			"            ], \n" +
			"            \"Effect\": \"Allow\"\n" +
			"        }\n" +
			"    ]\n" +
			"}";
		//@formatter:on
		request.setPolicy(policy); // 如果policy为空，则用户将获得该角色下所有权限。
		request.setDurationSeconds(this.aliyunOssProperty.getStsDurationSeconds()); // 设置临时访问凭证的有效时间为3600秒。
		try {
			AssumeRoleResponse response = this.doAssumeRequest(request);
			Credentials credentials = response.getCredentials();

			AliyunStsRes res = new AliyunStsRes();
			res.setAccessKeyId(credentials.getAccessKeyId());
			res.setAccessKeySecret(credentials.getAccessKeySecret());
			res.setSecurityToken(credentials.getSecurityToken());
			res.setBucket(bucket);
			res.setEndpoint(this.aliyunOssProperty.getEndpoint());
			LocalDateTime parse = LocalDateTime.parse(credentials.getExpiration(), DateTimeFormatter.ISO_DATE_TIME);
			long expireTime = parse.toInstant(ZoneOffset.UTC).toEpochMilli();
			res.setExpireTime(expireTime);
			return (T) res;
		}
		catch (ClientException e) {
			log.error(String.format("aliyun oss sts assume error. %s", bucket), e);
		}
		return null;
	}

	protected AssumeRoleResponse doAssumeRequest(AssumeRoleRequest request) throws ClientException {
		return this.getClient().getAcsResponse(request);
	}

}
