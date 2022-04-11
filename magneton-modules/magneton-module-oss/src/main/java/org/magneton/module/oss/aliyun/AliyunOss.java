package org.magneton.module.oss.aliyun;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.auth.sts.AssumeRoleResponse.Credentials;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutionException;
import javax.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.magneton.core.base.Preconditions;
import org.magneton.core.base.Strings;
import org.magneton.core.base.Verify;
import org.magneton.core.cache.Cache;
import org.magneton.core.cache.CacheBuilder;
import org.magneton.module.oss.StsOss;

/**
 * .
 *
 * @author zhangmsh 25/03/2022
 * @since 2.0.7
 */
@Slf4j
@SuppressWarnings("SynchronizeOnThis")
public class AliyunOss implements StsOss<AliyunOssSts> {

	private static final AliyunOssSts NIL_STS_RES = new AliyunOssSts();

	private final AliyunOssConfig aliyunOssConfig;

	private DefaultAcsClient defaultAcsClient;

	private Cache<String, AliyunOssSts> stsCache;

	public AliyunOss(AliyunOssConfig aliyunOssConfig) {
		this.aliyunOssConfig = aliyunOssConfig;
		this.stsCache = CacheBuilder.newBuilder()
				.expireAfterWrite(Duration.ofSeconds(aliyunOssConfig.getStsDurationSeconds())).maximumSize(1024)
				.build();
	}

	protected DefaultAcsClient getClient() {
		if (this.defaultAcsClient == null) {
			synchronized (AliyunOss.class) {
				if (this.defaultAcsClient == null) {
					String endpoint = this.aliyunOssConfig.getEndpoint();
					DefaultProfile.addEndpoint("", "Sts", endpoint);
					// 构造default profile。
					IClientProfile profile = DefaultProfile.getProfile("", this.aliyunOssConfig.getAccessKey(),
							this.aliyunOssConfig.getAccessKeySecret());
					// 构造client。
					this.defaultAcsClient = new DefaultAcsClient(profile);
				}
			}
		}
		return this.defaultAcsClient;
	}

	/**
	 * 阿里云STS授权
	 *
	 * 详见：{@code https://help.aliyun.com/document_detail/100624.html}
	 * @param bucket Bucket
	 * @return
	 */
	@Override
	@Nullable
	public AliyunOssSts sts(@Nullable String bucket) {
		String bucketName = this.getBucket(bucket);
		AliyunOssSts sts;
		try {
			sts = this.stsCache.get(bucketName, () -> this.doStsRequest(bucketName));
		}
		catch (ExecutionException e) {
			log.error("sts request error", e);
			sts = this.doStsRequest(bucketName);
		}
		if (sts == null || sts.getExpireTime() < System.currentTimeMillis()) {
			// 已过期
			return null;
		}
		return sts;
	}

	/**
	 * 简单上传。
	 *
	 * 错误说明：The specified object is not valid. 表示文件名称以/开头。
	 * @param sts
	 * @param fileName
	 * @param file
	 * @param bucket
	 */
	@Override
	public void simpleUpdate(AliyunOssSts sts, String fileName, File file, @Nullable String bucket) {
		Preconditions.checkNotNull(sts);
		Preconditions.checkNotNull(fileName);
		Preconditions.checkNotNull(file);
		bucket = this.getBucket(bucket);
		OSS oss = this.createOssClient(sts);
		if (fileName.charAt(0) == '/') {
			fileName = fileName.substring(1);
		}
		PutObjectRequest request = new PutObjectRequest(bucket, fileName, file);
		oss.putObject(request);
	}

	@Override
	public String getDomain(@Nullable String bucket) {
		bucket = this.getBucket(bucket);
		String endpoint = this.aliyunOssConfig.getEndpoint();
		return "https://" + bucket + "." + endpoint;
	}

	protected AssumeRoleResponse doAssumeRequest(AssumeRoleRequest request) throws ClientException {
		return this.getClient().getAcsResponse(request);
	}

	protected OSS createOssClient(AliyunOssSts aliyunOssSts) {
		return new OSSClientBuilder().build(aliyunOssSts.getEndpoint(), aliyunOssSts.getAccessKeyId(),
				aliyunOssSts.getAccessKeySecret(), aliyunOssSts.getSecurityToken());
	}

	@Nullable
	protected AliyunOssSts doStsRequest(String bucket) {
		bucket = this.getBucket(bucket);
		AssumeRoleRequest request = new AssumeRoleRequest();
		request.setSysMethod(MethodType.POST);
		request.setRoleArn(this.aliyunOssConfig.getRoleArn());
		request.setRoleSessionName(this.aliyunOssConfig.getRoleSessionName());
		request.setSysRegionId(this.aliyunOssConfig.getRegionId());
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
		request.setDurationSeconds(this.aliyunOssConfig.getStsDurationSeconds()); // 设置临时访问凭证的有效时间为3600秒。
		try {
			AssumeRoleResponse response = this.doAssumeRequest(request);
			Credentials credentials = response.getCredentials();

			AliyunOssSts res = new AliyunOssSts();
			res.setAccessKeyId(credentials.getAccessKeyId());
			res.setAccessKeySecret(credentials.getAccessKeySecret());
			res.setSecurityToken(credentials.getSecurityToken());
			res.setBucket(bucket);
			res.setEndpoint(this.aliyunOssConfig.getEndpoint());
			LocalDateTime parse = LocalDateTime.parse(credentials.getExpiration(), DateTimeFormatter.ISO_DATE_TIME);
			long expireTime = parse.toInstant(ZoneOffset.UTC).toEpochMilli();
			res.setExpireTime(expireTime);
			return res;
		}
		catch (ClientException e) {
			log.error(String.format("aliyun oss sts assume error. %s", bucket), e);
		}
		return null;
	}

	protected String getBucket(@Nullable String bucket) {
		if (Strings.isNullOrEmpty(bucket)) {
			bucket = this.aliyunOssConfig.getDefaultBucket();
		}
		Verify.verify(!Strings.isNullOrEmpty(bucket), "bucket must not be null");
		return bucket;
	}

}
