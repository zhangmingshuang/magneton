package org.magneton.support.api.auth.properties;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhangmsh 2022/3/19
 * @since 1.0.0
 */
@Setter
@Getter
@ToString
@ConfigurationProperties(prefix = "magneton.api-auth")
public class ApiAuthProperties {

	public static final String PREFIX = "${magneton.api-auth.prefix:api}";

	/**
	 * 模块的请求前缀
	 */
	private String prefix = "api";

	/**
	 * 是否启用签名
	 */
	private boolean sign = true;

	/**
	 * 签名的作用周期，即在这个周期内，同一个签名不可以重复请求. 如果小于1则表示不限制周期。
	 */
	private int signPeriodSeconds = 5 * 60;

	/**
	 * 鉴权模块要拦截的地址，支持表达式
	 */
	private Set<String> authInterceptorPathPatterns;

	/**
	 * 鉴权模块要忽略拦截的地址，支持表达式
	 */
	private Set<String> authInterceptorExcludePathPatterns;

	/**
	 * 签名模块要拦截的地址，支持表达式
	 */
	private Set<String> signInterceptorPathPatterns;

	/**
	 * 签名模块要忽略拦截的地址，支持表达式
	 */
	private Set<String> signInterceptorExcludePathPatterns;

	/**
	 * 签名参数名称
	 */
	private String signParamName = "sign";

	/**
	 * 时间参数名称
	 */
	private String nonceParamName = "nonce";

	/**
	 * 密钥ID参数名称
	 */
	private String secretKeyIdParamName = "secretKeyId";

	/**
	 * Nonce时间与实际时间可相关的秒数
	 */
	private long signInterceptorNonceGapLimitSeconds = 5 * 60;

	/**
	 * 签名模块必须要携带的请求参数
	 */
	private String[] signInterceptorNeedParams;

	/**
	 * 获取签名摘要接口的接口签名固定密钥
	 */
	private String secretKeySignSalt = "123456";

	/**
	 * 启用安全秘钥交互的Debug模式，此时忽略时间与签名校验。
	 */
	private boolean secretKeyDebug = false;

	/**
	 * 安全秘钥交互的Debug模式,返回的固定SecretKeyId
	 */
	private String debugSecretKeyId = "123456";

	/**
	 * 安全秘钥交互的Debug模式，返回的固定SecretKey
	 */
	private String debugSecretKey = "123456";

	/**
	 * 获取签名摘要接口的接口时间间隔限制，默认5分钟
	 */
	private long secretKeyTimeGapLimit = 5 * 60;

}
