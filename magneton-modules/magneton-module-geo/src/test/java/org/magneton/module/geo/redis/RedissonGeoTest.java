package org.magneton.module.geo.redis;

import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.magneton.foundation.comparator.VersionComparator;
import org.magneton.module.geo.Geo;
import org.magneton.module.geo.GeoEntry;
import org.magneton.module.geo.GeoPosition;
import org.magneton.module.geo.query.PositionArgs;
import org.redisson.api.redisnode.RedisNode;
import org.redisson.api.redisnode.RedisNodes;

/**
 * .
 *
 * @author zhangmsh 2022/2/11
 * @since 1.2.0
 */
@SuppressWarnings("unchecked")
class RedissonGeoTest extends TestRedisson {

	private final Geo geo = new RedissonGeo(redissonClient);

	@Test
	void add() {
		String no1 = "xiamen software park no.1";
		long add = this.geo.add("add", 118.188087, 24.494134, no1);
		Assertions.assertEquals(1, add);

		String no2 = "xiamen software park no.2";
		long add2 = this.geo.add("add", GeoEntry.of(118.185701, 24.491918, no2));
		Assertions.assertEquals(1, add2);

		long add3 = this.geo.add("add", GeoEntry.of(118.18699, 24.491196, "no3"),
				GeoEntry.of(118.185701, 24.491918, no2));
		Assertions.assertEquals(1, add3);
	}

	@Test
	void addIfAbsent() {
		if (this.isIgnore()) {
			System.out.println("version less . ignored.");
			return;
		}
		String no1 = "xiamen software park no.1";
		long add = this.geo.addIfAbsent("addIfAbsent", 118.188087, 24.494134, no1);
		Assertions.assertEquals(1, add);

		String no2 = "xiamen software park no.2";
		long add2 = this.geo.addIfAbsent("addIfAbsent", GeoEntry.of(118.185701, 24.491918, no2));
		Assertions.assertEquals(1, add2);

		long add3 = this.geo.addIfAbsent("addIfAbsent", GeoEntry.of(118.18699, 24.491196, "no3"),
				GeoEntry.of(118.185701, 24.491918, no2));
		Assertions.assertEquals(1, add3);

	}

	@Test
	void addIfExists() {
		if (this.isIgnore()) {
			System.out.println("version less . ignored.");
			return;
		}
		String no1 = "xiamen software park no.1";
		long add = this.geo.add("addIfExists", 118.188087, 24.494134, no1);
		Assertions.assertEquals(1, add);

		long add1 = this.geo.addIfExists("addIfExists", 118.188087, 24.494134, "test");
		Assertions.assertEquals(1, add1);

		long add2 = this.geo.addIfExists("addIfExists-nil", 1, 1, "nil");
		Assertions.assertEquals(0, add2);
	}

	private boolean isIgnore() {
		String version = redissonClient.getRedisNodes(RedisNodes.SINGLE).getInstance()
				.info(RedisNode.InfoSection.SERVER).get("redis_version");
		int compare = VersionComparator.getInstance().compare(version, "6.2.0");
		return compare < 0;
	}

	@Test
	void dist() {
		String no1 = "xiamen software park no.1";
		long add = this.geo.add("dist", 118.188087, 24.494134, no1);
		Assertions.assertEquals(1, add);

		String no2 = "xiamen software park no.2";
		long add2 = this.geo.add("dist", GeoEntry.of(118.185701, 24.491918, no2));
		Assertions.assertEquals(1, add2);

		Double dist = this.geo.dist("dist", no1, no2);
		Assertions.assertTrue(dist != null && dist > 0D);
	}

	@Test
	void pos() {
		String no1 = "xiamen software park no.1";
		long add = this.geo.add("pos", 118.188087, 24.494134, no1);
		Assertions.assertEquals(1, add);

		Map<String, GeoPosition> pos = this.geo.pos("pos", no1);
		Assertions.assertEquals(1, pos.size());
		Set<Map.Entry<String, GeoPosition>> entries = pos.entrySet();
		for (Map.Entry<String, GeoPosition> entry : entries) {
			Assertions.assertEquals(no1, entry.getKey());
		}
	}

	@Test
	void radiusWithDistance() {
		String no1 = "xiamen software park no.1";
		long add = this.geo.add("radiusWithDistance", 118.188087, 24.494134, no1);
		Assertions.assertEquals(1, add);

		String no2 = "xiamen software park no.2";
		long add2 = this.geo.add("radiusWithDistance", GeoEntry.of(118.185701, 24.491918, no2));
		Assertions.assertEquals(1, add2);

		PositionArgs args = new PositionArgs().setName("radiusWithDistance").setPosition(118.188087, 24.494134)
				.setRadius(1000);

		Map<Integer, Double> radiusWithDistance = this.geo.radiusWithDistance(args);
		Assertions.assertFalse(radiusWithDistance.isEmpty());
	}

	@Test
	void radiusWithPosition() {
		String no1 = "xiamen software park no.1";
		long add = this.geo.add("radiusWithPosition", 118.188087, 24.494134, no1);
		Assertions.assertEquals(1, add);

		String no2 = "xiamen software park no.2";
		long add2 = this.geo.add("radiusWithPosition", GeoEntry.of(118.185701, 24.491918, no2));
		Assertions.assertEquals(1, add2);

		PositionArgs args = new PositionArgs().setName("radiusWithPosition").setPosition(118.188087, 24.494134)
				.setRadius(1000);
		Map<Object, GeoPosition> radiusWithPosition = this.geo.radiusWithPosition(args);
		System.out.println(radiusWithPosition);
		Assertions.assertFalse(radiusWithPosition.isEmpty());
	}

}