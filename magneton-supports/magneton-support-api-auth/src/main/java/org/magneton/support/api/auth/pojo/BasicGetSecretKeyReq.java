package org.magneton.support.api.auth.pojo;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zhangmsh 2022/3/20
 * @since 1.0.0
 */
@Setter
@Getter
@ToString
public class BasicGetSecretKeyReq {

	/**
	 * 客户端生成的RSA公钥, Base64的串
	 */
	@Size(min = 1, max = 2048, message = "RSA公钥长度必须在1~2048位")
	@NotNull(message = "RSA公钥不能为空")
	private String rsaPublicKey;

	/**
	 * 当前的时间
	 */
	@Min(1)
	private long nonce;

	/**
	 * 请求公钥时的签名，注意与全局请求签名的区别
	 */
	@NotNull(message = "签名不能为空")
	private String sign;

}
