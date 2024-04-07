package org.magneton;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangmsh.
 * @since
 */
public class CacheTest {

	static Cache<String, List<String>> cache = CacheBuilder.newBuilder().maximumSize(3)
			.expireAfterWrite(3, TimeUnit.SECONDS).build();

	public static void main(String[] args) throws ExecutionException {
		int size = 30000;

		List<String> list = new ArrayList<>(size);
		for (int i = 0; i < size; i++) {
			list.add(i + "...");
		}

		List<String> cached = cache.get("test-key", () -> {
			System.out.println("load data");
			return list;
		});
		System.out.println(cached.size());

		try {
			Thread.sleep(4000);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		cached = cache.get("test-key", () -> {
			System.out.println("load data2");
			return list;
		});
		System.out.println(cached.size());

		cached = cache.get("test-key", () -> {
			System.out.println("load data3");
			return list;
		});
		System.out.println(cached.size());
	}

}