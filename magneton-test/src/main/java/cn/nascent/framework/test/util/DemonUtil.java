package cn.nascent.framework.test.util;

import cn.nascent.framework.test.injector.InjectType;
import java.util.concurrent.ThreadLocalRandom;

/**
 * .
 *
 * @author zhangmsh 2021/8/3
 * @since
 */
public class DemonUtil {

	private DemonUtil() {
	}

	public static int createInt(InjectType injectType, int value) {
		if (!injectType.isDemon()) {
			return value;
		}
		int demon = ThreadLocalRandom.current().nextInt();
		return (demon + value) / ThreadLocalRandom.current().nextInt(1, 10);
	}

}
