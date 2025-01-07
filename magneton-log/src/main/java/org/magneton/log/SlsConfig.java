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

package org.magneton.log;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * sls日志配置
 * <p>
 * 参数说明详见：<a href="https://developer.aliyun.com/article/682761">官方参数说明</a>
 * <p>
 * 注意：只有 project、logStore、topic、source、shardHash 这 5
 * 个属性都相同的数据才有机会和并在一起批量发送。为了让数据合并功能充分发挥作用，同时也为了节省内存，建议您控制这 5 个字段的取值范围。如果某个字段如 topic
 * 的取值确实非常多，建议您将其加入 logItem 而不是直接使用 topic。
 *
 * @author zhangmsh.
 * @since 1.0.0
 */
@Setter
@Getter
@ToString
public class SlsConfig {

	/**
	 * sls日志服务的访问地址
	 */
	private String endpoint = "cn-zhangjiakou.log.aliyuncs.com";

	/**
	 * sls日志服务的访问key
	 */
	private String accessKey;

	/**
	 * sls日志服务的访问密钥
	 */
	private String accessSecret;

	/**
	 * 日志配置
	 */
	private Config producer;

	/**
	 * 特定业务的日志配置
	 * <p>
	 * key=业务ID , value=日志配置
	 * @see SlsClient
	 */
	private Map<String, Config> bizProducer;

	@Setter
	@Getter
	@ToString
	public static class Config {

		/**
		 * 待发送数据的目标 project
		 */
		private String project;

		/**
		 * 待发送数据的目标 logStore
		 */
		private String logStore;

		/**
		 * 待发送数据的 topic，如果留空或没有指定，该字段将被赋予""。
		 */
		@Nullable
		private String topic;

		/**
		 * 待发送数据的 source。如果留空或没有指定，该字段将被赋予 producer 所在宿主机的 IP。
		 */
		@Nullable
		private String source;

		/**
		 * 待发送数据的 shardHash，用于将数据写入 logStore 中的特定 shard。
		 * <p>
		 * 如果留空或没有指定，数据将被随机写入目标 logStore 的某个 shard 中。
		 */
		@Nullable
		private String shardHash;

	}

}
