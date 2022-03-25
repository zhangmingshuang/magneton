package org.magneton.module.oss.aliyun;

import com.aliyun.oss.OSSClientBuilder;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.magneton.module.oss.Sts;
import org.magneton.module.oss.StsOss;

/**
 * .
 *
 * @author zhangmsh 25/03/2022
 * @since 2.0.7
 */
public class AliyunOss implements StsOss {

	private final AliyunOssProperty aliyunOssProperty;

	private DefaultAcsClient defaultAcsClient;

	public AliyunOss(AliyunOssProperty aliyunOssProperty) {
		this.aliyunOssProperty = aliyunOssProperty;
	}

	protected DefaultAcsClient getClient() {
		new OSSClientBuilder().build(this.aliyunOssProperty.getEndpoint(), , this.aliyunOssProperty.getAccessKey())
		if (this.defaultAcsClient == null) {
			synchronized (AliyunOss.class) {
				if (this.defaultAcsClient == null) {
					// regionId表示RAM的地域ID。以华东1（杭州）地域为例，regionID填写为cn-hangzhou。也可以保留默认值，默认值为空字符串（""）。
					String regionId = this.aliyunOssProperty.getRegionId();
					String endpoint = this.aliyunOssProperty.getEndpoint();
					DefaultProfile.addEndpoint(regionId, "Sts", endpoint);
					// 构造default profile。
					IClientProfile profile = DefaultProfile.getProfile(regionId, this.aliyunOssProperty.getAccessKey(),
							this.aliyunOssProperty.getAccessKeySecret());
					// 构造client。
					this.defaultAcsClient = new DefaultAcsClient(profile);
				}
			}
		}
		return this.defaultAcsClient;
	}

	@Override
	public <T extends Sts> T sts(String bucket) {
		return null;
	}

}
