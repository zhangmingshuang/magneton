package org.magneton.enhance.safedog.sign;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * 签名值生成器.
 *
 * @author zhangmsh.
 * @since 2023.9
 */
public interface SignGenerator {

	/**
	 * 生成签名值.
	 * @param data 要生成签名值的数据
	 * @param keys 要生成签名值的数据对应的Key。该Key的主要作用是用来根据对应的值顺序获取有序的待签名数据。 如果为 {@code null}, 则会使用
	 * {@code data} 的默认所有Key。
	 * @param salt 盐
	 * @return 签名值
	 */
	String generate(Map<String, String> data, @Nullable List<String> keys, @Nullable String salt);

	/**
	 * 获取签名字符串.
	 * @param data 要生成签名值的数据
	 * @param keys 要生成签名值的数据对应的Key。该Key的主要作用是用来根据对应的值顺序获取有序的待签名数据。 如果为 {@code null}, 则会使用
	 * {@code data} 的默认所有Key。
	 * @param salt 盐
	 * @return 签名字符串
	 */
	String getSignString(Map<String, String> data, @Nullable List<String> keys, @Nullable String salt);

}