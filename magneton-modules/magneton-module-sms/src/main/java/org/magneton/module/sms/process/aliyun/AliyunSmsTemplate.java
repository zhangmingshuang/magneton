package org.magneton.module.sms.process.aliyun;

import com.google.common.base.Preconditions;
import java.util.Map;
import javax.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * .
 *
 * @author zhangmsh 18/03/2022
 * @since 2.0.7
 */
@Setter
@Getter
@ToString
public class AliyunSmsTemplate {

	/**
	 * 验证码
	 */
	private final String code;

	/**
	 * 附加属性
	 */
	@Nullable
	private Map<String, String> addition;

	public AliyunSmsTemplate(String code) {
		this.code = Preconditions.checkNotNull(code);
	}

}
