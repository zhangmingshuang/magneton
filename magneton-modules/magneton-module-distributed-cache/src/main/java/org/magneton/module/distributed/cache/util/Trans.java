package org.magneton.module.distributed.cache.util;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.magneton.core.collect.Collections;
import org.magneton.core.collect.Lists;

/**
 * .
 *
 * @author zhangmsh 2021/6/24
 * @since 1.0.0
 */
public class Trans {

	private Trans() {
		// private
	}

	public static String toStr(byte[] response) {
		return new String(response, StandardCharsets.UTF_8);
	}

	public static byte[] toByte(String str) {
		return str.getBytes(StandardCharsets.UTF_8);
	}

	public static byte[][] toByteArray(String... strs) {
		byte[][] bytes = new byte[strs.length][];
		for (int i = 0, l = strs.length; i < l; i++) {
			bytes[i] = strs[i].getBytes(StandardCharsets.UTF_8);
		}
		return bytes;
	}

	public static byte[][] toByteArray(List<String> strs) {
		byte[][] bytes = new byte[strs.size()][];
		for (int i = 0, l = strs.size(); i < l; i++) {
			bytes[i] = strs.get(i).getBytes(StandardCharsets.UTF_8);
		}
		return bytes;
	}

	public static List<String> toStr(List<byte[]> bytes) {
		List<String> list = new ArrayList<>(bytes.size());
		for (byte[] bs : bytes) {
			list.add(new String(bs, StandardCharsets.UTF_8));
		}
		return list;
	}

	public static Map<byte[], byte[]> toByteMap(Map<String, String> values) {
		Map<byte[], byte[]> byteMap = new HashMap<>((int) (values.size() * 1.4));
		values.forEach((k, v) -> byteMap.put(k.getBytes(StandardCharsets.UTF_8), v.getBytes(StandardCharsets.UTF_8)));
		return byteMap;
	}

	public static Set<String> toStrSet(Set<byte[]> keys) {
		Set<String> strSet = new HashSet<>((int) (keys.size() * 1.4));
		keys.forEach(b -> strSet.add(new String(b, StandardCharsets.UTF_8)));
		return strSet;
	}

	public static List<String> toStrList(List<byte[]> values) {
		List<String> strList = new ArrayList<>(values.size());
		values.forEach(b -> strList.add(new String(b, StandardCharsets.UTF_8)));
		return strList;
	}

	public static Map<String, String> toStrMap(Map<byte[], byte[]> all) {
		Map<String, String> map = new HashMap<>((int) (all.size() * 1.4));
		all.forEach((k, v) -> map.put(new String(k, StandardCharsets.UTF_8), new String(v, StandardCharsets.UTF_8)));
		return map;
	}

	public static List<Boolean> toBoolean(Collection<Object> objects) {
		if (Collections.isNullOrEmpty(objects)) {
			return Collections.emptyList();
		}
		List<Boolean> list = Lists.newArrayListWithCapacity(objects.size());
		for (Object object : objects) {
			if (object instanceof Boolean) {
				list.add((Boolean) object);
			}
			else {
				list.add(Boolean.FALSE);
			}
		}
		return list;
	}

}
