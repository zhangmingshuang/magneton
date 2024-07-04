package org.magneton.enhance.sms;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.magneton.enhance.sms.process.aliyun.AliyunSmsProperty;
import org.magneton.enhance.sms.property.SmsProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
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
@ConditionalOnClass(SmsProperty.class)
@ConfigurationProperties(prefix = "magneton.module.sms")
public class SmsProperties extends SmsProperty {

	/**
	 * aliyun sms property.
	 */
	@NestedConfigurationProperty
	private AliyunSmsProperty aliyun = new AliyunSmsProperty();

}
