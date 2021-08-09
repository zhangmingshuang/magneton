package cn.nascent.framework.test.util;

import cn.nascent.framework.test.annotation.TestSort;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.List;

/**
 * .
 *
 * @author zhangmsh 2021/8/3
 * @since 2.0.0
 */
public class SortUtil {

	private SortUtil() {
	}

	@SuppressWarnings("SubtractionInCompareTo")
	@CanIgnoreReturnValue
	public static <T> List<T> sort(List<T> list) {
		list.sort((o1, o2) -> {
			TestSort s1 = AnnotationUtils.findAnnotation(o1.getClass(), TestSort.class);
			TestSort s2 = AnnotationUtils.findAnnotation(o2.getClass(), TestSort.class);
			return (s1 == null ? 0 : s1.value()) - (s2 == null ? 0 : s2.value());
		});
		return list;
	}

}
