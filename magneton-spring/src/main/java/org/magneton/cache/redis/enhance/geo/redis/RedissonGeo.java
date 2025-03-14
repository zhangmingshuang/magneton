package org.magneton.cache.redis.enhance.geo.redis;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.magneton.cache.redis.enhance.geo.Geo;
import org.magneton.cache.redis.enhance.geo.GeoEntry;
import org.magneton.cache.redis.enhance.geo.GeoPosition;
import org.magneton.cache.redis.enhance.geo.GeoUnit;
import org.magneton.cache.redis.enhance.geo.query.MemberArgs;
import org.magneton.cache.redis.enhance.geo.query.PositionArgs;
import org.redisson.api.RGeo;
import org.redisson.api.RedissonClient;
import org.redisson.api.geo.GeoSearchArgs;

import javax.annotation.Nullable;
import javax.annotation.Versielimiet;
import java.util.Collections;
import java.util.Map;

/**
 * .
 *
 * @author zhangmsh 2022/2/11
 * @since 1.2.0
 */
public class RedissonGeo implements Geo {

	private final RedissonClient redissonClient;

	public RedissonGeo(RedissonClient redissonClient) {
		this.redissonClient = redissonClient;
	}

	@SafeVarargs
	@Override
	public final <V> long add(String name, GeoEntry<V>... entries) {
		Preconditions.checkNotNull(name);
		org.redisson.api.GeoEntry[] geoEntries = this.entryTransform(entries);
		return this.redissonClient.getGeo(name).add(geoEntries);
	}

	@Override
	public <V> void remove(String name, V member) {
		Preconditions.checkNotNull(name);
		Preconditions.checkNotNull(member);
		this.redissonClient.getGeo(name).remove(member);
	}

	@Override
	@Versielimiet(component = "redis", version = "6.2.0")
	public <V> long addIfAbsent(String name, GeoEntry<V>... entries) {
		Preconditions.checkNotNull(name);
		org.redisson.api.GeoEntry[] geoEntries = this.entryTransform(entries);
		return this.redissonClient.getGeo(name).tryAdd(geoEntries);
	}

	@Override
	@Versielimiet(component = "redis", version = "6.2.0")
	public <V> long addIfExists(String name, GeoEntry<V>... entries) {
		Preconditions.checkNotNull(name);
		org.redisson.api.GeoEntry[] geoEntries = this.entryTransform(entries);
		return this.redissonClient.getGeo(name).addIfExists(geoEntries);
	}

	@Override
	public <V> Double dist(String name, V member1, V member2, @Nullable GeoUnit unit) {
		Preconditions.checkNotNull(name);
		org.redisson.api.GeoUnit geoUnit = this.unitTransform(unit);
		return this.redissonClient.getGeo(name).dist(member1, member2, geoUnit);
	}

	@Override
	public <V> Map<V, GeoPosition> pos(String name, V... members) {
		Preconditions.checkNotNull(name);
		Preconditions.checkNotNull(members);
		if (members == null || members.length < 1) {
			return Collections.emptyMap();
		}
		RGeo<V> geo = this.redissonClient.getGeo(name);
		Map<V, org.redisson.api.GeoPosition> poses = geo.pos(members);
		Map<V, GeoPosition> result = Maps.newHashMapWithExpectedSize(poses.size());
		poses.forEach((member, pos) -> {
			result.put(member, GeoPosition.of(pos.getLongitude(), pos.getLatitude()));
		});
		return result;
	}

	@Override
	public <V> Map<V, Double> radiusWithDistance(MemberArgs<V> args) {
		Preconditions.checkNotNull(args);
		Preconditions.checkNotNull(args.getName());
		Preconditions.checkNotNull(args.getMember());
		org.redisson.api.GeoUnit geoUnit = this.unitTransform(args.getUnit());
		RGeo<V> geo = this.redissonClient.getGeo(args.getName());
		return geo.searchWithDistance(
				GeoSearchArgs.from(args.getMember()).radius(args.getRadius(), geoUnit).count(args.getCount()));
	}

	@Override
	public <V> Map<V, Double> radiusWithDistance(PositionArgs args) {
		Preconditions.checkNotNull(args);
		Preconditions.checkNotNull(args.getName());
		Preconditions.checkNotNull(args.getPosition());
		org.redisson.api.GeoUnit geoUnit = this.unitTransform(args.getUnit());
		RGeo<V> geo = this.redissonClient.getGeo(args.getName());
		GeoPosition position = args.getPosition();
		return geo.searchWithDistance(GeoSearchArgs.from(position.getLongitude(), position.getLatitude())
				.radius(args.getRadius(), geoUnit).count(args.getCount()));
	}

	@Override
	public <V> Map<V, GeoPosition> radiusWithPosition(MemberArgs<V> args) {
		Preconditions.checkNotNull(args);
		Preconditions.checkNotNull(args.getName());
		Preconditions.checkNotNull(args.getMember());
		org.redisson.api.GeoUnit geoUnit = this.unitTransform(args.getUnit());
		RGeo<V> geo = this.redissonClient.getGeo(args.getName());
		Map<V, org.redisson.api.GeoPosition> geoPositions = geo.searchWithPosition(
				GeoSearchArgs.from(args.getMember()).radius(args.getRadius(), geoUnit).count(args.getCount()));
		return this.positionTransform(geoPositions);
	}

	@Override
	public <V> Map<V, GeoPosition> radiusWithPosition(PositionArgs args) {
		Preconditions.checkNotNull(args);
		Preconditions.checkNotNull(args.getName());
		Preconditions.checkNotNull(args.getPosition());
		org.redisson.api.GeoUnit geoUnit = this.unitTransform(args.getUnit());
		RGeo<V> geo = this.redissonClient.getGeo(args.getName());
		GeoPosition position = args.getPosition();
		Map<V, org.redisson.api.GeoPosition> geoPositions = geo
				.searchWithPosition(GeoSearchArgs.from(position.getLongitude(), position.getLatitude())
						.radius(args.getRadius(), geoUnit).count(args.getCount()));
		return this.positionTransform(geoPositions);
	}

	private <V> Map<V, GeoPosition> positionTransform(Map<V, org.redisson.api.GeoPosition> geoPositions) {
		Map<V, GeoPosition> result = Maps.newHashMapWithExpectedSize(geoPositions.size());
		geoPositions.forEach((k, v) -> result.put(k, GeoPosition.of(v.getLongitude(), v.getLatitude())));
		return result;
	}

	private org.redisson.api.GeoUnit unitTransform(GeoUnit unit) {
		org.redisson.api.GeoUnit geoUnit;
		switch (unit) {
		default:
		case METERS:
			geoUnit = org.redisson.api.GeoUnit.METERS;
			break;
		case KILOMETERS:
			geoUnit = org.redisson.api.GeoUnit.KILOMETERS;
			break;
		case MILES:
			geoUnit = org.redisson.api.GeoUnit.MILES;
			break;
		case FEET:
			geoUnit = org.redisson.api.GeoUnit.FEET;
			break;
		}
		return geoUnit;
	}

	private org.redisson.api.GeoEntry[] entryTransform(GeoEntry<?>... entries) {
		Preconditions.checkNotNull(entries);
		org.redisson.api.GeoEntry[] reidssonGeoEntries = new org.redisson.api.GeoEntry[entries.length];
		for (int i = 0; i < entries.length; i++) {
			GeoEntry<?> entry = entries[i];
			reidssonGeoEntries[i] = new org.redisson.api.GeoEntry(entry.getLongitude(), entry.getLatitude(),
					entry.getMember());
		}
		return reidssonGeoEntries;
	}

}
