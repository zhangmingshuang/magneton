package org.magneton.foundation.comparator;

import cn.hutool.core.text.CharPool;
import cn.hutool.core.text.CharSequenceUtil;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

/**
 * 版本比较器<br>
 * 比较两个版本的大小<br>
 * 排序时版本从小到大排序，即比较时小版本在前，大版本在后<br>
 * 支持如：1.3.20.8，6.82.20160101，8.5a/8.5c等版本形式<br>
 * 参考：<a href="https://www.cnblogs.com/shihaiming/p/6286575.html">Java实现比较版本号</a>
 *
 * @author Looly
 * @since 4.0.2
 */
public class VersionComparator implements Comparator<String>, Serializable {

	private static final long serialVersionUID = 8083701245147495562L;

	/** 单例 */
	private static final VersionComparator INSTANCE = new VersionComparator();

	public static VersionComparator getInstance() {
		return INSTANCE;
	}

	private VersionComparator() {
		// private.
	}

	/**
	 * 比较两个版本<br>
	 * null版本排在最小：即： <pre>
	 * compare(null, "v1") &lt; 0
	 * compare("v1", "v1")  = 0
	 * compare(null, null)   = 0
	 * compare("v1", null) &gt; 0
	 * compare("1.0.0", "1.0.2") &lt; 0
	 * compare("1.0.2", "1.0.2a") &lt; 0
	 * compare("1.13.0", "1.12.1c") &gt; 0
	 * compare("V0.0.20170102", "V0.0.20170101") &gt; 0
	 * </pre>
	 * @param version1 版本1
	 * @param version2 版本2
	 * @return 比较结果，前者大于后者返回正数，后者大于前者返回负数，相等返回0
	 */
	@Override
	public int compare(String version1, String version2) {
		if (Objects.equal(version1, version2)) {
			return 0;
		}
		if (version1 == null) {
			// null视为最小版本，排在前
			return -1;
		}
		else if (version2 == null) {
			return 1;
		}

		List<String> v1s = Lists.newArrayList(CharSequenceUtil.split(version1, CharPool.DOT));
		List<String> v2s = Lists.newArrayList(CharSequenceUtil.split(version2, CharPool.DOT));

		int diff = 0;
		int minLength = Math.min(v1s.size(), v2s.size());
		String v1;
		String v2;
		for (int i = 0; i < minLength; i++) {
			v1 = v1s.get(i);
			v2 = v2s.get(i);
			// 先比较长度
			diff = v1.length() - v2.length();
			if (0 == diff) {
				diff = v1.compareTo(v2);
			}
			if (diff != 0) {
				// 已有结果，结束
				break;
			}
		}

		// 如果已经分出大小，则直接返回，如果未分出大小，则再比较位数，有子版本的为大；
		return (diff != 0) ? diff : v1s.size() - v2s.size();
	}

}
