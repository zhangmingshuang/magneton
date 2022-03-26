package org.magneton.module.oss;

import javax.annotation.Nullable;

/**
 * .
 *
 * @author zhangmsh 25/03/2022
 * @since 2.0.7
 */
public interface StsOss extends Oss {

	/**
	 * 获取STS授权
	 * @param bucket Bucket
	 * @return 授权信息，如果异常或失败，返回{@code null}
	 */
	@Nullable
	<T extends Sts> T sts(@Nullable String bucket);

}
