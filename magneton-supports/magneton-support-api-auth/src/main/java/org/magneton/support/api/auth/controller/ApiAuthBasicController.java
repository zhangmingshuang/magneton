package org.magneton.support.api.auth.controller;

import java.util.Map;
import javax.validation.Valid;
import org.magneton.core.Response;
import org.magneton.module.safedog.impl.AbstractSignSafeDog;
import org.magneton.support.api.auth.constant.CommonError;
import org.magneton.support.api.auth.pojo.BasicGetSecretKeyReq;
import org.magneton.support.api.auth.properties.ApiAuthProperties;
import org.magneton.support.api.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 基础请求鉴权
 *
 * @apiNote 基础请求鉴权模块用来处理基本的请求数据摘要、密码传输安全管理等相关。 请求鉴权流程为：
 * <ul>
 * <li>1. 调用方生成RSA秘钥，然后使用公钥请求摘要接口（除该接口外，所有的接口都需要有数据签名）</li>
 * <li>2. 调用方获得请求摘要数据之后，使用RSA私钥进行解密，拿到摘要签名串</li>
 * <li>3.
 * 后续所有的请求根据签名规则进行数据摘要生成签名，签名的字段由调用方法与服务端进行确认，因为这个字段是可配置的，默认为sign。{@link ApiAuthProperties#getSignParamName()}</li>
 * <li>4.
 * 此外，启动签名校验之后，额外必须参数也需要与服务端进行确认，否则校验不通过。{@link ApiAuthProperties#getSignInterceptorNeedParams()}</li>
 * </ul>
 *
 * 数据签名规则说明 {@link AbstractSignSafeDog#sign(Map)}
 * <p>
 * <ul>
 * <li>1. 所有的请求参数列表的所有Key进行排序， JAVA采用 {@code Collections.sort(keys)}</li>
 * <li>2. 遍历请求列表所有Key，然后获取对应的Value，如果Value为{@code null}或者空字符串则忽略，否则则进行拼接。
 * 如请求参数列表：{@code k1=v1&k2=v2&k3=}，则拼接字符串为：{@code k1=v1,k2=v2}，因为{@code k3}为空所以不拼接。</li>
 * <li>3. 拼接完成之后的请求串，需要额外添加secretKey，最终字符串为：{@code k1=v1,k2=v2secretKey}</li>
 * <li>4. 注意必填参数，如果必填参数不存在，则直接响应签名错误</li>
 * <li>5. <b>特别注意：最终的签名字段不参与签名。签名的字段由调用方法与服务端进行确认，因为这个字段是可配置的，默认为sign</b></li>
 * </ul>
 * @order 1
 * @author zhangmsh 2022/3/19
 * @since 1.0.0
 */
@RestController
@RequestMapping("/" + ApiAuthProperties.PREFIX + "/auth/basic")
@Validated
public class ApiAuthBasicController {

	@Autowired
	private AuthService authService;

	/**
	 * 获取签名摘要
	 * @apiNote 该接口不会被全局签名机制校验，所以该接口的签名规则是固定的。调用方需要与服务方协调好一个固定的签名密钥，然后通过简单的SHA256进行签名。
	 * 注意：该接口的时间差异有范围控制，需要与服务方进行沟通（即请求到接收的时间差异秒数）。
	 * 并且，返回的密钥信息如果超过一定时间则会过期，此时服务方会响应{@link CommonError#SECRET_KEY_MISS}错误，此时调用方应该重新调用该接口生成新的密钥。
	 *
	 * 该接口响应的为如下格式的JSON串，使用RSA公钥加密后的字符串(Base64)：
	 * <ul>
	 * <li>secretId 秘钥ID</li>
	 * <li>secretKey 秘钥</li>
	 * </ul>
	 * @param basicGetSecretKeyReq 请求
	 * @return 签名摘要
	 */
	@PostMapping("/getSecretKey")
	public Response<String> getSecretKey(@Valid @RequestBody BasicGetSecretKeyReq basicGetSecretKeyReq) {
		return this.authService.createSecretKey(basicGetSecretKeyReq);
	}

}
