package org.magneton.spy.mysql;

import com.google.auto.service.AutoService;
import org.magneton.spy.core.Protocol;
import org.magneton.spy.core.SumProtocol;

/**
 * @author zhangmsh 2021/7/28
 * @since 1.0.0
 */
@AutoService(Protocol.class)
public class MySqlSumProtocol implements SumProtocol {

	@Override
	public String db() {
		return "mysql";
	}

}
