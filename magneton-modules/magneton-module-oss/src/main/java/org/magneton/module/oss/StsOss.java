package org.magneton.module.oss;

/**
 * .
 *
 * @author zhangmsh 25/03/2022
 * @since 2.0.7
 */
public interface StsOss extends Oss {

	<T extends Sts> T sts(String bucket);

}
