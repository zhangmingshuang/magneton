package org.magneton.spy.sql;

import java.util.List;
import javax.annotation.Nullable;

/**
 * @author zhangmsh 2021/9/22
 * @since 1.0.0
 */
public class ResultSet {

	private final DataConverter dataConverter;

	@Nullable
	private final Object data;

	public ResultSet(DataConverter dataConverter, @Nullable Object data) {
		this.dataConverter = dataConverter;
		this.data = data;
	}

	@Nullable
	public Object getData() {
		return this.data;
	}

	public <T> T to(Class<T> clazz) {
		return this.dataConverter.to(clazz);
	}

	public <T> List<T> toList(Class<T> clazz) {
		return this.dataConverter.toList(clazz);
	}

}
