package org.magneton.support.api.auth.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.magneton.module.sms.process.aliyun.AliyunSmsProperty;
import org.magneton.module.sms.property.SmsProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * .
 *
 * @author zhangmsh 18/03/2022
 * @since 2.0.7
 */
@Setter
@Getter
@ToString
@ConfigurationProperties(prefix = "magneton.module.sms")
public class SmsModuleProperties extends SmsProperty {

	/**
	 * aliyun sms property.
	 */
	@NestedConfigurationProperty
	private AliyunSmsProperty aliyun = new AliyunSmsProperty();

}
