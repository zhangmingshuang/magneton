package org.magneton.enhance.sms.process.aliyun;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * .
 *
 * @author zhangmsh 18/03/2022
 * @since 2.0.7
 */
@Setter
@Getter
@ToString
public class AliyunSms {

	/**
	 * 验证码
	 */
	private final String code;

	/**
	 * 附加属性
	 */
	@Nullable
	private Map<String, String> addition;

	public AliyunSms(String code) {
		this.code = Preconditions.checkNotNull(code, "code must not be null");
	}

}
