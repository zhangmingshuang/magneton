/**
 * Copyright (c) 2013-2021 Nikita Koksharov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.magneton.module.geo;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Nikita Koksharov
 *
 */
@Setter
@Getter
public class GeoPosition {

	private final double longitude;

	private final double latitude;

	public GeoPosition(double longitude, double latitude) {

		Preconditions.checkArgument(longitude >= -180 && longitude <= 180, "longitude must in [-180,180]");
		Preconditions.checkArgument(latitude >= -85.05112878 && latitude <= 85.05112878,
				"latitude must in [-85.05112878,85.05112878]");

		this.longitude = longitude;
		this.latitude = latitude;
	}

	public static GeoPosition of(double longitude, double latitude) {
		return new GeoPosition(longitude, latitude);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(this.latitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(this.longitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		GeoPosition other = (GeoPosition) obj;
		if (Double.doubleToLongBits(this.latitude) != Double.doubleToLongBits(other.latitude))
			return false;
		if (Double.doubleToLongBits(this.longitude) != Double.doubleToLongBits(other.longitude))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "GeoPosition [longitude=" + this.longitude + ", latitude=" + this.latitude + "]";
	}

}
